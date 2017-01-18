package com.wl.tableball.model;

import com.wl.tableball.game.GameView;
/**
 * ��ϰģʽ
 * @author taozhang
 *
 */
public class PlayTimeExercise1 extends PlayTime1{

	/**
	 * ��ϰģʽ
	 * @param gameview
	 */
	public PlayTimeExercise1(GameView gameview)
	{
		super(gameview);
		TimeGoThread timegothread=new TimeGoThread();
		timegothread.start();
	}
	
	private class TimeGoThread extends Thread
	{
		@Override
		public void run() {
			while(gameview.heroislive)
			{
				if(!gameview.isGamePause)
				{
					runTime++;
				
					index_m0=(runTime/60)%60/10;//��ϰģʽ�µ�10��λ
					index_m1=(runTime/60)%60%10;//��ϰģʽ�µķ�λ
					index_s0=runTime%60/10;//��ϰģʽ�µ�10��λ
					index_s1=runTime%60%10;//��ϰģʽ�µ���λ
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
