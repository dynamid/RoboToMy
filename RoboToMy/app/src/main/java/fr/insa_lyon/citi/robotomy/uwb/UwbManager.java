package fr.insa_lyon.citi.robotomy.uwb;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Pair;
import android.util.SparseArray;

import com.bespoon.uwb.IUwbDevice;
import com.bespoon.uwb.IUwbService;
import com.bespoon.uwb.IUwbServiceListener;

import fr.insa_lyon.citi.robotomy.tools.Constants;

import java.util.ArrayList;
import java.util.List;

public class UwbManager {

    public interface UwbManagerListener {
        public void onUWBManagerCreated(UwbManager manager);
        public void onUWBManagerDestroyed(UwbManager manager);

        void onUwbEnableStateChanged(int prevState, int newState);

        void onUwbRadioStateChanged(int prevState, int newState);

        void onUwbDeviceAttached(UwbDevice device);

        void onUwbDeviceDetached(UwbDevice device);
    }

    private static class SafeUwbManagerListener extends Pair<UwbManagerListener, Handler> {
        public SafeUwbManagerListener(UwbManagerListener first, Handler second) {
            super(first, second);
        }
    }

    private Context mContext;
    private ArrayList<SafeUwbManagerListener> mListeners = new ArrayList<SafeUwbManagerListener>();
    private boolean mIUwbServiceBound;
    private IUwbService mIUwbService;
    private SparseArray<UwbDevice> mUwbDevices;
    private UwbDevice mUwbDevice;

    public UwbManager() {
    }

    public void registerUWBManagerListener(UwbManagerListener l) {
        mListeners.add(new SafeUwbManagerListener(l, new Handler()));
    }

    public void unregisterUWBManagerListener(UwbManagerListener l) {
        for(SafeUwbManagerListener e : mListeners) {
            if(e.first == l) {
                mListeners.remove(e);
                break;
            }
        }
    }

    public boolean create(Context context) {
        if(!mIUwbServiceBound) {
            mIUwbServiceBound = context.bindService(new Intent(IUwbService.class.getName()), mConnection, Context.BIND_AUTO_CREATE);
            if(mIUwbServiceBound) {
                mContext = context;
            }
        }

        return mIUwbServiceBound;
    }

    public void destroy() {
        if(mIUwbServiceBound) {
            mContext.unbindService(mConnection);
            mContext = null;
            mIUwbServiceBound = false;
        }
    }

    public boolean enable() {
        if(mIUwbService == null) {
            return false;
        }

        try {
            return mIUwbService.enable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean disable() {
        if(mIUwbService == null) {
            return false;
        }

        try {
            return mIUwbService.disable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getEnableState() {
        if(mIUwbService == null) {
            return Constants.ENABLE_STATE_OFF;
        }

        try {
            return mIUwbService.getEnableState();
        } catch(RemoteException e) {
            return Constants.ENABLE_STATE_OFF;
        }
    }

    public boolean enableRadio() {
        if(mIUwbService == null) {
            return false;
        }

        try {
            return mIUwbService.enableRadio();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean disableRadio() {
        if(mIUwbService == null) {
            return false;
        }

        try {
            return mIUwbService.disableRadio();
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getRadioState() {
        if(mIUwbService == null) {
            return Constants.RADIO_STATE_OFF;
        }

        try {
            return mIUwbService.getRadioState();
        } catch(RemoteException e) {
            return Constants.RADIO_STATE_OFF;
        }
    }

    public List<UwbDevice> getBondedDevices() {
        if(mIUwbService == null) {
            return null;
        }

        try {
            int[] shortAddresses = mIUwbService.getBondedDevices();
            return getUwbDevicesForShortAddresses(shortAddresses);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<UwbDevice> getAttachedDevices() {
        if(mIUwbService == null) {
            return null;
        }

        try {
            int[] shortAddresses = mIUwbService.getAttachedDevices();
            return getUwbDevicesForShortAddresses(shortAddresses);
        } catch (RemoteException e) {
            return null;
        }
    }

    public UwbDevice getLocalDevice() {
        if(mIUwbService == null) {
            return null;
        }

        if(mUwbDevice != null) {
            return mUwbDevice;
        }

        try {
            IUwbDevice ld = mIUwbService.getLocalDevice();
            mUwbDevice = new UwbDevice(ld);
            return mUwbDevice;
        } catch (RemoteException e) {
            return null;
        }
    }

    public UwbDevice getUwbDevice(int shortAddress) {
        if(mIUwbService == null) {
            return null;
        }

        try {
            return getUwbDeviceInternal(shortAddress);
        } catch(RemoteException e) {
            return null;
        }
    }

    public void removeBond(int shortAddress) {
        if(mIUwbService == null) {
            return;
        }

        try {
            mIUwbService.removeBond(shortAddress);
            removeDevice(shortAddress);
        } catch(RemoteException e) {
            // pass
        }
    }

    public void addManualBond(int shortAddress) {
        if(mIUwbService == null) {
            return;
        }

        try {
            mIUwbService.addManualBond(shortAddress);
        } catch(RemoteException e) {
            // pass
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mIUwbService = IUwbService.Stub.asInterface(service);
            mUwbDevices = new SparseArray<UwbDevice>();
            mUwbDevice = null;
            try {
                mIUwbService.registerListener(mIUwbServiceListener);
                for(final SafeUwbManagerListener l : mListeners) {
                    l.second.post(new Runnable() {
                        @Override
                        public void run() {
                            l.first.onUWBManagerCreated(UwbManager.this);
                        }
                    });
                }
            } catch(RemoteException e) {
                mIUwbService = null;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mIUwbService.unregisterListener(mIUwbServiceListener);
            } catch (RemoteException e) {
                // pass
            }
            mUwbDevices = null;
            mUwbDevice = null;
            mIUwbService = null;
            for(final SafeUwbManagerListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onUWBManagerDestroyed(UwbManager.this);
                    }
                });
            }
        }
    };

    private IUwbServiceListener mIUwbServiceListener = new IUwbServiceListener.Stub() {

        @Override
        public void onIUwbEnableStateChange(final int prevState, final int newState) throws RemoteException {
            for(final SafeUwbManagerListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onUwbEnableStateChanged(prevState, newState);
                    }
                });
            }
        }

        @Override
        public void onIUwbRadioStateChange(final int prevState, final int newState) throws RemoteException {
            for(final SafeUwbManagerListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onUwbRadioStateChanged(prevState, newState);
                    }
                });
            }
        }

        @Override
        public void onIUwbDeviceBondingRequest(int shortAddress) throws RemoteException {

        }

        @Override
        public void onIUwbDeviceBondingStateChange(int shortAddress, int prevState, int newState, int errno) throws RemoteException {

        }

        @Override
        public void onIUwbDeviceBondingCanceled(int shortAddress) throws RemoteException {

        }

        @Override
        public void onIUwbDeviceAttachRequest(int shortAddress) throws RemoteException {
            if(getUwbDeviceInternal(shortAddress).isBonded()) {
                mIUwbService.acceptAttachRequest(shortAddress);
            } else {
                mIUwbService.declineAttachRequest(shortAddress);
            }
        }

        @Override
        public void onIUwbDeviceAttached(int shortAddress) throws RemoteException {
            final UwbDevice d = getUwbDeviceInternal(shortAddress);
            for(final SafeUwbManagerListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onUwbDeviceAttached(d);
                    }
                });
            }
        }

        @Override
        public void onIUwbDeviceDetachRequest(int shortAddress) throws RemoteException {

        }

        @Override
        public void onIUwbDeviceDetached(int shortAddress) throws RemoteException {
            final UwbDevice d = getUwbDeviceInternal(shortAddress);
            for(final SafeUwbManagerListener l : mListeners) {
                l.second.post(new Runnable() {
                    @Override
                    public void run() {
                        l.first.onUwbDeviceDetached(d);
                    }
                });
            }
        }
    };

    private void removeDevice(int shortAddress) {
        mUwbDevices.remove(shortAddress);
    }

    private UwbDevice getUwbDeviceInternal(int shortAddress) throws RemoteException {
        UwbDevice d = mUwbDevices.get(shortAddress);
        if(d == null && arrayContains(mIUwbService.getBondedDevices(), shortAddress)) {
            IUwbDevice i = mIUwbService.getDevice(shortAddress);
            d = new UwbDevice(i);
            mUwbDevices.put(shortAddress, d);
        }

        return d;
    }

    private ArrayList<UwbDevice> getUwbDevicesForShortAddresses(int[] shortAddresses) throws RemoteException {
        ArrayList<UwbDevice> devices = new ArrayList<UwbDevice>(shortAddresses.length);

        for(int sa : shortAddresses) {
            devices.add(getUwbDeviceInternal(sa));
        }

        return devices;
    }

    private static boolean arrayContains(int[] array, int value) {
        for(int v : array) {
            if(v == value) return true;
        }

        return false;
    }

}
