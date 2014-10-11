package com.bespoon.uwb;

import com.bespoon.uwb.IUwbServiceListener;
import com.bespoon.uwb.IUwbDevice;

interface IUwbService {



    void registerListener(IUwbServiceListener listener);
    void unregisterListener(IUwbServiceListener listener);


	//*********************************************************************************
	//***************************   Session control  **********************************
	//*********************************************************************************

    /**
     * Turn on the local UWB adapter&mdash;do not use without explicit
     * user action to turn on UWB Location.
     * <p>This powers on the underlying UWB hardware, starts UWB radio and starts all
     * UWB Location system services.
     * <p class="caution"><strong>UWB should never be enabled without
     * direct user consent</strong>. If you want to turn on UWB in order
     * to create a wireless network, you should use the {@link
     * #ACTION_REQUEST_ENABLE} Intent, which will raise a dialog that requests
     * user permission to turn on UWB Location. The {@link #enable()} method is
     * provided only for applications that include a user interface for changing
     * system settings, such as a "power manager" app.</p>
     * <p>This is an asynchronous call: it will return immediately, and
     * clients should listen for {@link #onUWBStateChange()}
     * to be notified of subsequent adapter state changes. If this call returns
     * true, then the adapter state will immediately transition from {@link
     * #STATE_OFF} to {@link #STATE_TURNING_ON}, and some time
     * later transition to either {@link #STATE_OFF} or {@link
     * #STATE_ON}. If this call returns false then there was an
     * immediate problem that will prevent the adapter from being turned on -
     * such as Airplane mode, or the adapter is already turned on.
     * <p>Requires the {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * permission
     *
     * @return true to indicate adapter startup has begun, or false on
     *         immediate error
     */
    boolean enable();

    /**
     * Turn off the local UWB adapter&mdash;do not use without explicit
     * user action to turn off UWB.
     * <p>This gracefully shuts down UWB radio, all UWB connections, stops UWB
     * system services, and powers down the underlying UWB hardware.
     * <p class="caution"><strong>UWB should never be disabled without
     * direct user consent</strong>. The {@link #disable()} method is
     * provided only for applications that include a user interface for changing
     * system settings, such as a "power manager" app.</p>
     * <p>This is an asynchronous call: it will return immediately, and
     * clients should listen for {@link #onUWBStateChange()}
     * to be notified of subsequent adapter state changes. If this call returns
     * true, then the adapter state will immediately transition from {@link
     * #STATE_ON} to {@link #STATE_TURNING_OFF}, and some time
     * later transition to either {@link #STATE_OFF} or {@link
     * #STATE_ON}. If this call returns false then there was an
     * immediate problem that will prevent the adapter from being turned off -
     * such as the adapter already being turned off.
     * <p>Requires the {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * permission
     *
     * @return true to indicate adapter shutdown has begun, or false on
     *         immediate error
     */
    boolean disable();

   /**
     * Get the current state of the local UWB_location adapter.
     * <p>Possible return values are
     * {@link #ENABLE_STATE_OFF},
     * {@link #ENABLE_STATE_TURNING_ON},
     * {@link #ENABLE_STATE_ON},
     * {@link #ENABLE_STATE_TURNING_OFF}.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return current state of the local UWB adapter
     */
    int getEnableState();

    /**
     * Turn on Radio.This turns on the Local UWB Radio.
     * Can be used as generic API for any other radio turn on also (cf AirPlane mode).
     * <p>This is an asynchronous call: it will return immediately, and
     * clients should listen for {@link #onUWBRadioStateChange()}
     * to be notified of subsequent UWB Radio state changes. If this call returns
     * true, then the Radio state will immediately transition from {@link
     * #STATE_OFF} to {@link #STATE_TURNING_ON}, and some time
     * later transition to either {@link #STATE_OFF} or {@link
     * #STATE_ON}. If this call returns false then there was an
     * immediate problem that will prevent the UWB radio from being turned on -
     * such as Airplane mode, or the UWB Radio is already turned on.
     * <p>Requires the {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * permission
     *
     * @return true to indicate UWB radio enabling has begun, or false on
     *         immediate error
     */
    boolean enableRadio();

    /**
     * Turn Off Radio.This turns Off the Local UWB Radio.
     * Can be used as generic API for any other radio turn Off also (cf AirPlane mode).
     * <p>This is an asynchronous call: it will return immediately, and
     * clients should listen for {@link #onUWBRadioStateChange()}
     * to be notified of subsequent UWB Radio state changes. If this call returns
     * true, then the Radio state will immediately transition from {@link
     * #STATE_ON} to {@link #STATE_TURNING_OFF}, and some time
     * later transition to either {@link #STATE_ON} or {@link
     * #STATE_OFF}. If this call returns false then there was an
     * immediate problem that will prevent the UWB radio from being turned off -
     * such as the UWB Radio is already turned off.
     * <p>Requires the {@link android.Manifest.permission#UWB_LOCATION_ADMIN}
     * permission
     *
     * @return true to indicate UWB radio disabling has begun, or false on
     *         immediate error
     */
    boolean disableRadio();

   /**
     * Get the current state of the local UWB_location adapter.
     * <p>Possible return values are
     * {@link #RADIO_STATE_OFF},
     * {@link #RADIO_STATE_TURNING_ON},
     * {@link #RADIO_STATE_ON},
     * {@link #RADIO_STATE_TURNING_OFF}.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return current state of the local UWB adapter
     */
    int getRadioState();


	//*********************************************************************************
	//***************************  Discovery control **********************************
	//*********************************************************************************

    /**
     * Get the current UWB scan mode of the local UWB adapter.
     * <p>The UWB scan mode determines if the local adapter is
     * discoverable from remote UWB devices.
     * <p>Possible values are:
     * {@link #SCAN_MODE_NONE},
     * {@link #SCAN_MODE_DISCOVERABLE},
     * {@link #SCAN_MODE_DISCOVERABLE_SECURE}.
     * <p>If UWB state is not {@link #STATE_ON}, this API
     * will return {@link #SCAN_MODE_NONE}. After turning on UWB,
     * wait for {@link #onUWBStateChange()} with {@link #STATE_ON}
     * to get the updated value.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}
     *
     * @return scan mode
     */
    int getScanMode();

	/**
     * Set the UWB scan mode of the local UWB adapter.
     * <p>The UWB scan mode determines if the local adapter is
     * discoverable from remote UWB devices.
     * <p>For privacy reasons, discoverable mode is automatically turned off
     * after <code>duration</code> seconds. For example, 30 seconds should be
     * enough for a remote device to initiate and complete its discovery
     * process.
     * <p>Valid scan mode values are:
     * {@link #SCAN_MODE_NONE},
     * {@link #SCAN_MODE_DISCOVERABLE},
     * {@link #SCAN_MODE_DISCOVERABLE_SECURE}.
     * <p>If UWB state is not {@link #STATE_ON}, this API
     * will return false. After turning on UWB,
     * wait for {@link #ACTION_STATE_CHANGED} with {@link #STATE_ON}
     * to get the updated value.
     *
     * @param mode valid scan mode
     * @param duration time in seconds to apply scan mode, only used for
     *                 {@link #SCAN_MODE_DISCOVERABLE}
					   {@link #SCAN_MODE_DISCOVERABLE_SECURE}
     * @return     true if the scan mode was set, false otherwise
	 *
     * @hide
     */
    boolean setScanMode(int mode, int duration);

    /**
     * Start a bonding (pairing) process with a remote Master.
	 * The master device is identified by its short adress (24bits)
	 * The bonding process can be standard or secure
	 * The local adapter can only be bonded with one master
     * <p>This is an asynchronous call, it will return immediately.
     * Listen to {@link #onBondingStateChange()} to be notified when
     * the bonding process completes, and its result.
	 *
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
     *
   	 * @param mode valid bonding mode (BONDING_STANDARD,BONDING_SECURE etc..)
     * @param masterShortAdress - The remote Master to start bonding with
	 *
     * @return false on immediate error (ex already bonded), true if bonding will begin
	 *
     * @hide
     */
    boolean startBonding(int mode,int masterShortAdress);

    /**
     * Start the bonding (pairing) process with a remote master using some
     * Out Of Band data (got from trust channel such as GSM,secure IP).
     *
     * <p>This is an asynchronous call, it will return immediately.
     * Listen to {@link #onBondingStateChange()} to be notified when
     * the bonding process completes, and its result.
     *
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
     *
     * @param masterShortAdress - The remote device to start bonding with
   	 * @param bondingMode valid bondig mode (BONDING_STANDARD,BONDING_SECURE etc..)
	 * @param data : some data acquired from other channels ( ex : binary sms)
     *
     * @hide
     */
    boolean startBondingOutOfBand(int bondingMode, int masterShortAdress, in byte[] data);


    /**
     * Accept the current bonding process initiate by the IUwbDevice
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @see Callback : onDeviceBondingRequest()
	 * @param device the IUwbDevice candidate for bonding
	 *
     * @hide
     */
	void acceptBondingRequest(int shortAddress);

    /**
     * Decline the current bonding process initiate by the IUwbDevice
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @see Callback : onDeviceBondingRequest()
	 * @param device the IUwbDevice candidate for bonding
	 *
     * @hide
     */
	void declineBondingRequest(int shortAddress);

    /**
     * Cancel an in-progress bonding process.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @param device the IUwbDevice candidate for bonding
     * @return true on success, false on error
     * @hide
     */
    boolean cancelBondingProcess(int shortAddress);

    /**
     * Remove bond (pairing) with the remote device.
     * <p>Delete the link key associated with the remote device, and
     * immediately terminate connections to that device that require
     * authentication and encryption.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @param device the bonded IUwbDevice
     * @return true on success, false on error
	 *
     * @hide
     */
    boolean removeBond(int shortAddress);

    /**
     * Accept the current attach process initiated by the IUwbDevice
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @see Callback : onAttachRequest()
	 * @param device the IUwbDevice candidate for attachment
	 *
     * @hide
     */
	void acceptAttachRequest(int shortAddress);

    /**
     * Decline the current attach process initiated by the IUwbDevice
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION_ADMIN}.
	 *
	 * @see Callback : onAttachRequest()
	 * @param device the IUwbDevice candidate for attachment
	 *
     * @hide
     */
	void declineAttachRequest(int shortAddress);

    /**
     * Return the set of devices that are bonded
     * (paired) to the local adapter.
     * Objects can be :
     * -> several tags paired with the local dapter
     * -> One (and only one) other master if the local adapter is paired with another master
     * <p>If UWB state is not {@link #STATE_ON}, this API
     * will return an empty set. After turning on UWB,
     * wait for {@link #onUWBStateChange()} with {@link #STATE_ON}
     * to get the updated value.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}.
     *
     * @return list of devices mac addresss, or null if error
     */
    int[] getBondedDevices();


    /**
     * Return the set of device that are bonded
     * (paired) to the local adapter and attached (active).
     * <p>If UWB state is not {@link #STATE_ON}, this API
     * will return an empty set. After turning on UWB,
     * wait for {@link #onUWBStateChange()} with {@link #STATE_ON}
     * to get the updated value.
     * <p>Requires {@link android.Manifest.permission#UWB_LOCATION}.
     *
     * @return list of devices mac addresss, or null if error
     */
   int[] getAttachedDevices();

    /**
     * Retrieve a device by its mac address
     * @return null if no device found
     */
    IUwbDevice getDevice(int shortAddress);

    /**
     * Access to the local device provided by this adapter.
     */
    IUwbDevice getLocalDevice();

    // TODO remove this once the bonding process is fully implemented
    void addManualBond(int shortAddress);
}
