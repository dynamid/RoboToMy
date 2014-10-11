package fr.insa_lyon.citi.robotomy.gui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.uwb.UwbContext;
import fr.insa_lyon.citi.robotomy.uwb.UwbDevice;

/**
 * Created by Arturo on 11/10/2014.
 */
public class LaunchActivity extends BaseActivity {

    private LinearLayout abstractView;
    private LinearLayout mainView;
    private TextView windowTitle;
    private Button connect;
    private EditText ipHolder;
    private WebView image;
    private String ip = "";
    WebViewClient c;

    private Socket socket;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, Constants.LAUNCH_CONST);
        AppTools.info("on create MainActivity");
        initGraphicalInterface();
    }

    private void initGraphicalInterface() {
        // set layouts
        LayoutInflater mInflater = LayoutInflater.from(this);
        abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
        mainView = (LinearLayout) mInflater.inflate(R.layout.launch_main, null);
        abstractView.addView(mainView);
        connect = (Button) findViewById(R.id.connect);
        image = (WebView) findViewById(R.id.image);
        ipHolder = (EditText) findViewById(R.id.ip);
        c = new WebViewClient();
        image.setWebViewClient(c);

        connect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(connect.getText()=="Disconnect"){
                    connect.setText("Connect");
                    ip = "";
                    c = new WebViewClient();
                    image.setWebViewClient(c);
                    image.loadData("<html></html","text/html","utf-8");
                    for(String key: UwbContext.mUwbAttachedDevices.keySet()){
                        UwbDevice d = UwbContext.mUwbAttachedDevices.get(key);
                        if (d.getLeg_hand()==0){
                            d.unregisterListener(direction);
                        }
                        else
                            d.unregisterListener(rotation);
                    }
                }
                else{
                    connect.setText("Disconnect");
                    ip = ipHolder.getText().toString();

                    c = new WebViewClient();
                    image.setWebViewClient(c);


                    String ip_tmp = "http://"+ip+":8090/";
                    //image.loadUrl(ip_tmp);
                    image.loadData("<body><head><title>Robot</title></head><body><img src=\"\"+ ip_tmp+\"\"></body></html>","text/html","utf-8");
                    AppTools.info(ip_tmp);

                    for(String key: UwbContext.mUwbAttachedDevices.keySet()){
                        UwbDevice d = UwbContext.mUwbAttachedDevices.get(key);
                        if (d.getLeg_hand()==0){
                            d.registerListener(direction);
                        }
                        else
                            d.registerListener(rotation);
                    }


                }
            }
        });

    }

    private float direction_v = 0;
    private float rotation_v =0;

    private UwbDevice.UwbDeviceListener rotation = new UwbDevice.UwbDeviceListener() {
        public void onDistanceChanged(UwbDevice device, int accuracy, long timestamp, float distance){
            if(distance*100 - device.getZero_value()*100 > 5)
                rotation_v = 1;
            else if(distance*100 - device.getZero_value()*100 < -5)
                rotation_v = -1;
            else {
                direction_v = 0;
                return;
            }
           AppTools.info("Rotatation : "+ (distance*100 - device.getZero_value()*100));
            sendCommand(direction_v,rotation_v);

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



    private UwbDevice.UwbDeviceListener direction = new UwbDevice.UwbDeviceListener() {
        public void onDistanceChanged(UwbDevice device, int accuracy, long timestamp, float distance){
            if(distance*100 - device.getZero_value()*100 > 10)
                direction_v = 1;
            else if(distance*100 - device.getZero_value()*100 < -10)
                direction_v = -1;
            else{
                direction_v = 0;
                return;
            }
            AppTools.info("Direction : "+ (distance *100 - device.getZero_value()*100));
            sendCommand(direction_v,rotation_v);
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

    private void sendCommand(float dir_x, float dir_z){
            String msg = ((int)dir_x)+"|10|"+(int)(dir_z)+"|5";
        MyClientTask myClientTask = new MyClientTask(
                ip,8081,msg);
        myClientTask.execute();

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String msg;

        MyClientTask(String addr, int port, String msg2) {
            dstAddress = addr;
            dstPort = port;
            msg = msg2;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                OutputStream out = socket.getOutputStream();
                out.write(msg.getBytes());


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
 }
