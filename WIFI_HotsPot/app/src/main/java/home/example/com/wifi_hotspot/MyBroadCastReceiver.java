package home.example.com.wifi_hotspot;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyBroadCastReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();





        Intent i= new Intent(context, MyWifiService.class);
       // wifiManager.setWifiEnabled(true);
        context.startService(i);

    }
}