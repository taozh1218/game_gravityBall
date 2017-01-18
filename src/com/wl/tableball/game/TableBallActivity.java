package com.wl.tableball.game;

import java.util.HashMap;
import org.jbox2d.common.Vec2;
import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.wl.tableball.util.Constant;
import com.wl.tableball.util.DBUtil;
import com.wl.tableball.util.SoundUtil;

public class TableBallActivity extends Activity {
	WhichView curr;// 当前的界面
	SharedPreferences.Editor editor;// 向SharedPreferences中写回数据
	/**
	 *  游戏界面
	 */
	GameView gv;
	NextView gotoNextView;// 跳转界面
	PatternChooseView apv;// 模式选择界面
	SettingsView asv;// 设置界面
	HistoryView ahv;// 历史记录界面
	LevelChooseView apcv;// 选关界面
	WelcomeView wcv;
	AudioManager audio;
	MainMenuView mmv;// 主菜单界面
	HashMap<Integer, Integer> soundPoolMap;
	// 声音类
	public SoundUtil soundutil;
	SharedPreferences sp;// 获取SharedPreferences

	/**
	 * 传感器管理者
	 */
	SensorManager mySensorManager;
	/**
	 * 传感器对象，这里是加速度传感器
	 */
	Sensor accelerometerSensor;
	/**
	 * 震动服务对象
	 */
	Vibrator vibrator;// 震动

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置为全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置为横屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 声音控制
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		// 获取屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		if (dm.widthPixels > dm.heightPixels) {
			Constant.SCREEN_WIDTH = dm.widthPixels;
			Constant.SCREEN_HEIGHT = dm.heightPixels;
		} else {
			Constant.SCREEN_WIDTH = dm.heightPixels;
			Constant.SCREEN_HEIGHT = dm.widthPixels;
		}
		new Thread() {
			public void run() {
				Constant.scaleCL(); 				// 初始化坐标
				Constant.initDB(); 					// 初始化数据库
				Constant.initBitmap(getResources());// 初始化图片
				Constant.LOAD_FINISH = true; 		// 指定标志位
			}
		}.start();

		soundutil = new SoundUtil(this);
		soundutil.initSounds();

		mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 获取传感器管理器
		accelerometerSensor = mySensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// 获取加速度传感器
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);// 获得震动服务对象

		Constant.from_nextview = true;
		hd.sendEmptyMessage(7);

	}

	// 消息接收
	public Handler hd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				gotoGameView(); // 去gameview
				break;
			case 1:
				gotoMainMenuView();// 去主界面
				break;
			case 2:
				gotoNextView();// 去跳转界面
				break;
			case 3:
				gotoPatternChooseView();// 去模式选择界面
				break;
			case 4:
				gotoSettingsView();// 去设置界面
				break;
			case 5:
				gotoHistoryView();// 去历史记录界面
				break;
			case 6:
				gotoLevelChooseView();// 去选关界面
				break;
			case 7:
				gotoWelcomeView();
				break;
			}
		}
	};

	/**
	 * 传感器监听器
	 */
	private SensorEventListener mySensorListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		/**
		 * 主要是对此方法的重写，计算出重力在屏幕上的投影方向及大小，并将值赋给GRAVITYTEMP，用于游戏控制
		 */
		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			// 计算出重力在屏幕上的投影方向
			Constant.GRAVITYTEMP = new Vec2(Constant.GRAVITY * values[1],
					Constant.GRAVITY * values[0]);
			//唤醒小球
			if (gv != null) {
				gv.ballActivate();
			}
		}
	};

	// 进入游戏界面
	public void gotoGameView() {
		// 打开线程
		// Constant.DRAW_THREAD_FLAG=true;
		curr = WhichView.GAME_VIEW;
		gv = new GameView(this);
		setContentView(gv);
	}

	// 进入开始界面
	public void gotoMainMenuView() {
		curr = WhichView.MAIN_MENU_VIEW;
		mmv = new MainMenuView(this);
		setContentView(mmv);
	}

	// 进入中间的跳转界面
	public void gotoNextView() {
		this.gv.heroislive = false;
		this.gv.DRAW_THREAD_FLAG = false;
		curr = WhichView.GOTO_NEXT_VIEW;
		gotoNextView = new NextView(this);
		setContentView(gotoNextView);
	}

	// 进入按下开始游戏按钮后的界面
	public void gotoPatternChooseView() {
		curr = WhichView.After_Play_View;
		apv = new PatternChooseView(this);
		setContentView(apv);
	}

	// 进入按下设置后的按钮
	public void gotoSettingsView() {
		curr = WhichView.After_Settings_View;
		asv = new SettingsView(this);
		setContentView(asv);
	}

	// 进入按下历史记录的按钮
	public void gotoHistoryView() {
		curr = WhichView.After_History_View;
		ahv = new HistoryView(this);
		setContentView(ahv);
	}

	// 进入选关界面
	public void gotoWelcomeView() {
		curr = WhichView.After_Play_Choose_View;
		wcv = new WelcomeView(this);
		setContentView(wcv);
	}

	public void gotoLevelChooseView() {
		curr = WhichView.WELCOME_VIEW;
		apcv = new LevelChooseView(this);
		setContentView(apcv);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP://音量加
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE,//增大
					AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN://音量减
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		case 4://返回键
			if (curr == WhichView.MAIN_MENU_VIEW) {
				int a, b, c;
				if (Constant.YINYUE_CLOSE == true) {
					a = 2;
				} else {
					a = 1;
				}
				if (Constant.YINXIAO_OPEN == true) {
					b = 1;
				} else {
					b = 2;
				}
				if (Constant.ZHENDONG_OPEN == true) {
					c = 1;
				} else {
					c = 2;
				}
				DBUtil.updateSetting(a, b, c);
				System.exit(0);
			} else if (curr == WhichView.GAME_VIEW) {
				hd.sendEmptyMessage(2);
			} else if (curr == WhichView.GOTO_NEXT_VIEW) {
				Constant.from_nextview = true;
				hd.sendEmptyMessage(1);
			} else if (curr == WhichView.After_Play_View
					|| curr == WhichView.After_Settings_View
					|| curr == WhichView.After_History_View) {
				hd.sendEmptyMessage(1);
			} else if (curr == WhichView.After_Play_Choose_View) {
				hd.sendEmptyMessage(3);
			}
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//取消注册监听器

	}

	@Override
	protected void onResume() {
		super.onResume();
		mySensorManager.registerListener(mySensorListener, accelerometerSensor,
				SensorManager.SENSOR_DELAY_GAME);//注册监听器
	}
}

/**
 * 在枚举类型中声明的第一个枚举成员它的默值为零。
 * 
 * @author taozhang
 * 
 */
enum WhichView {
	WELCOME_VIEW, GAME_VIEW, MAIN_MENU_VIEW, GOTO_NEXT_VIEW, After_Play_View, After_Settings_View, After_History_View, After_Play_Choose_View
}