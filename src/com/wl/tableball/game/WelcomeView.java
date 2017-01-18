package com.wl.tableball.game;

import com.wl.tableball.util.PicLoadUtil;
import static com.wl.tableball.util.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class WelcomeView extends SurfaceView implements Callback {

	TableBallActivity activity;// activity������
	Paint paint; // ����
	int currentAlpha; // ��ǰ�Ĳ�͸��ֵ
	int sleepSpan = 150; // ������ʱ��ms
	Bitmap logo; // logoͼƬ����
	float currentX; // ͼƬλ��
	float currentY;

	public WelcomeView(TableBallActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);// �����������ڻص��ӿڵ�ʵ����
		paint = new Paint();// ��������
		paint.setAntiAlias(true);// �򿪿����
		logo = PicLoadUtil.loadBM(getResources(), "androidheli.png");// ����ͼƬ
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// ���ƺ��������屳��
		paint.setColor(Color.BLACK);// ���û�����ɫ
		paint.setAlpha(255);// ���ò�͸����Ϊ255
		canvas.drawRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, paint);
		// ����ƽ����ͼ
		if (logo == null)
			return;
		paint.setAlpha(currentAlpha);
		canvas.drawBitmap(logo, currentX, currentY, paint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// �����ͼƬ��λ��
		currentX = SCREEN_WIDTH / 2 - logo.getWidth() / 2;
		currentY = SCREEN_HEIGHT / 2 - logo.getHeight() / 2;

		new Thread() {
			public void run() {

				SurfaceHolder mholder = WelcomeView.this.getHolder();// ��ȡ�ص��ӿ�
				for (int i = 255; i > -10; i -= 10) {

					if (i < 0)// �����ǰ��͸����С����
					{
						i = 0;// ����͸������Ϊ��
					}
					currentAlpha = i;
					Canvas canvas = mholder.lockCanvas();// ��ȡ����
					try {
						synchronized (mholder) // ͬ��
						{
							onDraw(canvas);// ���л��ƻ���
						}
						Thread.sleep(200);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							mholder.unlockCanvasAndPost(canvas);
						}
					}

				}
				while (!LOAD_FINISH) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				activity.hd.sendEmptyMessage(1);
			}
		}.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}
