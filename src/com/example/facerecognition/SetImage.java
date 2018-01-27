package com.example.facerecognition;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Bitmap.Config;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SetImage extends Activity {
	final static int cd=0;
	ImageView imv;
	ImageView imv2;
	TextView percentmatch;
	Bitmap bmp;
	Button cap,rec;
	int[][] vtd;
	int newimageg[][];
	Trainingset trainObject= new Trainingset();
	ImageManu imageObject = new ImageManu();
	FaceRecognize faceObject=new FaceRecognize();
	SharedPreferences file;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setimage);
		cap=(Button) findViewById(R.id.capture);
		file=getSharedPreferences("data.my", 0);
		
		
		cap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent inte=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				//Intent inte=new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
				startActivityForResult(inte,cd);
				//startActivityForResult(Intent.createChooser(inte, "SEL P"),0);
				
			}
		});
		
	}

	
	int countimg=0;
	Bitmap faces[]=new Bitmap[10];
	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			
			Bundle b=data.getExtras();
			Bitmap tbmp=(Bitmap) b.get("data");
			
			
			bmp=tbmp.copy(Bitmap.Config.RGB_565, true);
			
			FaceDetector fd=new FaceDetector(bmp.getWidth(), bmp.getHeight(), 2);
			FaceDetector.Face[] f=new FaceDetector.Face[2];
			int a=fd.findFaces(bmp, f);
			if(a>0){
			PointF p=new PointF();
			f[0].getMidPoint(p);
			cap.setText(a+"::Mid:"+p.x+", "+p.y+"Eye:"+f[0].eyesDistance());
			imv.setImageBitmap(bmp);
			float ed=f[0].eyesDistance();
            float left = p.x - (float)(1.4 * ed);
            float top = p.y - (float)(1.8 * ed);

			Bitmap bmFace = Bitmap.createBitmap(bmp, (int) left, (int) top, (int) (2.8 * ed), (int) (3.6 * ed));          
			bmFace = imageObject.getResizedBitmap(bmFace, 100, 100);
			bmFace=imageObject.toGrayscale(bmFace);
			faces[countimg]=bmFace;
			countimg++;
						
			}
			
			if(countimg<2){
			Intent inte=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(inte,cd);
				}
			
			else {
				
				int mWidth = faces[0].getWidth();
				int mHeight = faces[0].getHeight();

				int[][] mIntArray = new int[10][mWidth * mHeight];

				// Copy pixel data from the Bitmap into the 'intArray' array
				for(int i=0;i<countimg;i++)
					faces[i].getPixels(mIntArray[i], 0, mWidth, 0, 0, mWidth, mHeight);
				
				for(int i=0;i<countimg;i++)
					faceObject.add(i,mIntArray[i]);
				
				byte databas[][]=new byte[countimg][40000];
				SharedPreferences.Editor filedit=file.edit();
				
				for(int i=0;i<10000;i++){
					filedit.putInt("img0:"+i, mIntArray[0][i]);
					filedit.putInt("img1:"+i, mIntArray[1][i]);
				}
				filedit.commit();
						}
		}
	}
	}


