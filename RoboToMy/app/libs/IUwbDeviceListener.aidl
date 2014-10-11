package com.bespoon.uwb;

import com.bespoon.uwb.IUwbDevice;

interface IUwbDeviceListener {

	/**********************  Device Info callbacks 	**************************/

	/**
	 * Called when the device changes Battery level.
	 * @param prevLevel : previous battery level
	 * @param newLevel : new battery level
	 */
	/*void onBatteryLevelChanged(int shortAddress, int prevLevel, int newLevel);*/

	/**
	 * Called when the device enter or leave low battery state.
	 * @param isLowBat : is the device low battery?
	 */
	void onBatteryStateChanged(int shortAddress, boolean isLow);

	/**
	 * Called when the device changes motion state  .
	 * @param isMoving
	 */
	void onMotionStateChanged(int shortAddress, boolean isMoving);



	/**********************  	Ranging callbacks 	**************************/
	/**
	 * Called whenever we tried to change a device Profile.
	 * @param prevState : previous profile
	 * @param newState : new profile
	 * @param status : status of the profile change
	 */
	void onLocationProfileChanged(int shortAddress, int prevProfile, int newProfile, int status);

	/**
	 * Called whenever we tried to change a device loc Rate 
	 * or whenever an agile tag changed its locrate.
	 * @param prevState : previous LocRate
	 * @param newState : new LocRate
	 * @param status : status of the LocRate change
	 */
	void onLocationRateChanged(int shortAddress, int prevLocRate, int newLocRate, int status);

	/**
	 * Called whenever a device distance changes.
	 * @param out accuracy : the distance last value accuracy
	 * @param out timestamp : the distance last value timestamp in nanosecond
	 * @param distance last known device distance to this local adapter
	 */
	void onDistanceChanged(int shortAddress, int accuracy, long timestamp, float distance);

    /**
	 * Called whenever a device location changes.
	 * @param out accuracy : the location last value accuracy
	 * @param out timestamp : the location last value timestamp in nanosecond
	 * @param out x,y,z : the location last coordinates
	 */
	void onLocationChanged(int shortAddress, int accuracy, long timestamp, float x, float y, float z);
	
	/**
	 * Called when a previously known device becomes unreachable.
	 * @see #onRecovered()
	 */
	void onLost(int shortAddress);
	
	/**
	 * Called when a previously unreachable device becomes reachable again.
	 * @see #onLost()
	 */
	void onRecovered(int shortAddress);


	/**********************  	Data callbacks 	**************************/
	/**
	 * Called whenever we tried to send data to the device 
	 * @param status : status of the data transfert
	 */
	void onDataSent(int shortAddress, int status);

	/**
	 * Called whenever the device sent us some user data .
	 * @param data : byte stream from device
	 */
	void onDataReceived(int shortAddress, in byte[] data);


	/**********************  	Sensor callbacks 	**************************/
	/**
	 * Called whenever the device sent us some sensor data.
	 * @param sensor : the refreshed data sensor
	 * @param out activeSensor : the sensor with new data
	 * @param out accuracy : the sensor last value accuracy
	 * @param out timestamp : the sensor last value timestamp in nanosecond
	 * @param out data : The length and contents of the value array depends on which sensor type is being monitored
	 */
	void onSensorDataReceived(int shortAddress, int activeSensor, int accuracy, long timestamp, in float[] data);

}
