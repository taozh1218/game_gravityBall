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
 * 刚体工具类
 * 
 * 生成游戏中的栋、小球、木块的类
 * 
 * 定义了生成每种不同物体的方法，定义了每种刚体的自身属性，如密度、摩擦、反弹补偿等
 * 
 * @author taozhang
 * 
 */
public class Box2DUtil {

	/**
	 * 创建小球
	 * 
	 * 设置小球属性：密度、摩擦、半径、反弹补偿、位置
	 * 
	 * @param world
	 *            世界
	 * @param x
	 *            小球圆心坐标x
	 * @param y
	 *            小球圆心坐标y
	 * @param radius
	 *            小球半径
	 * @param isStatic
	 *            是否静态
	 * @param bitmap
	 *            图片对象
	 * @param gameview
	 *            游戏界面对象
	 * @return
	 */
	public static Ball createBall(World world, float x, float y, float radius,
			boolean isStatic, Bitmap bitmap, GameView gameview) {
		CircleDef ballshapdef = new CircleDef();// 设置形状
		if (isStatic) {
			ballshapdef.density = 0;
		} else {
			ballshapdef.density = 1.0f;// 密度
		}

		ballshapdef.friction = 0f;// 摩擦力
		ballshapdef.restitution = Constant.ball_restitution;// 反弹补偿
		ballshapdef.radius = radius - Constant.BODYOFF;// 半径

		BodyDef bodydef = new BodyDef();// 初始化body
		Vec2 v = new Vec2(x, y);// 圆心坐标
		bodydef.position.set(v);// 添加

		// 将小球放入世界
		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);

		// 设了一下球的初速度
		body.setMassFromShapes();// 设置质量
		return new Ball(body, bitmap, gameview);// 返回MyBall
	}

	/**
	 * 创建球洞
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
			ballshapdef.density = 1.0f;// 密度
		}

		ballshapdef.friction = 0f;// 摩擦力
		ballshapdef.restitution = Constant.ball_restitution;// 反贪补偿
		ballshapdef.radius = radius - Constant.BODYOFF;// 半径

		BodyDef bodydef = new BodyDef();// 初始化body
		Vec2 v = new Vec2(x, y);// 圆心坐标
		bodydef.position.set(v);// 添加

		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);

		// 设了一下球的初速度
		body.setMassFromShapes();// 设置质量
		return new Hole(body, bitmap, gameview);// 返回MyBall
	}

	// *********************小球与静态球洞的唯一区别，就是小球是非静态，球洞是静态*******************

	/**
	 * 创建木块
	 * 
	 * @param world
	 * @param x
	 *            中心坐标
	 * @param y
	 * @param halfweight
	 *            半宽半高
	 * @param halfHeight
	 * @param isStatic
	 *            静态标志位
	 * @param bitmap
	 * @param gameview
	 * @return
	 */
	public static Rec creatRec(World world, float x, float y, float halfweight,
			float halfHeight, boolean isStatic, Bitmap bitmap, GameView gameview) {
		PolygonDef recshap = new PolygonDef();// 多边形
		recshap.setAsBox(halfweight + Constant.BODYOFF, halfHeight
				+ Constant.BODYOFF);// 矩形
		if (isStatic) {
			recshap.density = 0;
		} else {
			recshap.density = 1.0f;// 密度
		}
		recshap.friction = 0f;// 摩擦
		recshap.restitution = 0.2f;

		BodyDef bodydef = new BodyDef();
		Vec2 v = new Vec2(x, y);
		bodydef.position.set(v);// 中心坐标

		Body body = world.createBody(bodydef);
		body.createShape(recshap);
		body.setMassFromShapes();

		return new Rec(body, bitmap, gameview);// 返回MyRec
	}

	/**
	 * 创建FalshHole（会关闭的球洞）对象，最后一个参数是它隔多长时间进行闪一下
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param radius
	 * @param isStatic
	 * @param bitmap
	 * @param timeDeadSpan
	 *            洞的不起作用时间
	 * @param timeLiveSpan
	 *            洞的起作用时间
	 * @param gameview
	 * @return
	 */
	public static FalshHole createFalshHole(World world, float x, float y,
			float radius, boolean isStatic, Bitmap bitmap, int timeDeadSpan,// 洞不起作用的时间
			int timeLiveSpan,// 洞起作用的时间
			GameView gameview) {
		CircleDef ballshapdef = new CircleDef();
		if (isStatic) {
			ballshapdef.density = 0;
		} else {
			ballshapdef.density = 1.0f;// 密度
		}

		ballshapdef.friction = 0f;// 摩擦力
		ballshapdef.restitution = Constant.ball_restitution;// 反贪补偿
		ballshapdef.radius = radius - Constant.BODYOFF;// 半径
		ballshapdef.isSensor = true;// 这样球可以穿过

		BodyDef bodydef = new BodyDef();// 初始化body
		Vec2 v = new Vec2(x, y);// 圆心坐标
		bodydef.position.set(v);// 添加

		Body body = world.createBody(bodydef);
		body.createShape(ballshapdef);
		body.setMassFromShapes();// 设置质量
		return new FalshHole(body, bitmap, timeDeadSpan, timeLiveSpan, gameview);// 返回MyBall
	}

}
