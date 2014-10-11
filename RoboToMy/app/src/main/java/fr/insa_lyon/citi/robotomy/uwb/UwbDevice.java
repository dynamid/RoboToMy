package fr.insa_lyon.citi.robotomy.uwb;


import android.os.Handler;
import android.os.RemoteException;


import android.util.Pair;


import com.bespoon.uwb.IUwbDevice;
import com.bespoon.uwb.IUwbDeviceListener;

import java.util.ArrayList;

import fr.insa_lyon.citi.robotomy.tools.AppTools;

public class UwbDevice {


    public interface UwbDeviceListener {

        public void onDistanceChanged(UwbDevice device, int accuracy, long timestamp, float distance);

        public void onLocationChanged(UwbDevice device, int accuracy, long timestamp, float x, float y, float z);

        public void onLost(UwbDevice device);

        public void onRecovered(UwbDevice device);

        public void onLocationProfileChanged(UwbDevice device, int prevProfile, int newProfile, int status);

        public void onBatteryLevelChanged(UwbDevice device, int prevLevel, int newLevel);

        public void onBatteryStateChanged(UwbDevice device, boolean isLow);
    }

    private static class SafeUwbDeviceListener extends Pair<UwbDeviceListener, Handler> {
        public SafeUwbDeviceListener(UwbDeviceListener first, Handler second) {
            super(first, second);
        }
    }

    protected IUwbDevice mIUwbDevice;
    protected int mShortAddress;
    protected ArrayList<SafeUwbDeviceListener> mListeners = new ArrayList<SafeUwbDeviceListener>();

    private int leg_hand = 0;
    private float zero_value = 0;

    public int getLeg_hand(){
        return leg_hand;
    }
    public void setLeg_hand(int v){
        this.leg_hand = v;
    }

    public float getZero_value(){
        return zero_value;
    }

    public void setZero_value(float v){
        this.zero_value = v;
    }
    UwbDevice(IUwbDevice from) throws RemoteException {
        mIUwbDevice = from;
        mShortAddress = mIUwbDevice.getShortAddress();
        mIUwbDevice.registerListener(mIUwbDeviceListener);
    }
        @Override
        protected void finalize() throws Throwable {
            mIUwbDevice.unregisterListener(mIUwbDeviceListener);
        }

    public void registerListener(UwbDeviceListener listener) {
        AppTools.info("registerListener: " + listener);
        mListeners.add(new SafeUwbDeviceListener(listener, new Handler()));
    }

    public void unregisterListener(UwbDeviceListener listener) {
        AppTools.info("unregisterListener: " + listener);
        for(SafeUwbDeviceListener e : mListeners) {
            if(e.first == listener) {
                mListeners.remove(e);
                break;
            }
        }
    }
    public static String getDeviceIdentity(UwbDevice device) {
        if (device.getName() != null) {
            return device.getName();
            //return device.getLastKnownDistance();
        }
        else {
            return String.valueOf(device.getShortAddress());
        }
    }
    public int getShortAddress() {
        return mShortAddress;
    }

    public String getMacAddress() {
        try {
            return mIUwbDevice.getMacAddress();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isBonded() {
        try {
            return mIUwbDevice.isBonded();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isAttached() {
        try {
            return mIUwbDevice.isAttached();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getHardwareVersion() {
        try {
            return mIUwbDevice.getHardwareVersion();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int getFirmwareVersion() {
        try {
            return mIUwbDevice.getFirmwareVersion();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setName(String name) {
        try {
            mIUwbDevice.setName(name);
        } catch (RemoteException e) {
            // pass
        }
    }

    public String getName() {
        try {
            return mIUwbDevice.getName();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isLowBattery() {
        try {
            return mIUwbDevice.isLowBattery();
        } catch (RemoteException e) {
            return false;
        }
    }

    private IUwbDeviceListener mIUwbDeviceListener = new IUwbDeviceListener.Stub() {
        @Override
        public void onBatteryStateChanged(int shortAddress, boolean isLow) throws RemoteException {
        }

        @Override
        public void onMotionStateChanged(int shortAddress, boolean isMoving) throws RemoteException {

        }

        @Override
        public void onLocationProfileChanged(int shortAddress, int prevProfile, int newProfile, int status) throws RemoteException {

        }

        @Override
        public void onLocationRateChanged(int shortAddress, int prevLocRate, int newLocRate, int status) throws RemoteException {

        }

        @Override
        public void onDistanceChanged(int shortAddress, final int accuracy, final long timestamp, final float distance) throws RemoteException {
            for(final SafeUwbDeviceListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onDistanceChanged(UwbDevice.this, accuracy, timestamp, distance);
                    }
                });
            }
        }

        @Override
        public void onLocationChanged(int shortAddress, final int accuracy, final long timestamp, final float x, final float y, final float z) throws RemoteException {
            for(final SafeUwbDeviceListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onLocationChanged(UwbDevice.this, accuracy, timestamp, x, y, z);
                    }
                });
            }
        }

        @Override
        public void onLost(int shortAddress) throws RemoteException {
            for(final SafeUwbDeviceListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onLost(UwbDevice.this);
                    }
                });
            }
        }

        @Override
        public void onRecovered(int shortAddress) throws RemoteException {
            for(final SafeUwbDeviceListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onRecovered(UwbDevice.this);
                    }
                });
            }
        }

        @Override
        public void onDataSent(int shortAddress, int status) throws RemoteException {

        }

        @Override
        public void onDataReceived(int shortAddress, byte[] data) throws RemoteException {

        }

        @Override
        public void onSensorDataReceived(int shortAddress, int activeSensor, int accuracy, long timestamp, float[] data) throws RemoteException {

        }
    };

}
