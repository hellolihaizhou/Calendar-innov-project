package database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DBOpenHelper;
import database.bean.addressBean;

public class addrDao {
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="address";

	public addrDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}

	}
	public long add(addressBean mbean)
	{

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("eid",mbean.getEid());
		cv.put("addr", mbean.getAddress());
		cv.put("latitude", mbean.getLatitude());
		cv.put("longitude",mbean.getLocation());

		return db.insert(table, null, cv);
	}

	public addressBean getDatas(int eid)
	{
		String sql="select * from "+table +" where eid=?";
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{eid+""});

		addressBean aBean = null;

		if (cursor.moveToFirst())
		{
			double latitude=cursor.getDouble(cursor.getColumnIndex("latitude"));
			double location=cursor.getDouble(cursor.getColumnIndex("longitude"));
			String address=cursor.getString(cursor.getColumnIndex("addr"));
			//  aBean.setDatas(latitude, location, address);
			aBean=new addressBean(latitude, location, address);
		}
		return aBean;
	}
	public int getCount(int eid)
	{
		int count=0;
		String sql="select count(*) as count from  "+table+" where eid=?";

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{eid+""});
		if(cursor.moveToFirst())// 遍历所有的信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}



	public int update(addressBean mbean)
	{
		int code=0;
		if(getCount(mbean.getEid())>0)
		{
			db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
			ContentValues cv = new ContentValues();
			cv.put("addr", mbean.getAddress());
			cv.put("latitude", mbean.getLatitude());
			cv.put("longitude",mbean.getLocation());
			code=db.update(table, cv,  "eid=?", new String[]{mbean.getEid()+""});;
		}
		else {
			if(add(mbean)>=0)
			{
				code=1;
			}
		}
		return code;
	}
	public int delete(int eid)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		return db.delete(table,"eid=?", new String[]{eid+""});
	}
}
