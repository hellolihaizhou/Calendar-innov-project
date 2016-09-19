package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;// 定义数据库版本号
	private static final String DBNAME = "MyCalendar.db";// 定义数据库名

	public DBOpenHelper(Context context){// 定义构造函数

		super(context, DBNAME, null, VERSION);// 重写基类的构造函数
	}

	@Override
	public void onCreate(SQLiteDatabase db){// 创建数据库

		db.execSQL("create table  conutdown(cid integer primary key,title text,date date,imgurl text)");//倒计时表
		db.execSQL( "create table punch(pid integer primary key, title text,imgindex int,repeat text, startdate date,enddate date,remindtime time,pstate int,isremind text)"); //打卡表  pstate 1 正常  0 删除
		db.execSQL( "create table record_punch(rid integer primary key,pid int,pdate date,state int default 0)"); //打卡记录表
		db.execSQL("create table event(eid integer primary key, title text,time text,sorts int,allday  text,starttime datetime,endtime datetime)");//活动事件表
		db.execSQL("create table address(aid integer primary key,eid int,addr text,latitude double,longitude double)");//活动事件地址表
		db.execSQL("create table record_event(rid integer primary key,eid int,edate date,state int default 0)");//活动事件记录表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)// 覆写基类的onUpgrade方法，以便数据库版本更新
	{
	}
}
