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
 * 游戏界面
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
	DrawThread drawthread; // 画的线程
	PhysicsThread physicsthread;// 模拟的线程
	/**
	 * 洞的body列表，进行碰撞的处理
	 */
	public ArrayList<MyBody> holelist;
	/**
	 * 挡板列表，不执行碰撞的处理
	 */
	public ArrayList<MyBody> reclist;
	/**
	 * 球和目标球洞
	 * 
	 * 0是目标洞
	 * 
	 * 1是球
	 */
	public ArrayList<MyBody> herolist;
	/**
	 * 会闪的洞列表
	 */
	public ArrayList<FalshHole> falshholelist;
	PlayTime1 playtime;

	private float previousX;// 上次的触控位置
	private float previousY;
	private float angdegElevation = 90;// 仰角
	private float angdegAzimuth = 90;// 方位角
	private final float TOUCH_SCALE_FACTOR = 180.0f / 400;

	public boolean isGamePause = false;
	/**
	 * 会动的球的初始状态值（false），标志停止
	 */
	public boolean heroislive = false;
	/**
	 *  绘制线程工作标志位
	 */
	public boolean DRAW_THREAD_FLAG = true;

	static int z = 0;

	public GameView(TableBallActivity tableBallActivity) {
		super(tableBallActivity);
		this.getHolder().addCallback(this);
		this.tableBallActivity = tableBallActivity;
		// 设置画笔
		paint = new Paint();
		paint.setAntiAlias(true);
		// 初始化列表
		holelist = new ArrayList<MyBody>();
		reclist = new ArrayList<MyBody>();
		herolist = new ArrayList<MyBody>();
		falshholelist = new ArrayList<FalshHole>();

		loadGameData();// 初始化游戏资源
		initContactListener();

		z++;

		physicsthread = new PhysicsThread(this);// 创建模拟线程
		drawthread = new DrawThread(this);// 创建会话线程
		physicsthread.start();
		drawthread.start();

	}

	/**
	 * 唤醒小球
	 */
	public void ballActivate() {
		myball.body.wakeUp();
	}

	/**
	 * 初始化游戏资源
	 */
	public void loadGameData() {
		heroislive = true;// 初始化heroball（小球）的状态
		DRAW_THREAD_FLAG = true;// 绘画线程标志位
		isGamePause = false;
		aabb = new AABB();// 创建包围盒子
		// 指定盒子的上下界
		aabb.lowerBound.set(-100.0f, -100.0f);
		aabb.upperBound.set(1000.0f, 1000.0f);
		boolean doSleep = true;
		world = new World(aabb, Constant.GRAVITYTEMP, doSleep);// 创建世界

		if (Constant.LEVEL != Constant.TEMPLEVEL) {// 来自不同的关
			Constant.initBoxBitmap1(getResources());// 初始化木块图片
		}
		Constant.TEMPLEVEL = Constant.LEVEL;

		// 绘制洞口,加载洞口数据
		for (int i = 0; i < Constant.bhoffset[Constant.LEVEL].length; i++) {
			holelist.add(Box2DUtil.createHole(world,
					Constant.bhoffset[Constant.LEVEL][i][0],// 获取x坐标
					Constant.bhoffset[Constant.LEVEL][i][1],// y坐标
					Constant.R,// 半径
					true,// 球洞是静态的
					Constant.TP_ARRAY[5], this));
		}

		// box刚体
		for (int i = 0; i < Constant.xyScale[Constant.LEVEL].length; i++) {
			reclist.add(Box2DUtil.creatRec(world,
					Constant.xycenter[Constant.LEVEL][i][0],
					Constant.xycenter[Constant.LEVEL][i][1],
					Constant.xyScale[Constant.LEVEL][i][0] * 50,
					Constant.xyScale[Constant.LEVEL][i][1] * 50, true,
					Constant.BOX_TP_ARRAY1[i], this));
		}
		// 添加会闪的洞
		Random random = new Random();// 用于随机球洞开关时间
		for (int i = 0; i < Constant.flashHolePositon[Constant.LEVEL].length; i++) {
			falshholelist.add(Box2DUtil.createFalshHole(world,
					Constant.flashHolePositon[Constant.LEVEL][i][0],
					Constant.flashHolePositon[Constant.LEVEL][i][1],
					Constant.R, true, Constant.TP_FLASHHOLE[1][0],
					1 + random.nextInt(10),// 这个参数的最后是1+random.nextInt(10)，加1是为了防止random.nextInt(10)会产生O秒就闪烁的洞来
					1 + random.nextInt(10)// 这个参数的最后是1+random.nextInt(10)，加1是为了防止random.nextInt(10)会产生O秒就闪烁的洞来
					, this));
		}

		// 绘制heroball，它是最后加的，为了保证对其进行处理是直接从列表中找

		// 目标球洞，他放在herolist的第一个个位置
		myball = Box2DUtil.createHole(world,
				Constant.ballendplace[Constant.LEVEL][0],
				Constant.ballendplace[Constant.LEVEL][1], Constant.R, true,
				Constant.TP_ARRAY[10], this);
		herolist.add(myball);
		// heroball，它放在herolist的第二个位置
		myball = Box2DUtil.createBall(world,
				Constant.ballstartplace[Constant.LEVEL][0],
				Constant.ballstartplace[Constant.LEVEL][1], Constant.R, false,
				Constant.TP_ARRAY[6], this);
		herolist.add(myball);

		// 加载计时器
		if (Constant.PLAY_MODEL == Constant.PLAY_MODEL1) {
			playtime = new PlayTimeCount1(this);
		} else {
			playtime = new PlayTimeExercise1(this);
		}
	}

	/**
	 * 碰撞监听
	 */
	void initContactListener() {
		ContactListener contactlistener = new ContactListener() {
			@Override
			public void add(ContactPoint arg0) {
				// 碰撞检测处理
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
			// 添加暂停按钮式事件
			if (x > Constant.xyoffsetEvent[30][0]
					&& x < Constant.xyoffsetEvent[30][0]
							+ TP_ARRAY[19].getWidth()
							* Constant.screenScaleResult.ratio
					&& // 如果差模的返回按钮
					y > Constant.xyoffsetEvent[30][1]
					&& y < Constant.xyoffsetEvent[30][1]
							+ TP_ARRAY[19].getHeight()
							* Constant.screenScaleResult.ratio) {
				isGamePause = !isGamePause;
				repaint();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;// 计算触控笔Y位移
			float dx = x - previousX;// 计算触控笔Y位移
			angdegAzimuth += dx * TOUCH_SCALE_FACTOR;// 设置沿y轴旋转角度
			angdegElevation += dy * TOUCH_SCALE_FACTOR;// 设置沿x轴旋转角度
			if (angdegElevation > 90) {
				angdegElevation = 90;
			} else if (angdegElevation < 0) {
				angdegElevation = 0;
			}
		}
		previousY = y;// 记录触控笔位置
		previousX = x;// 记录触控笔位置

		return true;
	}

	/**
	 * 保存画布、变换画布、绘画、恢复画布，
	 * 
	 * 先绘制背景、然后调用刚体对象的drawSelf方法，绘制归纳刚体，最后绘制时间和暂停按钮
	 */
	@Override
	public void onDraw(Canvas canvas) {

		if (canvas == null) {
			return;
		}
		canvas.save();// 保存画布
		canvas.translate(Constant.screenScaleResult.lucX,
				Constant.screenScaleResult.lucY);
		canvas.scale(Constant.screenScaleResult.ratio,
				Constant.screenScaleResult.ratio);

		canvas.drawBitmap(Constant.TP_ARRAY[3], 0.0f, 0.0f, paint);// 先画背景
		for (MyBody mybodytemp : holelist) {// 绘制洞
			mybodytemp.drawself(canvas, paint);
		}
		for (MyBody mybodytemp : falshholelist) {// 绘制会闪的栋
			mybodytemp.drawself(canvas, paint);

		}
		for (MyBody mybodytemp : reclist) {// 绘制木块
			mybodytemp.drawself(canvas, paint);
		}
		for (MyBody mybodytemp : herolist) {// 绘制球和目标洞
			mybodytemp.drawself(canvas, paint);
		}
		canvas.drawBitmap(TP_ARRAY[40], 0, 0, paint);// 上
		canvas.drawBitmap(TP_ARRAY[41], 0, 457, paint);// 下
		canvas.drawBitmap(TP_ARRAY[42], 0, 0, paint);// 左
		canvas.drawBitmap(TP_ARRAY[43], 779, 3, paint);// 右

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
		// 停止主界面音乐
		// 播放游戏背景音乐
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
	 * 由DrawThread调用
	 */
	public void repaint() {
		SurfaceHolder holder = this.getHolder();
		Canvas canvas = holder.lockCanvas();// 获取画布
		try {
			synchronized (holder) {
				onDraw(canvas);// 绘制
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
