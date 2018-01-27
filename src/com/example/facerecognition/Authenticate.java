package com.example.facerecognition;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Authenticate extends Activity {
	Preview preview;
	SharedPreferences file;
	public static FaceRecognize recognizer;
	public static int[][] im=new int[2][10000];
	public static boolean next=true;
	public static ImageView imv;
	static Context c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognize);
		c=getApplicationContext();
		imv=(ImageView)findViewById(R.id.preview);
		preview=new Preview(getApplicationContext());
		((FrameLayout)findViewById(R.id.camera_preview)).addView(preview);
		file=getSharedPreferences("data.my", 0);
		for(int i=0;i<10000;i++){
			im[0][i]=file.getInt("img0:"+i, 0);
			im[1][i]=file.getInt("img1:"+i, 0);
		}
	}


}
