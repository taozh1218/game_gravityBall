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
	WhichView curr;// ��ǰ�Ľ���
	SharedPreferences.Editor editor;// ��SharedPreferences��д������
	/**
	 *  ��Ϸ����
	 */
	GameView gv;
	NextView gotoNextView;// ��ת����
	PatternChooseView apv;// ģʽѡ�����
	SettingsView asv;// ���ý���
	HistoryView ahv;// ��ʷ��¼����
	LevelChooseView apcv;// ѡ�ؽ���
	WelcomeView wcv;
	AudioManager audio;
	MainMenuView mmv;// ���˵�����
	HashMap<Integer, Integer> soundPoolMap;
	// ������
	public SoundUtil soundutil;
	SharedPreferences sp;// ��ȡSharedPreferences

	/**
	 * ������������
	 */
	SensorManager mySensorManager;
	/**
	 * ���������������Ǽ��ٶȴ�����
	 */
	Sensor accelerometerSensor;
	/**
	 * �𶯷������
	 */
	Vibrator vibrator;// ��

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����Ϊȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����Ϊ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// ��������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
		// ��ȡ��Ļ�ߴ�
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
				Constant.scaleCL(); 				// ��ʼ������
				Constant.initDB(); 					// ��ʼ�����ݿ�
				Constant.initBitmap(getResources());// ��ʼ��ͼƬ
				Constant.LOAD_FINISH = true; 		// ָ����־λ
			}
		}.start();

		soundutil = new SoundUtil(this);
		soundutil.initSounds();

		mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// ��ȡ������������
		accelerometerSensor = mySensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// ��ȡ���ٶȴ�����
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);// ����𶯷������

		Constant.from_nextview = true;
		hd.sendEmptyMessage(7);

	}

	// ��Ϣ����
	public Handler hd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				gotoGameView(); // ȥgameview
				break;
			case 1:
				gotoMainMenuView();// ȥ������
				break;
			case 2:
				gotoNextView();// ȥ��ת����
				break;
			case 3:
				gotoPatternChooseView();// ȥģʽѡ�����
				break;
			case 4:
				gotoSettingsView();// ȥ���ý���
				break;
			case 5:
				gotoHistoryView();// ȥ��ʷ��¼����
				break;
			case 6:
				gotoLevelChooseView();// ȥѡ�ؽ���
				break;
			case 7:
				gotoWelcomeView();
				break;
			}
		}
	};

	/**
	 * ������������
	 */
	private SensorEventListener mySensorListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		/**
		 * ��Ҫ�ǶԴ˷�������д���������������Ļ�ϵ�ͶӰ���򼰴�С������ֵ����GRAVITYTEMP��������Ϸ����
		 */
		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			// �������������Ļ�ϵ�ͶӰ����
			Constant.GRAVITYTEMP = new Vec2(Constant.GRAVITY * values[1],
					Constant.GRAVITY * values[0]);
			//����С��
			if (gv != null) {
				gv.ballActivate();
			}
		}
	};

	// ������Ϸ����
	public void gotoGameView() {
		// ���߳�
		// Constant.DRAW_THREAD_FLAG=true;
		curr = WhichView.GAME_VIEW;
		gv = new GameView(this);
		setContentView(gv);
	}

	// ���뿪ʼ����
	public void gotoMainMenuView() {
		curr = WhichView.MAIN_MENU_VIEW;
		mmv = new MainMenuView(this);
		setContentView(mmv);
	}

	// �����м����ת����
	public void gotoNextView() {
		this.gv.heroislive = false;
		this.gv.DRAW_THREAD_FLAG = false;
		curr = WhichView.GOTO_NEXT_VIEW;
		gotoNextView = new NextView(this);
		setContentView(gotoNextView);
	}

	// ���밴�¿�ʼ��Ϸ��ť��Ľ���
	public void gotoPatternChooseView() {
		curr = WhichView.After_Play_View;
		apv = new PatternChooseView(this);
		setContentView(apv);
	}

	// ���밴�����ú�İ�ť
	public void gotoSettingsView() {
		curr = WhichView.After_Settings_View;
		asv = new SettingsView(this);
		setContentView(asv);
	}

	// ���밴����ʷ��¼�İ�ť
	public void gotoHistoryView() {
		curr = WhichView.After_History_View;
		ahv = new HistoryView(this);
		setContentView(ahv);
	}

	// ����ѡ�ؽ���
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
		case KeyEvent.KEYCODE_VOLUME_UP://������
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE,//����
					AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN://������
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		case 4://���ؼ�
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
		mySensorManager.unregisterListener(mySensorListener);//ȡ��ע�������

	}

	@Override
	protected void onResume() {
		super.onResume();
		mySensorManager.registerListener(mySensorListener, accelerometerSensor,
				SensorManager.SENSOR_DELAY_GAME);//ע�������
	}
}

/**
 * ��ö�������������ĵ�һ��ö�ٳ�Ա����ĬֵΪ�㡣
 * 
 * @author taozhang
 * 
 */
enum WhichView {
	WELCOME_VIEW, GAME_VIEW, MAIN_MENU_VIEW, GOTO_NEXT_VIEW, After_Play_View, After_Settings_View, After_History_View, After_Play_Choose_View
}