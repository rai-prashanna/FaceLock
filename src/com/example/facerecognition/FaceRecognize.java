package com.example.facerecognition;

import java.util.ArrayList;

import android.app.LauncherActivity.ListItem;
import android.widget.Toast;

public class FaceRecognize {
	
	ArrayList<int[]> images=new ArrayList<int[]>();
	int Average[]=new int[10000],red,green,blue;
	
	public boolean add(int id, int[] image){
		images.add(image);
		return false;
	}
	
	
	public float[] prepare(){
		for(int[] img:images){
			img=Trainingset.equilize(img);
		}
		
		for(int j=0;j<10000;j++){
			for(int[] img:images)
			{
			 red  +=  ((img[j] >> 16) & 0xFF)/2;
			 green += ((img[j] >> 8) & 0xFF)/2;
			 blue += (img[j] & 0xFF)/2;
			 
			}
			Average[j]=((red << 16) | ((green << 8) | blue));
			red=green=blue=0;
		}
			
		
		int[][] normal = new int[images.size()][10000];
		int[][] normalg = new int[10000][images.size()];
		for(int[] img:images){
			for(int j=0;j<10000;j++){
				red  =  ((img[j] >> 16) & 0xFF)-((Average[j] >> 16) & 0xFF);
				green = ((img[j] >> 8) & 0xFF)-((Average[j] >> 8) & 0xFF);
				blue = (img[j] & 0xFF)-((Average[j]) & 0xFF);
				
				
				normal[images.indexOf(img)][j]=((red << 16) | ((green << 8) | blue));
				normalg[j][images.indexOf(img)]=(int)(red*0.2125+green*0.7154+blue*0.0721);
			}
			
		}
		
		
		int [][] amat=new int[images.size()][10000];
		int [][] atrans=new int[10000][images.size()];

		for(int i=0;i<images.size();i++)
		{
			for(int j=0;j<10000;j++)
			{
				amat[i][j]=normal[i][j];
				atrans[j][i]=normal[i][j];
			}
		}
		
		int[][] covar=Trainingset.matmul(amat,atrans);
		
		int[][] vector=Trainingset.matmulnorm(atrans,covar);

		int[][] vtd=new int[images.size()][10000];
		for(int i=0;i<10000;i++){
			vtd[0][i]=vector[i][0];
			vtd[1][i]=vector[i][1];
			vtd[2][i]=vector[i][2];
		}
		
		
		for(int i=0;i<vtd.length;i++)
		{
			vtd[i]=Trainingset.equilize(vtd[i]);
		}
		
		int [][] weight=Trainingset.matmulnorm(vtd, normalg);
		//Toast.makeText(getApplicationContext(), weight[0][0]+","+weight[0][1]+","+weight[1][0]+","+weight[1][1]+"\n::"+weight[2][0]+","+weight[2][1], Toast.LENGTH_LONG).show();
		
			float match1=((1-Math.abs((weight[2][0]-weight[0][0])/(float)weight[2][0]))+(1-Math.abs((weight[2][1]-weight[0][1])/(float)weight[2][1])))*50;
			float match2=((1-Math.abs((weight[2][0]-weight[1][0])/(float)weight[2][0]))+(1-Math.abs((weight[2][1]-weight[1][1])/(float)weight[2][1])))*50;
			
		//Toast.makeText(getApplicationContext(), match1+", "+match2, Toast.LENGTH_SHORT).show();

		
		return new float[]{match1,match2};
	}
	
	
	
	
	private int recognize(int[] image){
		
		return 0;
	}

}