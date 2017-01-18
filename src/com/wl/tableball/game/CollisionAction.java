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
 * ��ײ��Ӧ��
 * @author taozhang
 *
 */
public class CollisionAction {
	/**
	 * ��ײ��⴦��
	 * @param gameview
	 * @param body1 ��ײ��
	 * @param body2  ��ײ��
	 * @param x ��ײ��x����
	 * @param y ��ײ��y����
	 * @param angle ��ײ�Ƕ�
	 * @param velocity ��ײ�ٶ� 
	 */ 
	public static void doAction(
			GameView gameview,Body body1,Body body2,
			float x,float y,Vec2 angle,Vec2 velocity)
	{
		
		//������ʱ���ǶԷ�herohall���жϱ�������Ϊ
		//ÿ�ε���ײ������heroball����ģ�����ÿ������ײ��ʱ������heroball����ģ�����
		//Ӧ���ǶԷ�heroball�Ķ���
		if(!gameview.heroislive)
		{
			return;
		}
		
		//*********************��ײǽ�ļ���******************************
		//����ľ�飬������ײ�Ƕȡ��ٶȡ������ж�������ײǽ����
		for(MyBody mytempbody:gameview.reclist)
		{
			if(mytempbody.body==body1||mytempbody.body==body2)
			{
				float jiaoduYZ=Vec2.dot(angle, velocity)/(angle.length()*velocity.length());//��ȡ��ײ�Ƕ�
				if(
					YINXIAO_OPEN&&//��Ч��
					velocity.length()>Constant.COLLISIONVELOCITY&&//����ײ�ٶ�����
					Math.abs(jiaoduYZ)>0.717//����ײ�Ƕ�����						
				)
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(1, 0);//����ײǽ������
				}
				
				return;
			}
		}

		//********************��С�������δ���أ��ļ���******************************
		for(MyBody mytempbody:gameview.holelist)//�������б�
		{
			
			if(mytempbody.body==body1||mytempbody.body==body2)
			{
				Constant.ROTEANGLE=(float)Math.toDegrees(Math.atan2(angle.y, angle.x));
				if(ZHENDONG_OPEN)//�𶯱�־
				{
					gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//��
				}
				
				if(YINXIAO_OPEN)//��Ч��־
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(0, 0);//���Ž�����
				}
				gameview.heroislive=false;//�ᶯС��ֹͣ
				mytempbody.doAction();//ִ�ж�����������ͼ&��ͼ��
				gameview.herolist.get(1).bitmap=null;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				//gameview���ܣ���ͣ1�����nextview����
				
				gameview.DRAW_THREAD_FLAG=false;//gameview �����߳�ֹͣ
				gameview.tableBallActivity.hd.sendEmptyMessage(2);
				return;
			}
		}
		
		for(FalshHole mytempbody:gameview.falshholelist)
		{
			//�Ի����Ķ���ӵ���ײ����
			if(mytempbody.isFlashDead//���Խ���
					&&(mytempbody.body==body1||mytempbody.body==body2))//��С������ļ���
			{
				Constant.ROTEANGLE=(float)Math.toDegrees(Math.atan2(angle.y, angle.x));
				if(ZHENDONG_OPEN)
				{
					gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//��
				}
				
				if(YINXIAO_OPEN)
				{
					gameview.tableBallActivity.soundutil.playEffectsSound(0, 0);//���Ž�����
				}
				gameview.heroislive=false;//�ᶯС��ֹͣ
				mytempbody.doAction();//ִ�ж�������
				gameview.herolist.get(1).bitmap=null;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				gameview.DRAW_THREAD_FLAG=false;//gameview �����߳�ֹͣ
				//gameview���ܣ���ͣ1�����nextview����
				gameview.tableBallActivity.hd.sendEmptyMessage(2);
				return;
			}
		}
		//******************�Թ��صļ���**********************
		if(gameview.herolist.get(0).body==body1||gameview.herolist.get(0).body==body2)
		{
			if(YINXIAO_OPEN)
			{
				gameview.tableBallActivity.soundutil.playEffectsSound(2, 0);
			}
			
			gameview.heroislive=false;//���С��ֹͣ
			gameview.tableBallActivity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//��һ��
			gameview.herolist.get(0).doAction();//ִ�ж���
			gameview.herolist.get(1).bitmap=null;
			//�������ݿ�
			if
			(
					gameview.playtime.getRunTime()<DBUtil.getTimeplay(Constant.PLAY_MODEL, Constant.LEVEL)//����ʱ��<���ʱ��
					||
					DBUtil.getTimeplay(Constant.PLAY_MODEL, Constant.LEVEL)==0//0��ʾ��һ��
			)
			{
				DBUtil.upDateTime(Constant.PLAY_MODEL, Constant.LEVEL, gameview.playtime.getRunTime());
			}
			//****************������һ��******************
			if(DBUtil.getLock(Constant.PLAY_MODEL, Constant.LEVEL+1)==0//��һ��δ����
					&&Constant.LEVEL!=6//���Ҳ������һ��    TODO �˹����ù������ֵ
					)
			{
				DBUtil.insert(Constant.PLAY_MODEL, Constant.LEVEL+1, 0, 1);//��һ�ؽ���
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameview.DRAW_THREAD_FLAG=false;//gameview �����߳�ͣ1ֹ
			gameview.tableBallActivity.hd.sendEmptyMessage(2);//������ת
			return;
		}
	}
}
