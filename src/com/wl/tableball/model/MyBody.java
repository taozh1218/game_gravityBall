package com.wl.tableball.model;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.wl.tableball.game.GameView;
/**
 * �����и�����ĸ��࣬Ϊ��ͷ���ֱ��ṩһ��������
 * @author taozhang
 *
 */
public abstract class MyBody {
	
	public Bitmap bitmap;
	/**
	 * ��ײ����
	 */
	public Body body;
	GameView gameview;
	
	float width;//Ŀ����
	float height;//Ŀ��߶�
	/**
	 * ��������x����
	 */
	float x;
	/**
	 * ��������y����
	 */
	float y;
	float angle;
	//������
	public MyBody(Body body, Bitmap bitmap,GameView gameview) 
	{
		this.body = body;
		this.bitmap = bitmap;
		this.gameview=gameview;
	}
	
	//��������������demo�еĹ�����һ��
	public MyBody(Body body, float width,float height,Bitmap bitmap) 
	{
		this.body = body;
		this.bitmap = bitmap;
		this.height=height;
		this.width=width;
	}
	
	//�滭����
	public  void drawself(Canvas canvas,Paint paint)
	{
		x=body.getPosition().x;
		y=body.getPosition().y;
		if(bitmap==null)
		{
			return;
		}
		canvas.save();
		Matrix m3=new Matrix();
		m3.setTranslate(x-bitmap.getWidth()/2, y-bitmap.getHeight()/2);//ƽ��
		
		canvas.drawBitmap(bitmap, m3, paint);
		canvas.restore();
	}
	
	/**
	 * ��Ͷ�������ײʱ�����ķ���
	 * 
	 * ֻ�ж���ִ������������򲻻�ִ������������˴���ֻ��һ��ͼ
	 */
	public void doAction()
	{
		
	}
}
