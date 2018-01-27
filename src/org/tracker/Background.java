package org.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.facerecognition.Camera;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Background extends Service {
	Timer timer;
	public static ArrayList<String> checkedme=new ArrayList<String>();
	public static String removeme=null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Starting..", Toast.LENGTH_SHORT).show();

		checkedme = new ArrayList<String>();

		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
      timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				// get the info from the currently running task

				List<ActivityManager.RunningTaskInfo> taskInfo = am
						.getRunningTasks(1);
				Log.d("topActivity", "CURRENT Activity ::"
						+ taskInfo.get(0).topActivity.getPackageName());

				
					if (checkedme.contains(taskInfo.get(0).topActivity.getPackageName())
							) {
						removeme=taskInfo.get(0).topActivity.getPackageName();
						Intent dialogIntent = new Intent(getBaseContext(),com.example.facerecognition.Camera.class);
						dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						getApplication().startActivity(dialogIntent);
						Log.i("hello", "success");
					}

				
				
			}
		}, 0, 1000);

		return super.onStartCommand(intent, flags, startId);

	}

}
