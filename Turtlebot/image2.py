#!/usr/bin/env python
import cv2
import rospy
from sensor_msgs.msg import Image
from cv_bridge import CvBridge, CvBridgeError
import datetime
import numpy as np
import socket
import sys
import threading
import select
import signal

class ImageConvertor:
 
    c = 0
 
    def __init__(self):
        self.server = None
        self.old = None
        # Refresh rate( Framse/sec)
        self.refresh_rate = 4
        print "Starting saving process"
        rospy.init_node('image_converter', anonymous=True)
        self.bridge = CvBridge()
        # Subscribe to listen the for new row images
        self.image_sub = rospy.Subscriber(
            '/camera/rgb/image_raw', Image, self._save_image)
        self.c = 0
        try:
            rospy.spin()
        except KeyboardInterrupt:
            print "Shutting down"
 
    def _save_image(self, data):
        now = datetime.datetime.now()
        milis = 0
        if self.old:
            diff = now - self.old
            milis = diff.microseconds / 10
            if milis < self.refresh_rate:
                return
        if self.server == None:
            t1 = threading.Thread(target=self.start_server)
            t1.setDaemon(True)
            t1.start()
        try:
            # Convert the bites to open cv image
            cv_image = self.bridge.imgmsg_to_cv(data, "bgr8")
            # Generate the image object
            _, data = cv2.imencode(
                '.jpg', np.asarray(cv_image), [cv2.cv.CV_IMWRITE_JPEG_QUALITY, 20])
            if not self.server:
                return
            for c in self.server.threads:
                # Send the image as a string to each connected client(Limited
                # to 1 client)
                c.q = data.tostring()
        except CvBridgeError, e:
            print "image conversion error:" + str(e)
        self.old = now
 
    def start_server(self):
        self.server = Server()
        self.server.run()
 
class Server:
 
    def __init__(self):
        self.host = ''
        self.port = 8090
        self.backlog = 5
        self.size = 1024
        self.server = None
        self.threads = []
 
    def open_socket(self):
        # Open a new socket
        try:
            self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.server.bind((self.host, self.port))  # Tuple
            self.server.listen(5)
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
                    c = Client(self.server.accept())
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
 
    def __init__(self, (client, address)):
        threading.Thread.__init__(self)
        self.client = client
        self.address = address
        self.size = 1024
        self.send_header = False
        self.q = None
 
    def generate_http_header(self):
        """ Multipart image header to allow jpg streaming to a <img> tag"""
        response = []
        response.append('HTTP/1.1 200 OK')
        response.append("Content-Type:image/jpeg")
        return '\r\n'.join(response)
 
    def generate_chunck_header(self, size):
        """ Frame header """
        response = []
        response.append("Content-Length:" + str(size))
        response.append("\r\n")
        return '\r\n'.join(response)
 
    def run(self):
        data = self.client.recv(self.size)
        # First send the stream header
        self.client.send(self.generate_http_header())
        while True:
            try:
                data = self.q
                if not data:
                    continue
                # Send the frame header
                self.client.send(self.generate_chunck_header(len(data)))
                # Send the frame
                self.client.send(data)
                # Send the frame separator
                self.client.close() 
            except Exception, e:
                print "image processor error:" + str(e)
                self.client.close()
                return
 
 
def signal_handler(signal, frame):
    if ic:
        ic.server.close()
    sys.exit(0)
 
signal.signal(signal.SIGINT, signal_handler)
ic = None
 
if __name__ == '__main__':
    ic = ImageConvertor()
