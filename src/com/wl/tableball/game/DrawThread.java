package com.wl.tableball.game;

/**
 * 游戏中的绘画线程是独立出来的，只完成游戏界面的绘制工作，由绘制标志位
 * 
 * @author taozhang
 * 
 */
public class DrawThread extends Thread {
	GameView gameview;

	// 构造器
	public DrawThread(GameView gameview) {
		this.gameview = gameview;
	}

	@Override
	public void run() {
		// int i=0;
		//一直绘制，否则就设置为true
		while (gameview.DRAW_THREAD_FLAG) {
			if (!gameview.isGamePause) {
				gameview.repaint();
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gameview.DRAW_THREAD_FLAG = true;
	}
}
