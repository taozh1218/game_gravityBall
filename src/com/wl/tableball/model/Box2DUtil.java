package com.wl.tableball.model;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import android.graphics.Bitmap;
import com.wl.tableball.game.GameView;
import com.wl.tableball.util.Constant;

/**
 * ���幤����
 * 
 * ������Ϸ�еĶ���С��ľ�����
 * 
 * ����������ÿ�ֲ�ͬ����ķ�����������ÿ�ָ�����������ԣ����ܶȡ�Ħ��������������
 * 
 * @author taozhang
 * 
 */
public class Box2DUtil {

	/**
	 * ����С��
	 * 
	 * ����С�����ԣ��ܶȡ�Ħ�����뾶������������λ��
	 * 
	 * @param world
	 *            ����
	 * @param x
	 *            С��Բ������x
	 * @param y
	 *            С��Բ������y
	 * @param radius
	 *            С��뾶
	 * @param isStatic
	 *            �Ƿ�̬
	 * @param bitmap
	 *            ͼƬ����
	 * @param gameview
	 *            ��Ϸ�������
	 * @return
	 */
	public static Ball createBall(World world, float x, float y, float radius,
			boolean isStatic, Bitmap bitmap, GameView gameview) {
		CircleDef ballshapdef = new CircleDef();// ������״
		if (isStatic) {
			ballshapdef.density = 0;
		} else {
			ballshapdef.density = 1.0f;// �ܶ�
		}

		ballshapdef.friction = 0f;// Ħ����
		ballshapdef.restitution = Constant.ball_restitution;// ��������
		ballshapdef.radius = radius - Constant.BODYOFF;// �뾶

		BodyDef bodydef = new BodyDef();// ��ʼ��body
		Vec2 v = new Vec2(x, y);// Բ������
		bodydef.position.set(v);// ���

		// ��С���������
		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);

		// ����һ����ĳ��ٶ�
		body.setMassFromShapes();// ��������
		return new Ball(body, bitmap, gameview);// ����MyBall
	}

	/**
	 * ������
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param radius
	 * @param isStatic
	 * @param bitmap
	 * @param gameview
	 * @return
	 */
	public static Hole createHole(World world, float x, float y, float radius,
			boolean isStatic, Bitmap bitmap, GameView gameview) {
		CircleDef ballshapdef = new CircleDef();
		if (isStatic) {
			ballshapdef.density = 0;
		} else {
			ballshapdef.density = 1.0f;// �ܶ�
		}

		ballshapdef.friction = 0f;// Ħ����
		ballshapdef.restitution = Constant.ball_restitution;// ��̰����
		ballshapdef.radius = radius - Constant.BODYOFF;// �뾶

		BodyDef bodydef = new BodyDef();// ��ʼ��body
		Vec2 v = new Vec2(x, y);// Բ������
		bodydef.position.set(v);// ���

		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);

		// ����һ����ĳ��ٶ�
		body.setMassFromShapes();// ��������
		return new Hole(body, bitmap, gameview);// ����MyBall
	}

	// *********************С���뾲̬�򶴵�Ψһ���𣬾���С���ǷǾ�̬�����Ǿ�̬*******************

	/**
	 * ����ľ��
	 * 
	 * @param world
	 * @param x
	 *            ��������
	 * @param y
	 * @param halfweight
	 *            �����
	 * @param halfHeight
	 * @param isStatic
	 *            ��̬��־λ
	 * @param bitmap
	 * @param gameview
	 * @return
	 */
	public static Rec creatRec(World world, float x, float y, float halfweight,
			float halfHeight, boolean isStatic, Bitmap bitmap, GameView gameview) {
		PolygonDef recshap = new PolygonDef();// �����
		recshap.setAsBox(halfweight + Constant.BODYOFF, halfHeight
				+ Constant.BODYOFF);// ����
		if (isStatic) {
			recshap.density = 0;
		} else {
			recshap.density = 1.0f;// �ܶ�
		}
		recshap.friction = 0f;// Ħ��
		recshap.restitution = 0.2f;

		BodyDef bodydef = new BodyDef();
		Vec2 v = new Vec2(x, y);
		bodydef.position.set(v);// ��������

		Body body = world.createBody(bodydef);
		body.createShape(recshap);
		body.setMassFromShapes();

		return new Rec(body, bitmap, gameview);// ����MyRec
	}

	/**
	 * ����FalshHole����رյ��򶴣��������һ�������������೤ʱ�������һ��
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param radius
	 * @param isStatic
	 * @param bitmap
	 * @param timeDeadSpan
	 *            ���Ĳ�������ʱ��
	 * @param timeLiveSpan
	 *            ����������ʱ��
	 * @param gameview
	 * @return
	 */
	public static FalshHole createFalshHole(World world, float x, float y,
			float radius, boolean isStatic, Bitmap bitmap, int timeDeadSpan,// ���������õ�ʱ��
			int timeLiveSpan,// �������õ�ʱ��
			GameView gameview) {
		CircleDef ballshapdef = new CircleDef();
		if (isStatic) {
			ballshapdef.density = 0;
		} else {
			ballshapdef.density = 1.0f;// �ܶ�
		}

		ballshapdef.friction = 0f;// Ħ����
		ballshapdef.restitution = Constant.ball_restitution;// ��̰����
		ballshapdef.radius = radius - Constant.BODYOFF;// �뾶
		ballshapdef.isSensor = true;// ��������Դ���

		BodyDef bodydef = new BodyDef();// ��ʼ��body
		Vec2 v = new Vec2(x, y);// Բ������
		bodydef.position.set(v);// ���

		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);
		body.setMassFromShapes();// ��������
		return new FalshHole(body, bitmap, timeDeadSpan, timeLiveSpan, gameview);// ����MyBall
	}

}
