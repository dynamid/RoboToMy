package fr.insa_lyon.citi.robotomy.gui.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;
import fr.insa_lyon.citi.robotomy.gui.common.IntentHelper;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.uwb.UwbContext;
import fr.insa_lyon.citi.robotomy.uwb.UwbDevice;
import fr.insa_lyon.citi.robotomy.uwb.UwbManager;

public class MainActivity extends BaseActivity {
    private LinearLayout abstractView;
    private LinearLayout mainView;
    private TextView windowTitle;
    private UwbManager mUWBManager;

    private Button calibrateButton;
    private Button launchButton;

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
            }

        }

        @Override
        public void onUWBManagerDestroyed(UwbManager manager) {

        }

        @Override
        public void onUwbEnableStateChanged(int prevState, int newState) {
            if ((newState != Constants.ENABLE_STATE_ON) && (newState != Constants.ENABLE_STATE_TURNING_ON)) {
                Toast.makeText(MainActivity.this, "UWB is off", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUwbRadioStateChanged(int prevState, int newState) {
            if ((newState != Constants.RADIO_STATE_ON) && (newState != Constants.RADIO_STATE_TURNING_ON)) {
                Toast.makeText(MainActivity.this, "UWB is off", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUwbDeviceAttached(UwbDevice device) {
            UwbContext.mUwbAttachedDevices.put(UwbDevice.getDeviceIdentity(device),device);
            AppTools.info("Device attached" + UwbDevice.getDeviceIdentity(device));
        }

        @Override
        public void onUwbDeviceDetached(UwbDevice device) {
            UwbContext.mUwbAttachedDevices.remove(UwbDevice.getDeviceIdentity(device));
           AppTools.info("Device detached" + UwbDevice.getDeviceIdentity(device));
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, Constants.MAIN_CONST);
        AppTools.info("on create MainActivity");
        initGraphicalInterface();


        UwbContext.mUwbAttachedDevices = new HashMap<String, UwbDevice>();
        mUWBManager = new UwbManager();
        mUWBManager.registerUWBManagerListener(mUwbManagerListener);
        mUWBManager.create(this);
    }

    private void initGraphicalInterface() {
        // set layouts
        LayoutInflater mInflater = LayoutInflater.from(this);
        abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
        mainView = (LinearLayout) mInflater.inflate(R.layout.activity_main, null);
        abstractView.addView(mainView);

        calibrateButton = (Button) findViewById(R.id.calibrateButton);
        launchButton = (Button) findViewById(R.id.launchButton);

        calibrateButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                IntentHelper.openNewActivity(CalibrateDeviceList.class, null, false);
            }
        });
        launchButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                IntentHelper.openNewActivity(LaunchActivity.class, null, false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

