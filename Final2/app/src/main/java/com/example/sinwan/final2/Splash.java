package com.example.sinwan.final2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Splash extends Activity {
	GPSTracker gps;
	SharedPreferences preferences1 ;

	String lat,lon;
	String PREFS_NAME1 = "com.example.sinwan.mini2";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		preferences1 = getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE);
	gps = new GPSTracker(Splash.this);

		// check if GPS enabled
		if(gps.canGetLocation()){

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String cityName = addresses.get(0).getAddressLine(0);
			String stateName = addresses.get(0).getAddressLine(1);
			String countryName = addresses.get(0).getAddressLine(2);

			SharedPreferences.Editor editor = preferences1.edit();
			editor.putString("mylat", String.valueOf(latitude));
			editor.putString("mylon", String.valueOf(longitude));
			editor.putString("city", String.valueOf(cityName));
			editor.commit();
			// \n is for new line
			Toast.makeText(getApplicationContext(), "City="+cityName+" Your Location is - \nLat: " +preferences1.getString("mylat", "").toString()+ "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		}else{
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint = new Intent("com.example.sinwan.final2.MainActivity");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
