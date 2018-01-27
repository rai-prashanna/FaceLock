package com.example.facerecognition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Trainingset {

	 public static int[][] matmul(int[][] m, int[][] n){
		    int mRows = m.length;
		    int mCol = m[0].length;
		    int nRows = n.length;
		    int nCol = n[0].length;
		    int [][] ans=new int[mRows][nCol];
		    float temp;
		    for(int i=0;i<mRows;i++)
		    {
		    	for(int j=0;j<nCol;j++){
		    		
		    		temp=0;
		    			for(int k=0;k<nRows;k++){
		    				
		    		//temp+=(m[i][k]>>16 & 0xFF)*(n[k][j]>>16 & 0xFF)*(1.0/nRows);
		    temp+=((m[i][k]>>16 & 0xFF)*0.2125+(m[i][k]>>8 & 0xFF)*0.7154+(m[i][k]>>16 & 0xFF)*0.0721)*((n[k][j]>>16 & 0xFF)*0.2125+(n[k][j]>>8 & 0xFF)*0.7154+(n[k][j] & 0xFF)*0.0721)*(1.0/nRows);
		    			}
		    	ans[i][j]=(int)temp;
		    			//ans[i][j]=(ans[i][j]<<16|ans[i][j]<<8|ans[i][j]);
		    		}
		    	
		    }
		   return ans;
		}
	 
	 		public static int[][] matmulnorm(int[][] m, int[][] n){
		    int mRows = m.length;
		    int mCol = m[0].length;
		    int nRows = n.length;
		    int nCol = n[0].length;
		    int [][] ans=new int[mRows][nCol];
		    float temp;
		    for(int i=0;i<mRows;i++)
		    {
		    	for(int j=0;j<nCol;j++){
		    		
		    		temp=0;
		    			for(int k=0;k<nRows;k++){
		    				
		    		//temp+=(m[i][k]>>16 & 0xFF)*(n[k][j]>>16 & 0xFF)*(1.0/nRows);
		    temp+=((m[i][k]>>16 & 0xFF)*0.2125+(m[i][k]>>8 & 0xFF)*0.7154+(m[i][k] & 0xFF)*0.0721)*((n[k][j]>>16 & 0xFF)*0.2125+(n[k][j]>>8 & 0xFF)*0.7154+(n[k][j] & 0xFF)*0.0721);
		    			}
		    	ans[i][j]=(int)temp;
		    			//ans[i][j]=(ans[i][j]<<16|ans[i][j]<<8|ans[i][j]);
		    		}
		    	
		    }
		   return ans;
		}	 
	public static  int[] equilize(int[] image){
		int[] result=new int[image.length];
		int[] intr=new int[256],cdfr=new int[256],scaledr=new int[256];
		int[] intg=new int[256],cdfg=new int[256],scaledg=new int[256];
		int[] intb=new int[256],cdfb=new int[256],scaledb=new int[256];
		int i,maxr,minr,maxg,ming,maxb,minb;
		for(i=0;i<image.length;i++){
			intr[(image[i]>>16 & 0xff)]++;
			intg[(image[i]>>8 & 0xff)]++;
			intb[(image[i] & 0xff)]++;
		}
		cdfr[0]=intr[0];
		cdfg[0]=intg[0];
		cdfb[0]=intb[0];
		for(i=1;i<256;i++){
			cdfr[i]=cdfr[i-1]+intr[i];
			cdfg[i]=cdfg[i-1]+intg[i];
			cdfb[i]=cdfb[i-1]+intb[i];
		}
		for(minr=0;intr[minr]==0;minr++);
		for(maxr=255;intr[maxr]==0;maxr--);
		for(ming=0;intg[ming]==0;ming++);
		for(maxg=255;intg[maxg]==0;maxg--);
		for(minb=0;intb[minb]==0;minb++);
		for(maxb=255;intb[maxb]==0;maxb--);
		
		for(i=0;i<256;i++){
			scaledr[i]=(int)(((cdfr[i]-intr[minr])/((float)cdfr[maxr]-cdfr[minr]))*255);
			scaledg[i]=(int)(((cdfg[i]-intg[ming])/((float)cdfg[maxg]-cdfg[ming]))*255);
			scaledb[i]=(int)(((cdfb[i]-intb[minb])/((float)cdfb[maxb]-cdfb[minb]))*255);
		}
		
		for(i=0;i<image.length;i++){
			result[i]=(0xff000000|(scaledr[(image[i]>>16 & 0xff)]<<16)|(scaledg[(image[i]>>8 & 0xff)]<<8)|(scaledr[(image[i] & 0xff)]));
		}
		
	return result;	
	}
}
	

