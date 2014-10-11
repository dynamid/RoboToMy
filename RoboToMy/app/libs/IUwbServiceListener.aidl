package com.bespoon.uwb;

interface IUwbServiceListener {


	/**********************  Session callbacks 	**************************/
	/**
	 * Called whenever the local UWB adapter enable state changes.
     * {@link #Uwb.ENABLE_STATE_OFF},
     * {@link #Uwb.ENABLE_STATE_TURNING_ON},
     * {@link #Uwb.ENABLE_STATE_ON},
     * {@link #Uwb.ENABLE_STATE_TURNING_OFF
	 */

	void onIUwbEnableStateChange(int prevState, int newState);

	/**
	 * Called whenever the local UWB adapter Radio state changes.
     * {@link #Uwb.RADIO_STATE_OFF},
     * {@link #STATE_TURNING_ON},
     * {@link #STATE_ON},
     * {@link #STATE_TURNING_OFF
	 */

	void onIUwbRadioStateChange(int prevState, int newState);

	/************************ Bonding Callbacks **************************/

	/**
	 * Called whenever a device wants to start a bonding process.
	 * @param device the IUwbDevice candidate for bonding
	 */
	void onIUwbDeviceBondingRequest(int shortAddress);


	/**
	 * Called whenever the device bonding state changes.
     * {@link #BOND_NONE},
     * {@link #BOND_BONDING},
     * {@link #BOND_BONDED}.
	 * @param device : the IUwbDevice candidate for bonding
	 * @param prevState : previous bonding state
	 * @param newState : new bonding state
	 * @param errno : more information about the state change mostly error codes when bonding failed
	*/
	void onIUwbDeviceBondingStateChange(int shortAddress, int prevState, int newState, int errno);

	/**
	 * Called whenever the device canceled the bonding process.
	 * @param device : the IUwbDevice candidate for bonding
	 */
	void onIUwbDeviceBondingCanceled(int shortAddress);

	/**
	 * Called whenever a bonded device wants to start a location session with the master.
	 * @param device : the IUwbDevice candidate for attachment
	 */
	void onIUwbDeviceAttachRequest(int shortAddress);

	/**
	 * Called whenever a bonded device started a location session with the master.
	 * @param device : the IUwbDevice candidate for attachment
	 */
	void onIUwbDeviceAttached(int shortAddress);

	/**
	 * Called whenever an attached device wants to leave a location session with the master.
	 * @param device : the IUwbDevice candidate for detachment
	 */
	void onIUwbDeviceDetachRequest(int shortAddress);

	/**
	 * Called whenever an attached device left a location session with the master.
	 * @param device : the IUwbDevice candidate for detachment
	 */
	void onIUwbDeviceDetached(int shortAddress);

}
