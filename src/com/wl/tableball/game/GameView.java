package com.wl.tableball.game;

import static com.wl.tableball.util.Constant.TP_ARRAY;

import java.util.ArrayList;
import java.util.Random;

import org.jbox2d.collision.AABB;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wl.tableball.model.Ball;
import com.wl.tableball.model.Box2DUtil;
import com.wl.tableball.model.FalshHole;
import com.wl.tableball.model.MyBody;
import com.wl.tableball.model.PlayTime1;
import com.wl.tableball.model.PlayTimeCount1;
import com.wl.tableball.model.PlayTimeExercise1;
import com.wl.tableball.util.Constant;

/**
 * ��Ϸ����
 * 
 * @author taozhang
 * 
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	public TableBallActivity tableBallActivity;
	Paint paint;
	World world;
	AABB aabb;
	Ball myball; // heroball
	DrawThread drawthread; // �����߳�
	PhysicsThread physicsthread;// ģ����߳�
	/**
	 * ����body�б�������ײ�Ĵ���
	 */
	public ArrayList<MyBody> holelist;
	/**
	 * �����б���ִ����ײ�Ĵ���
	 */
	public ArrayList<MyBody> reclist;
	/**
	 * ���Ŀ����
	 * 
	 * 0��Ŀ�궴
	 * 
	 * 1����
	 */
	public ArrayList<MyBody> herolist;
	/**
	 * �����Ķ��б�
	 */
	public ArrayList<FalshHole> falshholelist;
	PlayTime1 playtime;

	private float previousX;// �ϴεĴ���λ��
	private float previousY;
	private float angdegElevation = 90;// ����
	private float angdegAzimuth = 90;// ��λ��
	private final float TOUCH_SCALE_FACTOR = 180.0f / 400;

	public boolean isGamePause = false;
	/**
	 * �ᶯ����ĳ�ʼ״ֵ̬��false������־ֹͣ
	 */
	public boolean heroislive = false;
	/**
	 *  �����̹߳�����־λ
	 */
	public boolean DRAW_THREAD_FLAG = true;

	static int z = 0;

	public GameView(TableBallActivity tableBallActivity) {
		super(tableBallActivity);
		this.getHolder().addCallback(this);
		this.tableBallActivity = tableBallActivity;
		// ���û���
		paint = new Paint();
		paint.setAntiAlias(true);
		// ��ʼ���б�
		holelist = new ArrayList<MyBody>();
		reclist = new ArrayList<MyBody>();
		herolist = new ArrayList<MyBody>();
		falshholelist = new ArrayList<FalshHole>();

		loadGameData();// ��ʼ����Ϸ��Դ
		initContactListener();

		z++;

		physicsthread = new PhysicsThread(this);// ����ģ���߳�
		drawthread = new DrawThread(this);// �����Ự�߳�
		physicsthread.start();
		drawthread.start();

	}

	/**
	 * ����С��
	 */
	public void ballActivate() {
		myball.body.wakeUp();
	}

	/**
	 * ��ʼ����Ϸ��Դ
	 */
	public void loadGameData() {
		heroislive = true;// ��ʼ��heroball��С�򣩵�״̬
		DRAW_THREAD_FLAG = true;// �滭�̱߳�־λ
		isGamePause = false;
		aabb = new AABB();// ������Χ����
		// ָ�����ӵ����½�
		aabb.lowerBound.set(-100.0f, -100.0f);
		aabb.upperBound.set(1000.0f, 1000.0f);
		boolean doSleep = true;
		world = new World(aabb, Constant.GRAVITYTEMP, doSleep);// ��������

		if (Constant.LEVEL != Constant.TEMPLEVEL) {// ���Բ�ͬ�Ĺ�
			Constant.initBoxBitmap1(getResources());// ��ʼ��ľ��ͼƬ
		}
		Constant.TEMPLEVEL = Constant.LEVEL;

		// ���ƶ���,���ض�������
		for (int i = 0; i < Constant.bhoffset[Constant.LEVEL].length; i++) {
			holelist.add(Box2DUtil.createHole(world,
					Constant.bhoffset[Constant.LEVEL][i][0],// ��ȡx����
					Constant.bhoffset[Constant.LEVEL][i][1],// y����
					Constant.R,// �뾶
					true,// ���Ǿ�̬��
					Constant.TP_ARRAY[5], this));
		}

		// box����
		for (int i = 0; i < Constant.xyScale[Constant.LEVEL].length; i++) {
			reclist.add(Box2DUtil.creatRec(world,
					Constant.xycenter[Constant.LEVEL][i][0],
					Constant.xycenter[Constant.LEVEL][i][1],
					Constant.xyScale[Constant.LEVEL][i][0] * 50,
					Constant.xyScale[Constant.LEVEL][i][1] * 50, true,
					Constant.BOX_TP_ARRAY1[i], this));
		}
		// ��ӻ����Ķ�
		Random random = new Random();// ��������򶴿���ʱ��
		for (int i = 0; i < Constant.flashHolePositon[Constant.LEVEL].length; i++) {
			falshholelist.add(Box2DUtil.createFalshHole(world,
					Constant.flashHolePositon[Constant.LEVEL][i][0],
					Constant.flashHolePositon[Constant.LEVEL][i][1],
					Constant.R, true, Constant.TP_FLASHHOLE[1][0],
					1 + random.nextInt(10),// ��������������1+random.nextInt(10)����1��Ϊ�˷�ֹrandom.nextInt(10)�����O�����˸�Ķ���
					1 + random.nextInt(10)// ��������������1+random.nextInt(10)����1��Ϊ�˷�ֹrandom.nextInt(10)�����O�����˸�Ķ���
					, this));
		}

		// ����heroball���������ӵģ�Ϊ�˱�֤������д�����ֱ�Ӵ��б�����

		// Ŀ���򶴣�������herolist�ĵ�һ����λ��
		myball = Box2DUtil.createHole(world,
				Constant.ballendplace[Constant.LEVEL][0],
				Constant.ballendplace[Constant.LEVEL][1], Constant.R, true,
				Constant.TP_ARRAY[10], this);
		herolist.add(myball);
		// heroball��������herolist�ĵڶ���λ��
		myball = Box2DUtil.createBall(world,
				Constant.ballstartplace[Constant.LEVEL][0],
				Constant.ballstartplace[Constant.LEVEL][1], Constant.R, false,
				Constant.TP_ARRAY[6], this);
		herolist.add(myball);

		// ���ؼ�ʱ��
		if (Constant.PLAY_MODEL == Constant.PLAY_MODEL1) {
			playtime = new PlayTimeCount1(this);
		} else {
			playtime = new PlayTimeExercise1(this);
		}
	}

	/**
	 * ��ײ����
	 */
	void initContactListener() {
		ContactListener contactlistener = new ContactListener() {
			@Override
			public void add(ContactPoint arg0) {
				// ��ײ��⴦��
				CollisionAction.doAction(GameView.this, arg0.shape1.getBody(),
						arg0.shape2.getBody(), arg0.position.x,
						arg0.position.y, arg0.normal, arg0.velocity);
			}

			@Override
			public void persist(ContactPoint arg0) {
			}

			@Override
			public void remove(ContactPoint arg0) {

			}

			@Override
			public void result(ContactResult arg0) {
			}
		};
		world.setContactListener(contactlistener);
	}

	public boolean onTouchEvent(MotionEvent e) {
		int action = e.getAction();
		float x = e.getX();
		float y = e.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// �����ͣ��ťʽ�¼�
			if (x > Constant.xyoffsetEvent[30][0]
					&& x < Constant.xyoffsetEvent[30][0]
							+ TP_ARRAY[19].getWidth()
							* Constant.screenScaleResult.ratio
					&& // �����ģ�ķ��ذ�ť
					y > Constant.xyoffsetEvent[30][1]
					&& y < Constant.xyoffsetEvent[30][1]
							+ TP_ARRAY[19].getHeight()
							* Constant.screenScaleResult.ratio) {
				isGamePause = !isGamePause;
				repaint();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;// ���㴥�ر�Yλ��
			float dx = x - previousX;// ���㴥�ر�Yλ��
			angdegAzimuth += dx * TOUCH_SCALE_FACTOR;// ������y����ת�Ƕ�
			angdegElevation += dy * TOUCH_SCALE_FACTOR;// ������x����ת�Ƕ�
			if (angdegElevation > 90) {
				angdegElevation = 90;
			} else if (angdegElevation < 0) {
				angdegElevation = 0;
			}
		}
		previousY = y;// ��¼���ر�λ��
		previousX = x;// ��¼���ر�λ��

		return true;
	}

	/**
	 * ���滭�����任�������滭���ָ�������
	 * 
	 * �Ȼ��Ʊ�����Ȼ����ø�������drawSelf���������ƹ��ɸ��壬������ʱ�����ͣ��ť
	 */
	@Override
	public void onDraw(Canvas canvas) {

		if (canvas == null) {
			return;
		}
		canvas.save();// ���滭��
		canvas.translate(Constant.screenScaleResult.lucX,
				Constant.screenScaleResult.lucY);
		canvas.scale(Constant.screenScaleResult.ratio,
				Constant.screenScaleResult.ratio);

		canvas.drawBitmap(Constant.TP_ARRAY[3], 0.0f, 0.0f, paint);// �Ȼ�����
		for (MyBody mybodytemp : holelist) {// ���ƶ�
			mybodytemp.drawself(canvas, paint);
		}
		for (MyBody mybodytemp : falshholelist) {// ���ƻ����Ķ�
			mybodytemp.drawself(canvas, paint);

		}
		for (MyBody mybodytemp : reclist) {// ����ľ��
			mybodytemp.drawself(canvas, paint);
		}
		for (MyBody mybodytemp : herolist) {// �������Ŀ�궴
			mybodytemp.drawself(canvas, paint);
		}
		canvas.drawBitmap(TP_ARRAY[40], 0, 0, paint);// ��
		canvas.drawBitmap(TP_ARRAY[41], 0, 457, paint);// ��
		canvas.drawBitmap(TP_ARRAY[42], 0, 0, paint);// ��
		canvas.drawBitmap(TP_ARRAY[43], 779, 3, paint);// ��

		playtime.drawself(canvas, paint);

		if (isGamePause) {
			canvas.drawBitmap(Constant.TP_ARRAY[30], 1, 1, paint);
		} else {
			canvas.drawBitmap(Constant.TP_ARRAY[36], 1, 1, paint);
		}

		canvas.restore();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// ֹͣ����������
		// ������Ϸ��������
		if (Constant.bg_music_sound) {
			Constant.YINYUE1 = false;
			this.tableBallActivity.soundutil.stop_bg_sound();
			this.tableBallActivity.soundutil.play_bg_sound();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	/**
	 * ��DrawThread����
	 */
	public void repaint() {
		SurfaceHolder holder = this.getHolder();
		Canvas canvas = holder.lockCanvas();// ��ȡ����
		try {
			synchronized (holder) {
				onDraw(canvas);// ����
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}
