package com.wl.tableball.model;

import com.wl.tableball.game.GameView;
/**
 * ��ʱģʽ:1min
 * @author taozhang
 *
 */
public class PlayTimeCount1 extends PlayTime1{

	int countTime=1*60;//��ʱ��ʱ�䣬��1*60,1�Ƿ֣�60����
	/**
	 * ��ʱģʽ
	 * @param gameview
	 */
	public PlayTimeCount1(GameView gameview)
	{
		super(gameview);
		TimeGoThread timegothread=new TimeGoThread();
		timegothread.start();
	}
	
	private class TimeGoThread extends Thread
	{
		@Override
		public void run() {
			while(runTime<countTime&&gameview.heroislive)
			{
				if(!gameview.isGamePause)
				{
					runTime++;
				
					index_m0=((countTime-runTime)/60)%60/10;//��ϰģʽ�µ�10��λ
					index_m1=((countTime-runTime)/60)%60%10;//��ϰģʽ�µķ�λ
					index_s0=(countTime-runTime)%60/10;//��ϰģʽ�µ�10��λ
					index_s1=(countTime-runTime)%60%10;//��ϰģʽ�µ���λ
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(runTime>=countTime)
			{
				gameview.heroislive=false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				gameview.DRAW_THREAD_FLAG=false;//gameview �����߳�ֹͣ
				PlayTimeCount1.this.gameview.tableBallActivity.hd.sendEmptyMessage(2);
			}
		}
	}
}
