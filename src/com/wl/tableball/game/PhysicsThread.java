package com.wl.tableball.game;

import com.wl.tableball.util.Constant;
/**
 * 模拟线程，控制整个游戏界面的模拟频率，也就是流畅度，只负责模拟，不负责绘画
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
		//英雄死了，就不在模拟了
		while(gameview.heroislive)
		{
			if(!gameview.isGamePause)
			{
				gameview.world.setGravity(Constant.GRAVITYTEMP);
				gameview.world.step(Constant.TIME_STEP, Constant.ITERA);//模拟频率
			}
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
}
