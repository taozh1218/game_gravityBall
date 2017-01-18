package com.wl.tableball.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.wl.tableball.util.Constant;
import com.wl.tableball.util.DBUtil;

import static com.wl.tableball.util.Constant.*;
/**
 * ѡ�ؽ���
 * 
 * ����ͼ������ʾ������Ϣ��������������
 * @author taozhang
 *
 */
public class LevelChooseView extends SurfaceView implements SurfaceHolder.Callback
{
	TableBallActivity tableBallActivity;
	Paint paint;

	public LevelChooseView(TableBallActivity tableBallActivity ) {
		super(tableBallActivity);
		this.tableBallActivity=tableBallActivity;
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
	}

	public void draw(Canvas canvas){
		super.draw(canvas);
		canvas.translate(Constant.screenScaleResult.lucX, Constant.screenScaleResult.lucY);
		canvas.scale(Constant.screenScaleResult.ratio, Constant.screenScaleResult.ratio);
		
		//���Ʊ���
		canvas.drawBitmap(TP_ARRAY[0],xyoffset[0][0], xyoffset[0][1],paint);
		//��һ��
		canvas.drawBitmap(LEVEL_ARRAY[0],xyoffset[20][0], xyoffset[20][1],paint);
		//�ڶ���
		
		canvas.drawBitmap(LEVEL_ARRAY[1],xyoffset[21][0], xyoffset[21][1],paint);
		/**
		 * ���δͨ����һ�ؾͻ���
		 * 
		 * 0��ʾ��ס��1��ʾδ�� 
		 */
		if(DBUtil.getLock(Constant.PLAY_MODEL, 1)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[21][0]+40, xyoffset[21][1]+30,paint);
		}
		//������
		
		canvas.drawBitmap(LEVEL_ARRAY[2],xyoffset[22][0], xyoffset[22][1],paint);
		if(DBUtil.getLock(Constant.PLAY_MODEL, 2)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[22][0]+40, xyoffset[22][1]+30,paint);
		}
		//���Ĺ�
		
		canvas.drawBitmap(LEVEL_ARRAY[3],xyoffset[33][0], xyoffset[33][1],paint);
		if(DBUtil.getLock(Constant.PLAY_MODEL, 3)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[33][0]+40, xyoffset[33][1]+30,paint);
		}
		//�����
		
		canvas.drawBitmap(LEVEL_ARRAY[4],xyoffset[34][0], xyoffset[34][1],paint);
		if(DBUtil.getLock(Constant.PLAY_MODEL, 4)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[34][0]+40, xyoffset[34][1]+30,paint);
		}
		//������
		
		canvas.drawBitmap(LEVEL_ARRAY[5],xyoffset[35][0], xyoffset[35][1],paint);
		if(DBUtil.getLock(Constant.PLAY_MODEL, 5)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[35][0]+40, xyoffset[35][1]+30,paint);
		}
		
		//TODO ���߹�
		canvas.drawBitmap(LEVEL_ARRAY[5],xyoffset[44][0], xyoffset[44][1],paint);//����ť
		if(DBUtil.getLock(Constant.PLAY_MODEL, 6)==0)
		{
			canvas.drawBitmap(HISTORY_ARRAY[10],xyoffset[44][0]+40, xyoffset[44][1]+30,paint);//����
		}
		
		canvas.drawBitmap(TP_ARRAY[19],xyoffset[19][0], xyoffset[19][1],paint);//�����ذ�ť
	}
	public boolean onTouchEvent(MotionEvent e)
	{
		int currentNUm=e.getAction();
		float x=e.getX();
		float y=e.getY();		
		switch(currentNUm)
		{
			case MotionEvent.ACTION_DOWN:
				
				if(x>xyoffsetEvent[20][0]&&x<xyoffsetEvent[20][0]+LEVEL_ARRAY[0].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�һ��
					y>xyoffsetEvent[20][1]&&y<xyoffsetEvent[20][1]+LEVEL_ARRAY[0].getHeight()*Constant.screenScaleResult.ratio)
				{
					Constant.LEVEL=0;
					tableBallActivity.hd.sendEmptyMessage(0);//�����һ��
				}
				else if(x>xyoffsetEvent[21][0]&&x<xyoffsetEvent[21][0]+LEVEL_ARRAY[1].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵڶ���
						y>xyoffsetEvent[21][1]&&y<xyoffsetEvent[21][1]+LEVEL_ARRAY[1].getHeight()*Constant.screenScaleResult.ratio)
					{
						if(DBUtil.getLock(Constant.PLAY_MODEL, 1)==1)
						{
							Constant.LEVEL=1;
							tableBallActivity.hd.sendEmptyMessage(0);//����ڶ���
						}else {
							Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}
				else if(x>xyoffsetEvent[22][0]&&x<xyoffsetEvent[22][0]+LEVEL_ARRAY[2].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�����
						y>xyoffsetEvent[22][1]&&y<xyoffsetEvent[22][1]+LEVEL_ARRAY[2].getHeight()*Constant.screenScaleResult.ratio)
					{
						if(DBUtil.getLock(Constant.PLAY_MODEL, 2)==1)
						{
							Constant.LEVEL=2;
							tableBallActivity.hd.sendEmptyMessage(0);//���������
						}else {
							Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}
				else if(x>xyoffsetEvent[33][0]&&x<xyoffsetEvent[33][0]+LEVEL_ARRAY[3].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�����
						y>xyoffsetEvent[33][1]&&y<xyoffsetEvent[33][1]+LEVEL_ARRAY[3].getHeight()*Constant.screenScaleResult.ratio)
					{
						if(DBUtil.getLock(Constant.PLAY_MODEL, 3)==1)
						{
							Constant.LEVEL=3;
							tableBallActivity.hd.sendEmptyMessage(0);//������Ĺ�
						}else {
							Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}
				else if(x>xyoffsetEvent[34][0]&&x<xyoffsetEvent[34][0]+LEVEL_ARRAY[4].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�����
						y>xyoffsetEvent[34][1]&&y<xyoffsetEvent[34][1]+LEVEL_ARRAY[4].getHeight()*Constant.screenScaleResult.ratio)
					{
						if(DBUtil.getLock(Constant.PLAY_MODEL, 4)==1)
						{
							Constant.LEVEL=4;
							tableBallActivity.hd.sendEmptyMessage(0);//��������
						}else {
							Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}
				else if(x>xyoffsetEvent[35][0]&&x<xyoffsetEvent[35][0]+LEVEL_ARRAY[5].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�����
						y>xyoffsetEvent[35][1]&&y<xyoffsetEvent[35][1]+LEVEL_ARRAY[5].getHeight()*Constant.screenScaleResult.ratio)
					{
						Constant.LEVEL=5;
						tableBallActivity.hd.sendEmptyMessage(0);//���������
						if(DBUtil.getLock(Constant.PLAY_MODEL, 5)==1)
						{
							Constant.LEVEL=5;
							tableBallActivity.hd.sendEmptyMessage(0);//���������
						}else {
							Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
						}
					}
				else if(x>xyoffsetEvent[19][0]&&x<xyoffsetEvent[19][0]+TP_ARRAY[19].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���Ƿ��ذ�ť
						y>xyoffsetEvent[19][1]&&y<xyoffsetEvent[19][1]+TP_ARRAY[19].getHeight()*Constant.screenScaleResult.ratio)
					{
						
						tableBallActivity.hd.sendEmptyMessage(3);//���ص���ת����
					}
				//TODO ���߹�
				else if (x>xyoffsetEvent[44][0]&&x<xyoffsetEvent[44][0]+LEVEL_ARRAY[5].getWidth()*Constant.screenScaleResult.ratio&&//�����ģ���ǵ�����
							y>xyoffsetEvent[44][1]&&y<xyoffsetEvent[44][1]+LEVEL_ARRAY[5].getHeight()*Constant.screenScaleResult.ratio) {
					Constant.LEVEL=6;
					tableBallActivity.hd.sendEmptyMessage(0);//������߹�
					if(DBUtil.getLock(Constant.PLAY_MODEL, 5)==1)//����
					{
						Constant.LEVEL=6;
						tableBallActivity.hd.sendEmptyMessage(0);//������߹�
					}else {
						Toast.makeText(tableBallActivity, "����δ����", Toast.LENGTH_SHORT).show();
					}
				}
			break;
		}
		return true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
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