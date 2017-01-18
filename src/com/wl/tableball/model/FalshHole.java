package com.wl.tableball.model;

import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

import com.wl.tableball.game.GameView;
import com.wl.tableball.util.Constant;

/**
 * ��С�ɱ仯���򶴣�����򿪻�رյ���
 * 
 * @author taozhang
 * 
 */
public class FalshHole extends Hole {
	/**
	 * ���պϣ��������õ�ʱ�䣬��λΪ��
	 */
	int timeDeadSpan;
	/**
	 * ���򿪣������õ�ʱ�䣬��λΪ��
	 */
	int timeLiveSpan;
	/**
	 * �ǲ��ǿ������Ķ�
	 */
	public boolean isFlashDead = true;

	FalshHole(Body body, Bitmap bitmap, int timeDeadSpan, int timeLiveSpan,
			GameView gameview) {
		super(body, bitmap, gameview);
		this.timeDeadSpan = timeDeadSpan;
		this.timeLiveSpan = timeLiveSpan;
		// ����һ����ͼ��������ײ��־λ���߳�
		FlashThread flashThread = new FlashThread();
		flashThread.start();
	}

	/**
	 * ��ͼ��������ײ��־λ���߳�
	 * 
	 * @author taozhang
	 * 
	 */
	private class FlashThread extends Thread {

		@Override
		public void run() {
			while (gameview.heroislive) {// ����Ϊ����
				if (!gameview.isGamePause) {// δ��ͣ������ֹͣ�Ͳ��ٽ��п�����
					isFlashDead = true;
					// ���л�ͼ
					for (int i = 1; i < Constant.TRUE_FLASHHOLE.length * 2 - 1; i++) {
						// ��ͣ
						while (gameview.isGamePause) {
							try {
								Thread.sleep(1000);// �ڰ�����ͣʱ�Ͷ��������ʹ���պϻ�������
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						// TODO �Ұ�������P539�������޸�
						// ��ͼ
						if (i < (Constant.TP_FLASHHOLE.length - 1)) {// �𽥴�
							bitmap = Constant.TRUE_FLASHHOLE[Math
									.abs(Constant.TP_FLASHHOLE.length - 1 - i)];// ����x�ľ���ֵ
						} else {// �𽥹ر�
							System.out.println("Constant.TP_FLASHHOLE.length:"+(Constant.TP_FLASHHOLE.length* 2 - 1
									- i));
							bitmap = Constant.TRUE_FLASHHOLE[Math
									.abs(Constant.TP_FLASHHOLE.length * 2 - 1
											- i)];
//							bitmap = Constant.TRUE_FLASHHOLE[Math
//									.abs(Constant.TP_FLASHHOLE.length - 1 - i
//											- i)];
						}
						try {
							Thread.sleep(timeDeadSpan * 1000
									/ (Constant.TRUE_FLASHHOLE.length * 2));// ���ú����˯��ʱ��
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// ͼƬ���꣬������Ӧ
					isFlashDead = false;
					bitmap = Constant.TRUE_FLASHHOLE[13];// ����ͼ
					try {
						Thread.sleep(timeLiveSpan * 1000);// ˯��
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (gameview.isGamePause)// ��������ͣ��ť��ʱ�����˯��
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
