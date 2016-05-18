package home.example.com.wifi_hotspot;

/**
 * Created by ayush on 2/5/16.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

    TextView textConnected, textIp, textSsid, textBssid, textMac,   textRssi,textview;
    List<NameValuePair> nameValuePairList;

    String ssid,bssid,imei_number,str1;
    ConnectivityManager myConnManager;
    WifiManager myWifiManager;
    NetworkInfo myNetworkInfo;
    WifiInfo myWifiInfo;
    int flag=0;

    public final static int POST = 2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textConnected = (TextView)findViewById(R.id.Connected);
        textIp = (TextView)findViewById(R.id.Ip);
        textSsid = (TextView)findViewById(R.id.Ssid);
        textBssid = (TextView)findViewById(R.id.Bssid);
        textview =(TextView)findViewById(R.id.disconnected_info);

        //textMac = (TextView)findViewById(R.id.Mac);
      //  textSpeed = (TextView)findViewById(R.id.Speed);
    //    textRssi = (TextView)findViewById(R.id.Rssi);

      //  DisplayWifiState();



        this.registerReceiver(this.myWifiReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private BroadcastReceiver myWifiReceiver
            = new BroadcastReceiver(){

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            NetworkInfo networkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                 DisplayWifiState();
                new SendtoServer().execute();

                if (!myWifiManager.isWifiEnabled()) {

                    Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                         Toast.LENGTH_LONG).show();

                    myWifiManager.setWifiEnabled(true);
                }


                Toast.makeText(getApplicationContext(),"Intent Service",Toast.LENGTH_LONG).show();
                Intent i= new Intent(arg0, MyWifiService.class);
                // wifiManager.setWifiEnabled(true);
                arg0.startService(i);
            }

        }
    };

    private void DisplayWifiState() {

       myConnManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
     myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
       myWifiInfo = myWifiManager.getConnectionInfo();



        bssid = myWifiInfo.getBSSID();
        ssid = myWifiInfo.getSSID();

        TelephonyManager tm = (TelephonyManager) MyActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        imei_number = tm.getDeviceId();


        //   textMac.setText(myWifiInfo.getMacAddress());


        if (myNetworkInfo.isConnected() && bssid.equalsIgnoreCase("6c:19:8f:ea:29:99")) {


            flag = 1;
            str1 = Integer.toString(flag);

            Toast.makeText(MyActivity.this, "you are in ASUS range", Toast.LENGTH_SHORT).show();

            int myIp = myWifiInfo.getIpAddress();

            textConnected.setText("--- CONNECTED ---");

            int intMyIp3 = myIp / 0x1000000;
            int intMyIp3mod = myIp % 0x1000000;

            int intMyIp2 = intMyIp3mod / 0x10000;
            int intMyIp2mod = intMyIp3mod % 0x10000;

            int intMyIp1 = intMyIp2mod / 0x100;
            int intMyIp0 = intMyIp2mod % 0x100;

            textIp.setText(String.valueOf(intMyIp0)
                            + "." + String.valueOf(intMyIp1)
                            + "." + String.valueOf(intMyIp2)
                            + "." + String.valueOf(intMyIp3)
            );

            textSsid.setText(myWifiInfo.getSSID());
            textBssid.setText(myWifiInfo.getBSSID());
            //  textSpeed.setText(String.valueOf(myWifiInfo.getLinkSpeed()) + " " + WifiInfo.LINK_SPEED_UNITS);
            // textRssi.setText(String.valueOf(myWifiInfo.getRssi()));
            textview.setText("you Are Conncted with" + ssid);
        //    new SendtoServer().execute();

        } else {

            textConnected.setText("--- DIS-CONNECTED! ---");
            Toast.makeText(MyActivity.this, "you are out of Asus range", Toast.LENGTH_SHORT).show();
            textIp.setText("---");
            textSsid.setText("---");
            textBssid.setText("---");
            //textSpeed.setText("---");
            //  textRssi.setText("---");
            textview.setText("You Are Not Connected With ASUS");

            flag = 0;
            str1 = Integer.toString(flag);
            Toast.makeText(MyActivity.this, " Attendence " + str1, Toast.LENGTH_SHORT).show();
            //  new SendtoServer().execute();

        }
    }


//    public class ConnectivityReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(ConnectivityReceiver.class.getSimpleName(), "action: "
//                    + intent.getAction());
//
//
//            try {
//
//                if (myWifiManager.isWifiEnabled() == false) {
//                    // If wifi disabled then enable it
//                    Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
//                            Toast.LENGTH_LONG).show();
//
//                    myWifiManager.setWifiEnabled(true);
//                }
//
//            }
//            catch(Exception e)
//
//            {
//                e.printStackTrace();
//            }
//
//
//
//
//        }
//    }



    class SendtoServer extends AsyncTask<String, Void, String> {


        ProgressDialog dlg;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dlg = new ProgressDialog(MyActivity.this);
            dlg.setMessage("Saving...");
            dlg.show();
        }


        @Override
        protected String doInBackground(String... params) {


            nameValuePairList = new ArrayList<NameValuePair>(POST);
            nameValuePairList.add(new BasicNameValuePair("wifiname",ssid));
            nameValuePairList.add(new BasicNameValuePair("imei", imei_number));
            nameValuePairList.add(new BasicNameValuePair("attendance", str1));
           // nameValuePairList.add(new BasicNameValuePair("F8", timeStamp));
            // nameValuePairList.add(new BasicNameValuePair("F8", str1));







            String result = new ServiceHandler().makeServiceCall("http://www.trinityapplab.in/slump/wifihotspot.php",2, nameValuePairList);
            Log.d("result =", result);


            return result;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dlg.dismiss();
        }


    }

    public void broadcastIntent(View view){
        Intent intent = new Intent();
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        sendBroadcast(intent);
    }


}