package com.wl.tableball.game;

import com.wl.tableball.util.Constant;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.wl.tableball.util.Constant.*;
/**
 * ��Ϸ�����棬��������һ����ת���ǽ�����˳�����
 * @author taozhang
 *
 */
public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback
{
	TableBallActivity tableBallActivity;//��ȡactivity
	Paint paint;//��������
	//������
	public MainMenuView(TableBallActivity tableBallActivity)
	{
		super(tableBallActivity);
		this.tableBallActivity=tableBallActivity;
		this.getHolder().addCallback(this);//�������ڻص��ӿ�ʵ����
		paint=new Paint();
		paint.setAntiAlias(true);
	}
	
	/**
	 * �滭����
	 */
	public void draw(Canvas canvas)
	{
		super.draw(canvas); //���ø���
		canvas.translate(Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY);//ƽ��
		canvas.scale(Constant.screenScaleResult.ratio, Constant.screenScaleResult.ratio);//����
		
		//���Ʊ���
		canvas.drawBitmap(TP_ARRAY[0], xyoffset[0][0], xyoffset[0][1],paint);
		//��������
		canvas.drawBitmap(TP_ARRAY[29], xyoffset[29][0], xyoffset[29][1],paint);
		//play��ť
		canvas.drawBitmap(TP_ARRAY[1], xyoffset[1][0], xyoffset[1][1],paint);
		//��ʷ��¼��ť
		canvas.drawBitmap(TP_ARRAY[15], xyoffset[15][0], xyoffset[15][1],paint);
		//���ð�ť
		canvas.drawBitmap(TP_ARRAY[16], xyoffset[16][0], xyoffset[16][1],paint);
	}

	//���ü���
	public boolean onTouchEvent(MotionEvent e)
	{
		int action = e.getAction();
		float x=e.getX();
		float y=e.getY();		
		switch(action)		
		{
			case MotionEvent.ACTION_DOWN:
				//��ʼ��Ϸ  x>x && x<x+width*ratio
				if(x>xyoffsetEvent[1][0]&&x<xyoffsetEvent[1][0]+TP_ARRAY[1].getWidth()*Constant.screenScaleResult.ratio&&
				   y>xyoffsetEvent[1][1]&&y<xyoffsetEvent[1][1]+TP_ARRAY[1].getHeight()*Constant.screenScaleResult.ratio)
				{
					tableBallActivity.hd.sendEmptyMessage(3);//ȥģʽѡ�����
					
				}
				//��ʷ��¼
				else if(x>xyoffsetEvent[15][0]&&x<xyoffsetEvent[15][0]+TP_ARRAY[15].getWidth()*Constant.screenScaleResult.ratio&&
					y>xyoffsetEvent[15][1]&&y<xyoffsetEvent[15][1]+TP_ARRAY[15].getHeight()*Constant.screenScaleResult.ratio)
				{
					tableBallActivity.hd.sendEmptyMessage(5);//ȥ��ʷ��¼����
					
				}
				//���ð�ť
				else if(x>xyoffsetEvent[16][0]&&x<xyoffsetEvent[16][0]+TP_ARRAY[16].getWidth()*Constant.screenScaleResult.ratio&&
					y>xyoffsetEvent[16][1]&&y<xyoffsetEvent[16][1]+TP_ARRAY[16].getHeight()*Constant.screenScaleResult.ratio)
				{
					tableBallActivity.hd.sendEmptyMessage(4);//ȥ���ý���
					
				}
				
			break;
		}
		return true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
	
	/**
	 * ���ű������ֺͻ滭
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(Constant.bg_music_sound)//���������ť��
		{
			if(Constant.from_nextview)//����Ǵ���ת�������
			{
				YINYUE1=true;
				this.tableBallActivity.soundutil.stop_bg_sound();//ֹͣ���ű�������
				this.tableBallActivity.soundutil.play_bg_sound();//��ʼֹͣ���ű�������
			}
			
		}
		Constant.from_nextview=false;
		//�滭
		Canvas canvas=holder.lockCanvas();
		try
		{
			synchronized(holder)
			{
				draw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
