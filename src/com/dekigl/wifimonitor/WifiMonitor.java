package com.dekigl.wifimonitor;

import java.util.Timer;
import java.util.TimerTask;

import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;

public class WifiMonitor extends Service {
    public class WifiMonitorBinder extends Binder {
    	WifiMonitor getService() {
            return WifiMonitor.this;
        }
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		//Toast.makeText(getApplicationContext(), "onBind()", Toast.LENGTH_SHORT).show();
		return new WifiMonitorBinder();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "onCreate()", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//Toast.makeText(getApplicationContext(), "onStart()", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(getApplicationContext(), "onDestroy()", Toast.LENGTH_SHORT).show();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

    private Timer timer;
    private Handler handler;
    private TextView wifistate;
  
    // サービス側の監視スレッド開始
    public void start_wifiMonitoring(Handler parent_handler, TextView textview) {
    	handler = parent_handler;
    	wifistate = textview;
    	
    	MyTimerTask timertask = new MyTimerTask();
    	timer = new Timer(true);
		timer.schedule(timertask, 1*1000, 1*1000);
    }
    
    private class MyTimerTask extends TimerTask {
    	@Override
    	public void run() {
    		handler.post( new Runnable() {
    			public void run(){
    	    		WifiManager wifiManager =(WifiManager) getSystemService(WIFI_SERVICE);

    	    		// android.permission.ACCESS_WIFI_STATE
    	    		String state = "";
    	    		switch(wifiManager.getWifiState()) {
    	    		case WifiManager.WIFI_STATE_DISABLING:
    	    			state = "WIFI_STATE_DISABLING";
    	    			break;
    	    		case WifiManager.WIFI_STATE_DISABLED:
    	    			state = "WIFI_STATE_DISABLED";
    	    			break;
    	    		case WifiManager.WIFI_STATE_ENABLING:
    	    			state = "WIFI_STATE_ENABLING";
    	    			break;
    	    		case WifiManager.WIFI_STATE_ENABLED:
    	    			state = "WIFI_STATE_ENABLED";
    	    			break;
    	    		case WifiManager.WIFI_STATE_UNKNOWN:
    	    			state = "WIFI_STATE_UNKNOWN";
    	    			break;
    	    		}
    	    		String wifissid = wifiManager.getConnectionInfo().getSSID();
    	    		int wifidbm = wifiManager.getConnectionInfo().getRssi();
    	    		
    	    		String out = wifissid + ":" + state + ":" + Integer.toString(wifidbm);
	    			wifistate.setText(out);
    	    	}
    		});
    	}
    }
    
    /*
	public void start_wifiMonitoring() {
		//Toast.makeText(getApplicationContext(), "start_wifiMonitoring()", Toast.LENGTH_SHORT).show();
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			
			public void run() {
				// 10秒おきに実行されるので、WiFiの接続状態を見て未接続状態がwifitimeout秒続いたらWiFiを切る
				WifiManager wifiManager =(WifiManager) getSystemService(WIFI_SERVICE);

				// android.permission.ACCESS_WIFI_STATE
				int wifiState = wifiManager.getWifiState();
				switch(wifiState) {
				case WifiManager.WIFI_STATE_DISABLING:
					Toast.makeText(getApplicationContext(), "WIFI_STATE_DISABLING", Toast.LENGTH_SHORT).show();
				    break;
				case WifiManager.WIFI_STATE_DISABLED:
					Toast.makeText(getApplicationContext(), "WIFI_STATE_DISABLED", Toast.LENGTH_SHORT).show();
				    break;
				case WifiManager.WIFI_STATE_ENABLING:
					Toast.makeText(getApplicationContext(), "WIFI_STATE_ENABLING", Toast.LENGTH_SHORT).show();
				    break;
				case WifiManager.WIFI_STATE_ENABLED:
					Toast.makeText(getApplicationContext(), "WIFI_STATE_ENABLED", Toast.LENGTH_SHORT).show();
				    break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					Toast.makeText(getApplicationContext(), "WIFI_STATE_UNKNOWN", Toast.LENGTH_SHORT).show();
				    break;
				}
			}
			
		};
		timer.schedule(timerTask, 1*1000);
	}
	*/
    
}