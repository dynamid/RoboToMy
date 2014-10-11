package fr.insa_lyon.citi.robotomy.gui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;
import fr.insa_lyon.citi.robotomy.gui.common.IntentHelper;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.uwb.UwbContext;
import fr.insa_lyon.citi.robotomy.uwb.UwbDevice;

/**
 * Created by Arturo on 11/10/2014.
 */
public class CalibrateDevice extends BaseActivity{

    private LinearLayout abstractView;
    private LinearLayout mainView;
    private TextView windowTitle;
    private TextView counter;
    private Switch leg_hand;
    private Button b;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, Constants.CALIBRATE_DETAILS_CONST);
        AppTools.info("on create MainActivity");
        initGraphicalInterface();
    }

    UwbDevice d;

    float distance_to_save = 0;

    private void initGraphicalInterface() {
        // set layouts
        LayoutInflater mInflater = LayoutInflater.from(this);
        abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
        mainView = (LinearLayout) mInflater.inflate(R.layout.calibrate_details_activity, null);
        abstractView.addView(mainView);
        String key = IntentHelper.getActiveIntentParam(String.class);

        leg_hand = (Switch) findViewById(R.id.switch1);
        counter = (TextView) findViewById(R.id.textView5);
        d = UwbContext.mUwbAttachedDevices.get(key);
        d.registerListener(mIUwbDeviceListener);
        AppTools.info("Id: "+ key);
        AppTools.info("Device:"+d.getName());
        if(d.getLeg_hand()==0){
            leg_hand.setChecked(true);
        }
        else{
            leg_hand.setChecked(false);
        }
        leg_hand.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(leg_hand.isChecked())
                    d.setLeg_hand(0);
                else{
                    d.setLeg_hand(1);
                }
            }
        });
        b = ( Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                d.setZero_value(distance_to_save);
                finish();
            }
        });
    }



    private UwbDevice.UwbDeviceListener mIUwbDeviceListener = new UwbDevice.UwbDeviceListener() {
        public void onDistanceChanged(UwbDevice device, int accuracy, long timestamp, float distance){
            counter.setText("" + (int)(distance * 100) + " cm");
            distance_to_save = distance;
        }

        public void onLocationChanged(UwbDevice device, int accuracy, long timestamp, float x, float y, float z){

        }

        public void onLost(UwbDevice device){

        }

        public void onRecovered(UwbDevice device){

        }
        public void onLocationProfileChanged(UwbDevice device, int prevProfile, int newProfile, int status){

        }

        public void onBatteryLevelChanged(UwbDevice device, int prevLevel, int newLevel){

        }

        public void onBatteryStateChanged(UwbDevice device, boolean isLow){

        }
    };
    @Override
    protected void onStop() {
        d.unregisterListener(mIUwbDeviceListener);
        super.onStop();
    }
}
