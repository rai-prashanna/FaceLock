package org.tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Locker extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		startService(new Intent(this,Background.class));
		finish();
	}

}
