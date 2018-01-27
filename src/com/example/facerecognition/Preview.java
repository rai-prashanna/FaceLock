package com.example.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.tracker.Background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "Preview";
	static int quality=50;
    SurfaceHolder mHolder;
    public Camera camera;
    Socket socket=null;
    Context context;
    int n;
    @SuppressWarnings("deprecation")
	Preview(Context cont) {
        super(cont);
        context=cont;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera = openFrontFacingCamera();
        camera.setDisplayOrientation(90);
        final Camera.Parameters params = camera.getParameters();
        params.setPreviewSize(640,480);
        camera.setParameters(params);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
			camera.setPreviewDisplay(holder);
			camera.setPreviewCallback(new PreviewCallback() {
			public void onPreviewFrame(byte[] data, Camera arg1) {
					
					 Camera.Parameters parameters = arg1.getParameters();

		                int format = parameters.getPreviewFormat();

		                //YUV formats require more conversion
		                if (format == ImageFormat.NV21 || format == ImageFormat.YUY2 || format == ImageFormat.NV16) {
		                    int w = parameters.getPreviewSize().width;
		                    int h = parameters.getPreviewSize().height;

		                    // Get the YuV image
		                    YuvImage yuv_image = new YuvImage(data, format, w, h, null);
		                    // Convert YuV to Jpeg
		                    Rect rect = new Rect(0, 0, w, h);
		                    ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
		                    yuv_image.compressToJpeg(rect, quality, output_stream);
		               if(Authenticate.next==true){
		            	   	Authenticate.next=false;
		                    Bitmap newbmp=BitmapFactory.decodeByteArray(output_stream.toByteArray(), 0, output_stream.toByteArray().length);
		                    Matrix mtx = new Matrix();
		        		    mtx.preRotate(90);
		        		    newbmp = Bitmap.createBitmap(newbmp, 0, 0, newbmp.getWidth(), newbmp.getHeight(), mtx, false);
		                    if(newbmp==null)
		                    	Log.i("faces", "null image");
		                    int[] newimg=new int[10000];
		                    newbmp=newbmp.copy(Bitmap.Config.RGB_565, true);
		                    
		                    //Authenticate.imv.setImageBitmap(newbmp);
		                    
		                    FaceDetector fd=new FaceDetector(newbmp.getWidth(), newbmp.getHeight(), 2);
		        			FaceDetector.Face[] f=new FaceDetector.Face[2];
		        			int a=fd.findFaces(newbmp, f);
		        			
		        			Log.i("faces", "f:"+a);
		        			if(a>0){
		        				
		        			PointF p=new PointF();
		        			f[0].getMidPoint(p);
		        			float ed=f[0].eyesDistance();
		                    float left = p.x - (float)(1.4 * ed);
		                    float top = p.y - (float)(1.8 * ed);
		                    if((int) (3.6 * ed)>100 && (int) (3.6 * ed)<480 && (int) (2.8 * ed)>100 && (int) (3.6 * ed)<640){
		                    	if(top<0)
		                    		top=0;
		                    	if(left<0)
		                    		left=0;
		        			Bitmap bmFace = Bitmap.createBitmap(newbmp, (int) left, (int) top, (int) (2.8 * ed), (int) (3.6 * ed));
		        			Log.i("faces", "f:"+bmFace.getHeight()+", "+bmFace.getWidth());
		        			bmFace = ImageManu.getResizedBitmap(bmFace, 100, 100);
		        			bmFace=ImageManu.toGrayscale(bmFace);
		        			Authenticate.imv.setImageBitmap(bmFace);
		        			bmFace.getPixels(newimg, 0, 100, 0, 0, 100, 100);
		        			Authenticate.recognizer=new FaceRecognize();
		        			Authenticate.recognizer.add(0, Authenticate.im[0]);
		        			Authenticate.recognizer.add(1, Authenticate.im[1]);
		        			Authenticate.recognizer.add(2, newimg);
		        			float wt[]=Authenticate.recognizer.prepare();
		        			if(wt[0]>95 & wt[1]>95)
		        			{
		        				Toast.makeText(context, "Provide access", Toast.LENGTH_LONG).show();
		        				Background.checkedme.remove(Background.removeme);
		        				Log.i("faces", "Provide access");
		        			}
		    				Toast.makeText(context, wt[0]+"."+wt[1], Toast.LENGTH_LONG).show();
		    				Log.i("faces", "Weght:"+wt[0]+"."+wt[1]);
		                    }
		        			}
		                    
		                    
		                    
		                    Authenticate.next=true;
		                }
		                    
		                }
						Preview.this.invalidate();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	 this.getHolder().removeCallback(this);
         camera.stopPreview();
         camera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        camera.startPreview();
    }

    private Camera openFrontFacingCamera() {
			Camera cam = null;
				try {
					cam = Camera.open();//camIdx);
				} catch (RuntimeException e) {
					Log.e(TAG,
							"Camera failed to open: " + e.getLocalizedMessage());
				}
			return cam;
	}
    
    
}