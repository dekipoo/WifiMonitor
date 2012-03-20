package com.dekigl.wifimonitor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

public class WifiMonitorActivity extends Activity {
	
    private WifiMonitor wifiMonitor;
    private ServiceConnection wifiMonitorConnection = new ServiceConnection() {
 	   public void onServiceConnected(ComponentName className, IBinder service) {
 		   wifiMonitor = ((WifiMonitor.WifiMonitorBinder)service).getService();

 			// サービス起動
 		   TextView mTextView = (TextView) findViewById(R.id.wifistate);
 		   Handler mHandler = new Handler();
 			wifiMonitor.start_wifiMonitoring(mHandler, mTextView);
 	   }
 		
 		public void onServiceDisconnected(ComponentName className) {
 			wifiMonitor = null;
 		}
 	};
     
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

       // WiFiの状態を監視するサービスを起動する
		// サービスを開始
		Intent intent = new Intent(this, WifiMonitor.class);
		startService(intent);
		
		// サービスにバインド
		bindService(intent, wifiMonitorConnection, Context.BIND_AUTO_CREATE);
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(wifiMonitorConnection); // バインド解除
		wifiMonitor.stopSelf(); // サービスは必要ないので終了させる。
	}

}