package com.wl.tableball.util;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.wl.tableball.game.R;
import com.wl.tableball.game.TableBallActivity;
/**
 * ����ģ��
 * @author taozhang
 *
 */
public class SoundUtil
{
	public MediaPlayer mp;
	SoundPool soundPool;//������
	HashMap<Integer, Integer> soundPoolMap;
	TableBallActivity tableBallActivity;
	public SoundUtil(TableBallActivity tableBallActivity)//������
	{
		this.tableBallActivity=tableBallActivity;
	}
	/**
	 * ��������صĳ�ʼ��
	 */
    public void initSounds()
    {
    	 //�������������
	     soundPool = new SoundPool
	     (
	    		 6, 							//ͬʱ����ಥ�ŵĸ���
	    		 AudioManager.STREAM_MUSIC,     //��Ƶ������
	    		 100							//�����Ĳ���������Ŀǰ��Ч
	     );
	     
	     //����������ԴMap	     
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //�����ص�������Դid�Ž���Map
	     soundPoolMap.put(0, soundPool.load(tableBallActivity, R.raw.hole_sounds, 1));//����
	     soundPoolMap.put(1, soundPool.load(tableBallActivity, R.raw.wall_sound, 1));//ײľͷ
	     soundPoolMap.put(2, soundPool.load(tableBallActivity, R.raw.ping_sounds, 1));//����
	     
	     //�м�����Ч���е�ǰ�������  R.raw.gamestart���ر�� ����     �����1Ϊ���ȼ� Ŀǰ������
	} 
       
   //������Ч�ķ���
   public void playEffectsSound(int sound, int loop) {
	   if(true)
	   {
		   AudioManager mgr = (AudioManager)tableBallActivity.getSystemService(Context.AUDIO_SERVICE);
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//��ǰ����   
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//�������       
		    float volume = streamVolumeCurrent / streamVolumeMax;   
		    
		    soundPool.play
		    (
	    		soundPoolMap.get(sound), //������Դid
	    		volume, 				 //����������
	    		volume, 				 //����������
	    		1, 						 //���ȼ�				 
	    		loop, 					 //ѭ������ -1������Զѭ��
	    		1f					 //�ط��ٶ�0.5f��2.0f֮��
		    );
	   }
	}
   public void play_bg_sound()//
   {
	   if(Constant.YINYUE_CLOSE)
	   {
		   //ͨ��assets ��������
	     AssetManager assetManager = tableBallActivity.getAssets();  
	     try {  
	     mp = new MediaPlayer();  
	     String s;
	     if(Constant.YINYUE1)
	     {
	    	 s="main.mp3";
	     }
	     else
	     {
	    	 s= "game.mp3"; 
	     }
	     AssetFileDescriptor fileDescriptor= assetManager.openFd(s); 
	     mp.setDataSource(fileDescriptor.getFileDescriptor(),  
	     fileDescriptor.getStartOffset(),  
	     fileDescriptor.getLength()); 
	     mp.setLooping(true);
	     mp.prepare();  
	     mp.start();  
	     } catch (IOException e) 
	     {  
	      e.printStackTrace();  
	     } 
	     Constant.YINYUE_CLOSE=false;
	   }
   }
   
   public void stop_bg_sound()
   {
	   if(!Constant.YINYUE_CLOSE&&mp!=null)
	   {
		   mp.stop();
		   mp.release();
		   Constant.YINYUE_CLOSE=true;
	   }
   }
}
