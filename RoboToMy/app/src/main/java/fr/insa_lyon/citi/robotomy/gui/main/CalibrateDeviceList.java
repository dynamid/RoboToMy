package fr.insa_lyon.citi.robotomy.gui.main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;
import fr.insa_lyon.citi.robotomy.gui.common.IntentHelper;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.uwb.UwbContext;
import fr.insa_lyon.citi.robotomy.uwb.UwbDevice;
import fr.insa_lyon.citi.robotomy.uwb.UwbManager;

public class CalibrateDeviceList  extends BaseActivity {
    private LinearLayout abstractView;
    private RelativeLayout mainView;
    private TextView windowTitle;
    private UwbManager mUWBManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, Constants.CALIBRATE_CONST);
        AppTools.info("on create MainActivity");
        initGraphicalInterface();
        mUWBManager = new UwbManager();
        mUWBManager.registerUWBManagerListener(mUwbManagerListener);
        mUWBManager.create(this);
    }

    private UwbManager.UwbManagerListener mUwbManagerListener = new UwbManager.UwbManagerListener() {

        @Override
        public void onUWBManagerCreated(UwbManager manager) {
            // Check Local adapter is turned on, otherwise, launch admin app to allow user to turn it on
            if ((manager.getEnableState() != Constants.ENABLE_STATE_ON) && (manager.getRadioState() != Constants.RADIO_STATE_ON)) {
                Intent enablerIntent = new Intent();
                enablerIntent.setClassName("com.bespoon.uwb", "com.bespoon.uwb.admin.EnablerActivity");
                try {
                    startActivity(enablerIntent);
                }
                catch (ActivityNotFoundException e) {
                    AppTools.error("Activity not found");
                }
            }
            for (UwbDevice device :manager.getAttachedDevices()) {
                if(!UwbContext.mUwbAttachedDevices.containsKey(UwbDevice.getDeviceIdentity(device))){
                    UwbContext.mUwbAttachedDevices.put(UwbDevice.getDeviceIdentity(device),device);
                }
                AppTools.info("Device attached" + UwbDevice.getDeviceIdentity(device));
                fillList();

            }

        }

        @Override
        public void onUWBManagerDestroyed(UwbManager manager) {

        }

        @Override
        public void onUwbEnableStateChanged(int prevState, int newState) {
            if ((newState != Constants.ENABLE_STATE_ON) && (newState != Constants.ENABLE_STATE_TURNING_ON)) {
                Toast.makeText(CalibrateDeviceList.this, "UWB is off", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUwbRadioStateChanged(int prevState, int newState) {
            if ((newState != Constants.RADIO_STATE_ON) && (newState != Constants.RADIO_STATE_TURNING_ON)) {
                Toast.makeText(CalibrateDeviceList.this, "UWB is off", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUwbDeviceAttached(UwbDevice device) {
            UwbContext.mUwbAttachedDevices.put(UwbDevice.getDeviceIdentity(device),device);
            AppTools.info("Device attached" + UwbDevice.getDeviceIdentity(device));
            fillList();

        }

        @Override
        public void onUwbDeviceDetached(UwbDevice device) {
            UwbContext.mUwbAttachedDevices.remove(UwbDevice.getDeviceIdentity(device));
            AppTools.info("Device detached" + UwbDevice.getDeviceIdentity(device));
            fillList();

        }
    };


    private void initGraphicalInterface() {
        // set layouts
        LayoutInflater mInflater = LayoutInflater.from(this);
        abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
        mainView = (RelativeLayout) mInflater.inflate(R.layout.calibrate_activity, null);
        abstractView.addView(mainView);
    }

    private void fillList(){

        final ArrayList<String> list = new ArrayList<String>();
        for (String key: UwbContext.mUwbAttachedDevices.keySet()) {
            list.add("Sensor " + key);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        ListView l = (ListView) findViewById(R.id.calibrationList);
        l.setAdapter(adapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                IntentHelper.openNewActivity(CalibrateDevice.class, list.get(position).replace("Sensor ",""), false);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
    @Override
    protected void onStop() {
        mUWBManager.unregisterUWBManagerListener(mUwbManagerListener);
        super.onStop();
    }
}
