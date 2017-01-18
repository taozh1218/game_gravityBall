package com.wl.tableball.game;

import com.wl.tableball.util.Constant;
/**
 * ģ���̣߳�����������Ϸ�����ģ��Ƶ�ʣ�Ҳ���������ȣ�ֻ����ģ�⣬������滭
 * @author taozhang
 *
 */
public class PhysicsThread extends Thread{
	GameView gameview;

	public PhysicsThread(GameView gameview) {
		super();
		this.gameview = gameview;
	}
	
	@Override
	public void run() {
		//Ӣ�����ˣ��Ͳ���ģ����
		while(gameview.heroislive)
		{
			if(!gameview.isGamePause)
			{
				gameview.world.setGravity(Constant.GRAVITYTEMP);
				gameview.world.step(Constant.TIME_STEP, Constant.ITERA);//ģ��Ƶ��
			}
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
}
