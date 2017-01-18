package com.wl.tableball.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * ����ͼƬ�����ء����š���ת���и�ȣ�
 * @author taozhang
 *
 */
public class PicLoadUtil 
{
	/**
	 * ��assets�У�ͨ��IO�õ���ͼƬ��bitmap
	 * @param res Resources
	 * @param fname �ļ���
	 * @return
	 */
	public static Bitmap loadBM(Resources res,String fname)
    {
    	Bitmap result=null;    	
    	try
    	{
    		InputStream in=res.getAssets().open(fname);
			int ch=0;
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    while((ch=in.read())!=-1)
		    {
		      	baos.write(ch);
		    }      
		    byte[] buff=baos.toByteArray();
		    baos.close();
		    in.close();
		    result=BitmapFactory.decodeByteArray(buff, 0, buff.length);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
    	return result;
    }
	
	//����ͼƬ�ķ���
	public static Bitmap scaleToFitXYRatio(Bitmap bm,float xRatio,float yRatio)//����ͼƬ�ķ���
	{
	   	float width = bm.getWidth(); //ͼƬ���
	   	float height = bm.getHeight();//ͼƬ�߶�
	   	Bitmap bmResult=Bitmap.createScaledBitmap(bm, (int)(width*xRatio), (int)(height*yRatio),true);
	   	return bmResult;
	}
	
	//����ͼƬ
	public static Bitmap scaleBitmap(Bitmap bm,int width,int height)//����ͼƬ�ķ���
	{
	   	Bitmap bmResult=Bitmap.createScaledBitmap(bm, width, height,true);
	   	return bmResult;
	}
	
	
	//���������и�ͼƬ
	public static Bitmap[][] splitPic
   (
		   int cols,//�и������ 
		   int rows,//�и������    
		   Bitmap srcPic,//���и��ͼƬ  
		   int dstWitdh,//�и�������Ŀ����
		   int dstHeight//�и�������Ŀ��߶�  
   ) 
   {   
	   final float width=srcPic.getWidth();
	   final float height=srcPic.getHeight();
	   
	   final int tempWidth=(int)(width/cols);
	   final int tempHeight=(int)(height/rows);
	   
	   Bitmap[][] result=new Bitmap[rows][cols];
	   
	   for(int i=0;i<rows;i++)
	   {
		   for(int j=0;j<cols;j++)
		   {
			   Bitmap tempBm=Bitmap.createBitmap(srcPic, j*tempWidth, i*tempHeight,tempWidth, tempHeight);		
			   result[i][j]=scaleToFit(tempBm,dstWitdh,dstHeight);
		   }
	   }
	   
	   return result;
    }
	
	/**
	 * �ϳ�ͼƬ
	 * @param srcPic
	 * @param dstWitdh
	 * @param dstHeight
	 * @return
	 */
	public static Bitmap combineRec
	   (   
			   Bitmap srcPic,//���и��ͼƬ  
			   float dstWitdh,//Ŀ����
			   float dstHeight//Ŀ��߶�  
	   ) 
	   {   
		   final float width=srcPic.getWidth();//ԭʼ���
		   final float height=srcPic.getHeight();//ԭʼ�߶�
		   Bitmap result[]=new Bitmap[4];
		   
		   result[0]=Bitmap.createBitmap(srcPic, 0, 0,(int)dstWitdh-4, (int)dstHeight-7);		
		   result[1]=Bitmap.createBitmap(srcPic, (int) (width-7), 0,7, (int)dstHeight-7);		
		   result[2]=Bitmap.createBitmap(srcPic, 0, (int) (height-7),(int)dstWitdh-4, 7);		
		   result[3]=Bitmap.createBitmap(srcPic, (int)width-7, (int)height-7, 7, 7);
		   Bitmap bm=Bitmap.createBitmap((int)dstWitdh, (int)dstHeight,Config.ARGB_8888 );
		   
		   Canvas canvas= new Canvas(bm);
		   Paint paint;
		   paint=new Paint();
		   paint.setAntiAlias(true);
		   
		   canvas.drawBitmap(result[3], dstWitdh-7, dstHeight-7, paint);
		   canvas.drawBitmap(result[2], 0, dstHeight-7, paint);
		   canvas.drawBitmap(result[1], dstWitdh-7, 0, paint);
		   canvas.drawBitmap(result[0], 0, 0, paint);
		   
		   return bm;
	    }
	
	 //������תͼƬ�ķ���
	   public static Bitmap scaleToFit(Bitmap bm,int dstWidth,int dstHeight)//����ͼƬ�ķ���
	   {
	   	float width = bm.getWidth(); //ͼƬ���
	   	float height = bm.getHeight();//ͼƬ�߶�
	   	float wRatio=dstWidth/height;
	   	float hRatio=dstHeight/width;
	   	
	   	Matrix m1 = new Matrix(); 
	   	m1.postScale(wRatio, hRatio);
	   	Matrix m2= new Matrix();
	   	m2.setRotate(45, dstWidth/2, dstHeight/2);
	   	Matrix mz=new Matrix();
		mz.setConcat(m1, m2);
	   	
	   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, mz, true);//����λͼ        	
	   	return bmResult;
	   }
}
