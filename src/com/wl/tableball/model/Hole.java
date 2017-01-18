package com.wl.tableball.model;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.wl.tableball.game.GameView;
import com.wl.tableball.util.Constant;
/**
 * ��ʵ����
 * @author taozhang
 *
 */
public class Hole extends Ball{
	
	//flag�Ǳ�־λ����־�Ƿ������ı���
	boolean flag=false;
	
	/**
	 * ��ͼ
	 */
	Bitmap bitmapother=Constant.TP_ARRAY[5];//��
	
	Hole(Body body,Bitmap bitmap,GameView gameview)
	{
		super(body,bitmap,gameview);
	}

	/**
	 * С���䶴����תС��
	 */
	public  void drawself(Canvas canvas,Paint paint)
	{
		super.drawself(canvas, paint);
		if(flag)
		{
			canvas.save();
			//pic ��ת
			angle=body.getAngle();//��ת�Ƕ�
			canvas.rotate((float)angle+Constant.ROTEANGLE+Constant.ROTEANGLEOFFSET, x, y);
			Matrix m3=new Matrix();
			m3.setTranslate(x-bitmapother.getWidth()/2, y-bitmapother.getHeight()/2);//ƽ��
			canvas.drawBitmap(bitmapother, m3, paint);//��ͼ
			canvas.restore();
		}
	}
	
	/**
	 * ��Ͷ�������ײʱ�����ķ���
	 * 
	 * ֻ�ж���ִ������������򲻻�ִ������������˴���ֻ��һ��ͼ
	 */
	public void doAction()
	{
		BitmapChangeThread bitmapchanagethread=new BitmapChangeThread(this);//ˢ��С���䶴�������߳�
		bitmapchanagethread.start();
		flag=true;
		
	}
	
	/**
	 * ��ͼ�߳�
	 * @author taozhang
	 *
	 */
	private class BitmapChangeThread extends Thread
	{
		Hole ball;
		int i=0;
		BitmapChangeThread(Hole ball)
		{
			this.ball=ball;
		}
		@Override
		public void run() {
			while(i<15)
			{
				//TODO ���������޸���i��λ��, ���֤����ԭ����I��λ������ȷ��
				ball.bitmapother=Constant.BALL_IN_HOLE[i][0];
				i++;
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ball.bitmapother=null;
			flag=false;
		}
	}
}

