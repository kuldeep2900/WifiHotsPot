package home.example.com.wifi_hotspot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ayush on 3/5/16.
 */
public class BroadcastReceiverforService extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals(utils.Constants.FREQ_RECEIVER_ACTION)) {
            Toast.makeText(context, "FrequencyReceiver", Toast.LENGTH_LONG).show();
            Log.d("", "FrequencyReceiver 1 ");
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            Intent i= new Intent(context, MyWifiService.class);
            // wifiManager.setWifiEnabled(true);
            context.startService(i);



        }
    }
}
