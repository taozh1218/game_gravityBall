package com.wl.tableball.util;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * ���ݿ�ģ��
 * @author taozhang
 *
 */
public class DBUtil {
	
	public static SQLiteDatabase createOrOpenDatabase()
	{
		SQLiteDatabase sld=null;
		try
    	{
			sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.wl.tableball.game/gamedb",
	    			null, 							//�α깤��
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //��д�����������򴴽�
	    	);	    		
			String sql="create table if not exists timerecord(model integer,gate integer,timeplay integer,lock integer);";
	    	sld.execSQL(sql);
	    	sql="create table if not exists setting(yinyue integer,yinxiao integer,zhendong integer);";
	    	sld.execSQL(sql);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return sld;
	}
	/**
	 * �ر����ݿ�ķ���
	 * @param sld
	 */
    public static void closeDatabase(SQLiteDatabase sld)
    {
    	try
    	{
	    	sld.close();     		
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    /**
     * �����ȡ��֪ͨ��Ϣ
     * @param model
     * @param gate
     * @param timeplay
     * @param lock
     */
    public static void insert(int model,int gate,int timeplay,int lock)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    			String sql="insert into timerecord values("+model+","+gate+","+timeplay+","+lock+");";
            	sld.execSQL(sql);            	
    	}  
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    }
    public static void insertSetting(int yinyue,int yinxiao,int zhendong)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="insert into setting values("+yinyue+","+yinxiao+","+zhendong+");";
        	sld.execSQL(sql);            	
		}  
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		finally
		{
			try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
		}
	}
    
    /**
     * �����ݿ��ȡ����ʱ��
     * @param model
     * @param gate
     * @return
     */
    public static Integer getTimeplay(int model,int gate)  
    {
    	SQLiteDatabase sld=null;
    	int timeplay=0;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="select timeplay from timerecord where model = "+model+"  and gate ="+gate;
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			timeplay=cur.getInt(0);
    		}
    		cur.close();        	
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return timeplay;
    }
    /**
     * ��ȡ����״̬
     * @param model ��Ϸģʽ��Constant.PLAY_MODEL��
     * @param gate �ڼ���
     * @return
     */
    public static Integer getLock(int model,int gate)  
    {
    	SQLiteDatabase sld=null;
    	int timeplay=0;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="select lock from timerecord where model = "+model+"  and gate ="+gate;
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			timeplay=cur.getInt(0);
    		}
    		cur.close();        	
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return timeplay;
    }
    
    /**
     * ����ʱ��
     * @param model
     * @param gate
     * @param timeplay
     */
    public static void upDateTime(int model,int gate,int timeplay)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="update timerecord set timeplay="+timeplay+" where model="+model+" and gate="+gate+";";
    		sld.execSQL(sql);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    }
    
    public static void updateSetting(int yinyue,int yinxiao,int zhendong)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="update setting set yinyue="+yinyue+",yinxiao="+yinxiao+",zhendong="+zhendong+";";
    		sld.execSQL(sql);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		closeDatabase(sld);
    	}
    }
    /**
     * ��ȡ���ݿ���ȫ����Ϣ
     * @return
     */
    public static ArrayList<int[]> searchAll()
    {
    	SQLiteDatabase sld=null;
    	ArrayList<int[]> list=new ArrayList<int[]>();
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="select * from timerecord";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		
    		while(cur.moveToNext())
    		{
    			int result[]=new int[4];
    			result[0]=cur.getInt(0);//֪ͨID
    			result[1]=cur.getInt(1);//֪ͨ����
    			result[2]=cur.getInt(2);//֪ͨ����
    			result[3]=cur.getInt(3);//֪ͨ��
    			list.add(result);
    		}
    		cur.close();
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return list;
    }
    public static int[] SearchSetting()
    {
    	SQLiteDatabase sld=null;
    	int result[]=new int[3];
    	try
    	{
    		sld=createOrOpenDatabase();//�����ݿ�
    		String sql="select * from setting";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
				result[0]=cur.getInt(0);//֪ͨID
				result[1]=cur.getInt(1);//֪ͨ����
				result[2]=cur.getInt(2);//֪ͨ����
    		}
    		cur.close();
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return result;
    }
}
