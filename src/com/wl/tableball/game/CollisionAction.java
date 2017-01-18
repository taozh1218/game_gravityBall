package com.wl.tableball.game;

import static com.wl.tableball.util.Constant.YINXIAO_OPEN;
import static com.wl.tableball.util.Constant.ZHENDONG_OPEN;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import com.wl.tableball.model.FalshHole;
import com.wl.tableball.model.MyBody;
import com.wl.tableball.util.Constant;
import com.wl.tableball.util.DBUtil;

/**
 * 碰撞响应类
 * @author taozhang
 *
 */
public class CollisionAction {
	/**
	 * 碰撞检测处理
	 * @param gameview
	 * @param body1 碰撞者
	 * @param body2  碰撞者
	 * @param x 碰撞点x坐标
	 * @param y 碰撞点y坐标
	 * @param angle 碰撞角度
	 * @param velocity 碰撞速度 
	 */ 
	public static void doAction(
			GameView gameview,Body body1,Body body2,
			float x,float y,Vec2 angle,Vec2 velocity)
	{
		
		//遍历的时候是对非herohall的判断遍历，因为
		//每次的碰撞都是有heroball参与的，所以每次有碰撞的时候都是有heroball参与的，所以
		//应该是对非heroball的东西
		if(!gameview.heroislive)
		{
			return;
		}
		
		//*********************对撞墙的监听******************************
		//遍历木块，根据碰撞角度、速度、进行判定，播放撞墙声音
		for(MyBody mytempbody:gameview.reclist)
		{
			if(mytempbody.body==body1||mytempbody.body==body2)
			{
				float jiaoduYZ=Vec2.dot(angle, velocity)/(angle.length()*velocity.length());//获取碰撞角度
				if(
					YINXIAO_OPEN&&//音效打开
					velocity.length()>Constant.COLLISIONVELOCITY&&//对碰撞速度限制
					Math.abs(jiaoduYZ)>0.717//对碰撞角度限制						
				)
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(1, 0);//播放撞墙的声音
				}
				
				return;
			}
		}

		//********************对小球进洞（未过关）的监听******************************
		for(MyBody mytempbody:gameview.holelist)//遍历洞列表
		{
			
			if(mytempbody.body==body1||mytempbody.body==body2)
			{
				Constant.ROTEANGLE=(float)Math.toDegrees(Math.atan2(angle.y, angle.x));
				if(ZHENDONG_OPEN)//震动标志
				{
					gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震动
				}
				
				if(YINXIAO_OPEN)//音效标志
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(0, 0);//播放进洞音
				}
				gameview.heroislive=false;//会动小球停止
				mytempbody.doAction();//执行动画方法（换图&绘图）
				gameview.herolist.get(1).bitmap=null;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				//gameview接受，暂停1秒进入nextview界面
				
				gameview.DRAW_THREAD_FLAG=false;//gameview 画的线程停止
				gameview.tableBallActivity.hd.sendEmptyMessage(2);
				return;
			}
		}
		
		for(FalshHole mytempbody:gameview.falshholelist)
		{
			//对会闪的洞添加的碰撞监听
			if(mytempbody.isFlashDead//可以进洞
					&&(mytempbody.body==body1||mytempbody.body==body2))//对小球进洞的监听
			{
				Constant.ROTEANGLE=(float)Math.toDegrees(Math.atan2(angle.y, angle.x));
				if(ZHENDONG_OPEN)
				{
					gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震动
				}
				
				if(YINXIAO_OPEN)
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(0, 0);//播放进洞音
				}
				gameview.heroislive=false;//会动小球停止
				mytempbody.doAction();//执行动画方法
				gameview.herolist.get(1).bitmap=null;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				gameview.DRAW_THREAD_FLAG=false;//gameview 画的线程停止
				//gameview接受，暂停1秒进入nextview界面
				gameview.tableBallActivity.hd.sendEmptyMessage(2);
				return;
			}
		}
		//******************对过关的监听**********************
		if(gameview.herolist.get(0).body==body1||gameview.herolist.get(0).body==body2)
		{
			if(YINXIAO_OPEN)
			{
				gameview.tableBallActivity.soundutil.playEffectsSound(2, 0);
			}
			
			gameview.heroislive=false;//标记小球停止
			gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震动一下
			gameview.herolist.get(0).doAction();//执行动画
			gameview.herolist.get(1).bitmap=null;
			//更新数据库
			if
			(
					gameview.playtime.getRunTime()<DBUtil.getTimeplay(Constant.PLAY_MODEL, Constant.LEVEL)//过关时间<最短时间
					||
					DBUtil.getTimeplay(Constant.PLAY_MODEL, Constant.LEVEL)==0//0表示第一次
			)
			{
				DBUtil.upDateTime(Constant.PLAY_MODEL, Constant.LEVEL, gameview.playtime.getRunTime());
			}
			//****************解锁下一个******************
			if(DBUtil.getLock(Constant.PLAY_MODEL, Constant.LEVEL+1)==0//下一关未解锁
					&&Constant.LEVEL!=6//并且不是最后一关    TODO 人工设置关数最大值
					)
			{
				DBUtil.insert(Constant.PLAY_MODEL, Constant.LEVEL+1, 0, 1);//下一关解锁
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameview.DRAW_THREAD_FLAG=false;//gameview 画的线程停1止
			gameview.tableBallActivity.hd.sendEmptyMessage(2);//界面跳转
			return;
		}
	}
}
