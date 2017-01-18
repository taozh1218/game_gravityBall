package com.wl.tableball.util;

enum ScreenOrien
{
	HP,  //��ʾ������ö��ֵ
	SP   //��ʾ������ö��ֵ
}

/**
 * ���ż���Ľ��
 * @author taozhang
 *
 */
public class ScreenScaleResult
{
	/**
	 * ���Ͻ�X����
	 */
	public int lucX;
	/**
	 * ���Ͻ�y����
	 */
	public  int lucY;
	/**
	 * ���ű���
	 */
	public  float ratio;
	/**
	 * ���������	
	 */
	public  ScreenOrien so;
	
	public ScreenScaleResult(int lucX,int lucY,float ratio,ScreenOrien so)
	{
		this.lucX=lucX;
		this.lucY=lucY;
		this.ratio=ratio;
		this.so=so;
	}
	
	public String toString()
	{
		return "lucX="+lucX+", lucY="+lucY+", ratio="+ratio+", "+so;
	}
}