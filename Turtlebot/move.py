#!/usr/bin/env python
import rospy
import socket
import sys
import threading
import select
import datetime
import signal
from geometry_msgs.msg import Twist

DEBUG = True


def printf(msg):
    if DEBUG:
        print "****** %s" % msg


class Move:

    def __init__(self):
        self.server = None
        print "Starting Move API"
        rospy.init_node('moveAPI')
        t1 = threading.Thread(target=self.start_server)
        t1.setDaemon(True)
        t1.start()
        self.twist = Twist()
        t2 = threading.Thread(target=self.publish_context)
        t2.setDaemon(True)
        t2.start()
        self.lastmessage = datetime.datetime.now()
        try:
            rospy.spin()
        except KeyboardInterrupt:
            print "Shutting down MAP API"

    def _do_movement(self, data):
        """ Performs the movment"""
        self.lastmessage = datetime.datetime.now()
        d = [int(l) for l in data.split('|')]
        if d[0] not in [-1, 0, 1] or d[1] < 0 or d[1] > 50 or d[2] not in [-1, 0, 1] or d[3] < 0 or d[3] > 50:
            return
        self.twist.linear.x = d[0] * d[1] / 100.0
        self.twist.angular.z = d[2] * d[3] / 10.0
        printf(str(self.twist))
        

    def publish_context(self):
        pub = rospy.Publisher('/cmd_vel_mux/input/teleop', Twist)
        while True:
            diff = (datetime.datetime.now() - self.lastmessage).seconds
            if not rospy.is_shutdown() and diff < 2:
                pub.publish(self.twist)
            rospy.sleep(0.1)

    def start_server(self):
        self.server = Server(self)
        self.server.run()


class Server:

    def __init__(self, parent):
        self.host = ''
        self.port = 8081
        self.backlog = 5
        self.size = 1024
        self.server = None
        self.threads = []
        self.parent = parent

    def open_socket(self):
        # Open a new socket
        try:
            self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server.bind((self.host, self.port)) 
            self.server.listen(1)
        except socket.error, (value, message):
            if self.server:
                self.server.close()
            print "Could not open socket :" + str(message)
            sys.exit(1)

    def run(self):
        self.open_socket()
        input = [self.server, sys.stdin]
        running = 1
        while running == 1:
            inputready, outputready, exceptready = select.select(input, [], [])
            for s in inputready:
                if s == self.server:
                    c = Client(self.server,self.parent)
                    c.start()
                    self.threads.append(c)
                elif s == sys.stdin:
                    sys.stdin.readline()
                    self.close()

    def close(self):
        self.server.close()
        print "Exiting..."
        for c in self.threads:
            c.join()


class Client(threading.Thread):

    def __init__(self,server, parent):
        threading.Thread.__init__(self)
        self.size = 1024
        self.send_header = False
        self.q = None
        self.server = server
        self.parent = parent

    def run(self):
        try:
            conn,addr = self.server.accept() 
            data = conn.recv(self.size)
            printf(data)
            if data:
                self.parent._do_movement(data)
        except Exception, e:
            print "Map Move Client error:" + str(e)


def signal_handler(signal, frame):
    if ic:
        ic.server.close()
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)
ic = None

if __name__ == '__main__':
    ic = Move()
