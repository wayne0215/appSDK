package com.example.appsdk;

import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppSDKController.init(getApplication());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		AppSDKController.ExitAppSDK();
		return super.onKeyDown(keyCode, event);
	}
}
