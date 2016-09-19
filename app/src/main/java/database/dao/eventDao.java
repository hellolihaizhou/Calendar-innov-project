package database.dao;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DBOpenHelper;
import database.bean.addressBean;
import database.bean.eventBean;

public class eventDao {
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="event";
	private addrDao aDao;
	public   eventDaoCallBack mBack;

	public interface eventDaoCallBack
	{
		public void query(eventBean Bean);
	}

	public void seteventDaoCallBack(eventDaoCallBack back)
	{
		this.mBack=back;
	}

	public eventDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}
		aDao=new addrDao(context);
	}
	public long add(eventBean bean)
	{


		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getTitle());
		cv.put("sorts",bean.getSorts());
		cv.put("starttime",bean.getStarttime());
		cv.put("endtime",bean.getEndtime());
		cv.put("time",bean.getTime());
		cv.put("allday",bean.getIsallday()+"");
		int eid=(int) db.insert(table, null, cv);
		String addr=bean.getaddressBean().getAddress();
		if(addr!="")
		{
			bean.getaddressBean().setEid(eid);
			aDao.add(bean.getaddressBean());


		}
		return eid;
	}

	public int getCount(String date)
	{
		int count=0;
		String sql="select count(*) as count from  "+table +"  where ? between strftime('%Y-%m-%d',starttime) and strftime('%Y-%m-%d',endtime)";

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date});
		if (cursor.moveToFirst())// 遍历所有的信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}
	/**
	 * @author asus
	 *
	 */
	public enum ChooseType {
		INDEX, THERD
	}//

	@SuppressLint("NewApi")
	public void Query(ChooseType chooseType,String date)
	{
		//	String sql="select  strftime('%Y-%m-%d','now'),* from punch  where  repeat like strftime('%w','now') or repeat='7' "+table;
		//String 	sql="select e.eid,sorts,title,(select addr from address a where a.eid=e.eid) as addr from event e where ? between strftime('%Y-%m-%d',starttime) and strftime('%Y-%m-%d',endtime)";
		//	System.out.println(chooseType+"----------"+date);

		String 	sql="";
		if(chooseType==ChooseType.INDEX)
		{

			sql="select e.eid,sorts,title,(select addr from address a where a.eid=e.eid) as addr"
					+ " from event e "
					+ " where ? between strftime('%Y-%m-%d',starttime) and strftime('%Y-%m-%d',endtime)";

		}
		if(chooseType==ChooseType.THERD)
		{
			sql="select e.eid,title,time,(select state from record_event a where a.eid=e.eid) as state"
					+ " from event e "
					+ " where ? between strftime('%Y-%m-%d',starttime) and strftime('%Y-%m-%d',endtime)";

		}

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date});
		while (cursor.moveToNext())// 遍历所有的信息
		{


			int eid=cursor.getInt(cursor.getColumnIndex("eid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			eventBean mBean = null;
			if(chooseType==ChooseType.INDEX)
			{
				String addr=cursor.getString(cursor.getColumnIndex("addr"));
				int sorts=cursor.getInt(cursor.getColumnIndex("sorts"));
				addressBean aBean;
				if(addr!=null)
				{
					aBean=aDao.getDatas(eid);
				}
				else {
					aBean=new addressBean();
				}
				mBean=new eventBean(eid, title, sorts, aBean);
			}

			if(chooseType==ChooseType.THERD)
			{
				String time=cursor.getString(cursor.getColumnIndex("time"));
				int state=cursor.getInt(cursor.getColumnIndex("state"));
				mBean=new eventBean(eid, title, time, state);

			}



			mBack.query(mBean);


		}
		cursor.close();
	}
	public eventBean getdates(int eid)
	{
		String sql="select * from "+table +" where eid=?";
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{eid+""});
		eventBean mBean=null;
		addressBean aBean=null;
		if(aDao.getCount(eid)>0)
		{
			aBean=aDao.getDatas(eid);
		}
		else {
			aBean=new addressBean();
		}
		//	addressBean aBean=aDao.getDatas(eid);
		if (cursor.moveToFirst())
		{
			String title=cursor.getString(cursor.getColumnIndex("title"));
			int sorts=cursor.getInt(cursor.getColumnIndex("sorts"));
			String time=cursor.getString(cursor.getColumnIndex("time"));
			String starttime=cursor.getString(cursor.getColumnIndex("starttime"));
			String endtime=cursor.getString(cursor.getColumnIndex("endtime"));
			boolean allday=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("allday")));
			mBean=new eventBean(title, sorts, starttime, endtime, time, allday, aBean);

		}
		return mBean;

	}

	public int update(eventBean bean)
	{
		String addr=bean.getaddressBean().getAddress();
		if(addr!="")
		{
			bean.getaddressBean().setEid(bean.getEid());
			aDao.update(bean.getaddressBean());
		}
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getTitle());
		cv.put("sorts",bean.getSorts());
		cv.put("starttime",bean.getStarttime());
		cv.put("endtime",bean.getEndtime());
		cv.put("time",bean.getTime());
		cv.put("allday",bean.getIsallday()+"");
		return db.update(table, cv,  " eid=? ", new String[]{bean.getEid()+""});
	}
	public int delete(int eid)
	{
		aDao.delete(eid);
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象

		return db.delete(table, "eid=?", new String[]{eid+""});
	}

	public eventBean newestrecord()
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		String sql="select * from event "
				+ "where  strftime('%Y-%m-%d','now') between strftime('%Y-%m-%d',starttime) and strftime('%Y-%m-%d',endtime) "
				+ " order by time desc" ;
		Cursor cursor = db.rawQuery(sql,null);
		eventBean mBean=null;
		if (cursor.moveToFirst())
		{
			int eid=cursor.getInt(cursor.getColumnIndex("eid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String time=cursor.getString(cursor.getColumnIndex("time"));
	/*		String starttime=cursor.getString(cursor.getColumnIndex("starttime"));
			String endtime=cursor.getString(cursor.getColumnIndex("endtime"));
            boolean allday=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("allday")));
            	int sorts=cursor.getInt(cursor.getColumnIndex("sorts"));

            */
			mBean=new eventBean(eid, title, time);

		}

		return mBean;
	}
}
