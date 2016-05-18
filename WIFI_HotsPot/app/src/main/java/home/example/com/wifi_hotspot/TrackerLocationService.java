package home.example.com.wifi_hotspot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import utils.Constants;

public class TrackerLocationService extends Service
{
	WifiManager myWifiManager;
	String TAG = "TrackerLocationService.java";
	int hunb=10;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		Log.i("TrackerLocationService", "From boot complete Started");
		myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	//	Toast.makeText(TrackerLocationService.this, "TrackerLocationService From boot complete Started", Toast.LENGTH_LONG).show();
		addLocationListener();


		if (hunb==10)
		{
			if(!myWifiManager.isWifiEnabled()) {

				Toast.makeText(getApplicationContext(), "wifi is off... please wait turning it on automatically ",
						Toast.LENGTH_LONG).show();

				myWifiManager.setWifiEnabled(true);
			}
		}


	}

	private void addLocationListener() {
		Thread triggerService = new Thread(new Runnable() {
			public void run() {
				try {
					Looper.prepare();
					//myWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


					if (myWifiManager.isWifiEnabled()==false) {

						Toast.makeText(getApplicationContext(), "wifi is off... please wait turning it on automatically ",
								Toast.LENGTH_LONG).show();

						myWifiManager.setWifiEnabled(true);
					}
					Looper.loop();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, "LocationThread");
		triggerService.start();
	}






}
