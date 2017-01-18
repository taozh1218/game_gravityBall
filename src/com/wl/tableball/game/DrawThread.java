package com.wl.tableball.game;

/**
 * ��Ϸ�еĻ滭�߳��Ƕ��������ģ�ֻ�����Ϸ����Ļ��ƹ������ɻ��Ʊ�־λ
 * 
 * @author taozhang
 * 
 */
public class DrawThread extends Thread {
	GameView gameview;

	// ������
	public DrawThread(GameView gameview) {
		this.gameview = gameview;
	}

	@Override
	public void run() {
		// int i=0;
		//һֱ���ƣ����������Ϊtrue
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
