package com.bespoon.uwb;

import com.bespoon.uwb.IUwbDeviceListener;

interface IUwbDevice {

    void registerListener(IUwbDeviceListener listener);
    void unregisterListener(IUwbDeviceListener listener);

	//********************************************************************************
	//*************************  	Pairing control 	******************************
	//********************************************************************************

    /**
     * Returns the device bonding capabilities (STANDARD,SECURE etc...)
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	int getBondingCapabilities();

	/**
     * Get the bond state of the remote device.
     * <p>Possible values for the bond state are:
     * {@link #BOND_NONE},
     * {@link #BOND_BONDING},
     * {@link #BOND_BONDED}.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}.
     *
     * @return the bond state
	*/
    int getBondState();

    /**
     * Returns true if the device has done successfully all the bonding process
	 * It can now attach/detach with the master
     * <p>Equivalent to:
     * <code>getBondState() == BOND_BONDED</code>
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	boolean isBonded();

    /**
     * Returns true if the device has done successfully all the attachment process
	 * It is now considered as "active"
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	boolean isAttached();


	//********************************************************************************
	//*************************  	Device Infos	 	******************************
	//********************************************************************************

    /**
     * Returns the hardware address of the Device : 128bits .
     * <p>For example, "2001:0db8:0000:85a3:0000:0000:ac1f:8001".
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return UWB hardware address as string
     */
    String getMacAddress();

    /**
     * Returns the short (hashed) address of the device : 24bits .
     * <p>For example : 0x0be5d00b.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return UWB hardware address as string
     */
    int getShortAddress();

    /**
     * Returns the Hardware version of the device : 5bits .
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return device hardware version
     */
	int getHardwareVersion();

    /**
     * Returns the Firmware version of the device : 5bits .
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return device firmware version
     */
	int getFirmwareVersion();

    /**
     * Set a friendly name for this device.
     * Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     */
    void setName(String name);

    /**
     * Retrieve the friendly name for this device.
     */
    String getName();

    /**
     * Returns the device battery level (0->100%).
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	int getBatteryLevel();

    /**
     * Returns if the device is in the low battery state .
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	boolean isLowBattery();

    /**
     * Returns if the device is the moving or not .
     * <p>Equivalent to:
     * <code>getMovingState() != MOVSTATE_STATIC </code>
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	boolean isMoving();

    /**
     * Get the device moving state.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	int getMovingState();

    /**
     * Set if the device is moving or not .
	 * Only available for local adapter
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	/*void setMovingState(int moveState);*/

	/**
     * The Device has network access ?
	 * tells if the device can used assisted pairing via IP connection
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
	*/
	boolean hasNetworkAccess();

	/**
     * The Device has GSM access ?
	 * tells if the device can used assisted pairing via GSM like network
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
	*/
	boolean hasGsmAccess();

    /**
     * Returns a bitmap of  location profiles supported by the device (TYPE_TAG,TYPE_BASE etc...)
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     * @return bitmap of device supported location profile
     */
	int getSupportedLocationProfiles();

    /**
     * Returns the device currrent location profile (TYPE_TAG,TYPE_BASE etc...)
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	int getLocationProfile();

    /**
     * Returns a bitmap of location rate supported by the device (STATIC,AGILE,LOW,etc...)
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     * @return bitmap of device supported location rates
     */
	int getSupportedLocationRate();

    /**
     * Returns the device currrent LocRate (STATIC,AGILE,LOW,etc..)
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	int getLocationRate();


	//********************************************************************************
	//**********************  		Ranging control 	******************************
	//********************************************************************************

    /**
     * Set the Local adapter current profile.
     *
     * <p>This is an asynchronous call, it will return immediately.
     * Listen to {@link #onLocationProfilechanged()} to be notified when
     * the profile changed  and its result.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
	 * @param locationProfile : TYPE_TAG,TYPE_BASE etc..
     * @hide
     */
    int setLocationProfile(int locationProfile);

    /**
     * Set device Location Rate.
     *
     * <p>This is an asynchronous call, it will return immediately.
     * Listen to {@link #onLocationRatechanged()} to be notified when
     * the location rate changed  and its result.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
	 * @param locationRate : LOCRATE_SLOW|MEDIUM|FAST|STATIC|AGILE
     * @hide
     */
	int setLocationRate(int locationRate);


	//********************************************************************************
	//**********************  		Sensor control 		******************************
	//********************************************************************************

    /**
     * Returns a bitmap of sensors supported by the device
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
 	 * @return bitmap of supported sensors (TYPE_ACCELEROMETER,TYPE_GRAVITY, etc..)
     */
	int getSupportedSensor();

    /**
     * Get the device sensor max range and resolution
	 * @param : sensor Android sensor type :
	 *  TYPE_ACCELEROMETER,TYPE_GRAVITY,TYPE_GYROSCOPE,TYPE_MAGNETIC_FIELD,TYPE_PRESSURE...
	 * @param  maxRange : maximum range of the sensor in the sensor's unit.
	 * @param  resolution : resolution of the sensor in the sensor's unit.
	 * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	void getSensorInfo(int sensor , in float[] maxRange, in float[] resolution);

    /**
     * Get the device sensor last value
	 * @param sensor : Android sensor type
	 *  TYPE_ACCELEROMETER,TYPE_GRAVITY,TYPE_GYROSCOPE,TYPE_MAGNETIC_FIELD,TYPE_PRESSURE...
	 * @param  in sensor : the sensor we want to get data
	 * @param  out accuracy : the sensor last value accuracy
	 * @param  out timestamp : the sensor last value timestamp in nanosecond
	 * @param  out value : The length and contents of the value array depends on which sensor type is being monitored
	 * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     */
	void getSensorValue(int sensor , int accuracy , long timestamp, in float[] value);


	//********************************************************************************
	//*************************  		Data control 	******************************
	//********************************************************************************

    /**
     * Send data to the remote device.
     *
     * <p>This is an asynchronous call, it will return immediately.
     * Listen to {@link #onDeviceDatasent()} to be notified when
     * the data are sent, and its result.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
	 * @param data : byte stream
     * @hide
     */
	void sendData(in byte[] data);


	//********************************************************************************
	//*************************  	Secure control 		******************************
	//********************************************************************************

    /**
     * Get secure key from device.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * @hide
     */
	void getSecureKey(out byte[] key);

    /**
     * set secure key of the device.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * @hide
     */
	void setSecureKey(in byte[] key);


	//********************************************************************************
	//*************************  	 MAC control 		******************************
	//********************************************************************************

    /**
     * Get device current IT_start mac settings
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * @hide
     */
	int getMacInfoItStart();

    /**
     * Get device current IT_slot mac settings
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * @hide
     */
	int getMacInfoItSlot();

}
