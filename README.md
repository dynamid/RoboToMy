RoboToMy
========

This project was created in the context of New technologies to enable eHealth applications.

Material
========
- Turtlebot with an embedded kinect 
- UWB sensors for wearable applications with an android phone

The idea is to control the Turtlebot (rotation and direction) with an android app wich is connected with some UWB sensors embedded on a human body. The person is able to do different gestures which are recognized with the UWB sensors in order to send instructions to the turtlebot. For sake of simplicity, we embedded 2 UWB sensors, one in the left wrist, one in the right leg and the android phone on the right hand. Then we use the RoboToMy app to interface both technologies.

The user needs to use the RoboToMy app from the android phone with the following instructions:

Calibration phase
========
- First it is necessary to choose which sensor will be used to control the direction and the rotation of the robot. You need to specify for each sensor if it will be placed on hand and his netural value

Launch app
========
- The app controls the movement of the robot and displays the kinnect image on the screen of the phone.
