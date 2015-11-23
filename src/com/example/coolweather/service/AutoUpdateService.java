package com.example.coolweather.service;

import com.example.coolweather.receiver.AutoUpdateReceiver;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO �Զ����ɵķ������
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO �Զ����ɵķ������
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour = 8*60*60*1000;
		long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
		Intent i = new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void updateWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO �Զ����ɵķ������
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO �Զ����ɵķ������
				e.printStackTrace();
			}
		});
	}

}
