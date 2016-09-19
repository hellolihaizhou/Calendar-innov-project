package database.dao;

import java.util.ArrayList;
import java.util.List;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DBOpenHelper;
import database.bean.punchclockBean;


/**打卡
 * @author asus
 *
 */
public class punchclockDao {
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="punch";
	public  punchclockDaoCallBack mBack;

	public interface punchclockDaoCallBack
	{
		public void query(punchclockBean Bean);
	}

	public void setPunchclockDaoCallBack(punchclockDaoCallBack back)
	{
		this.mBack=back;
	}

	public punchclockDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}

	}
	public long add(punchclockBean bean)
	{

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getTitle());
		cv.put("imgindex",bean.getImageId());
		cv.put("repeat",bean.getRepetition());
		cv.put("startdate",bean.getStartdate());
		cv.put("enddate",bean.getEnddate());
		cv.put("remindtime",bean.getTime());
		cv.put("pstate",1);
		cv.put("isremind",bean.isIsremind()+"");
		return db.insert(table, null, cv);
	}

	public int getCount()
	{
		int count=0;
		String sql="select count(*) as count from  "+table +" where pstate=1";

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,null);
		while (cursor.moveToNext())// 遍历所有的信息
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
		INDEX, THERD,ALL
	}//

	public void Query(ChooseType chooseType)
	{
		//	String sql="select  strftime('%Y-%m-%d','now'),* from punch  where  repeat like strftime('%w','now') or repeat='7' "+table;
		String sql="";
		if(chooseType==ChooseType.THERD)
		{
			sql="select  *,(select state from record_punch  r where r.pid=p.pid) as state"
					+ " from punch p "
					+ " where  pstate=1 and (strftime('%Y-%m-%d','now') between startdate and enddate) and   (repeat like '%'||strftime('%w','now')||'%' or repeat='7')";
		}
		if(chooseType==ChooseType.INDEX)
		{
			//	sql="select * from punch where pstate=1";

			sql="select  *,(select state from record_punch  r where r.pid=p.pid) as state"
					+ " from punch p "
					+ " where  pstate=1 and (strftime('%Y-%m-%d','now') between startdate and enddate) and   (repeat like '%'||strftime('%w','now')||'%' or repeat='7')";
		}



		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,null);
		while (cursor.moveToNext())// 遍历所有的信息
		{
			punchclockBean mBean=null;

			int pid=cursor.getInt(cursor.getColumnIndex("pid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			if(chooseType==ChooseType.INDEX)
			{

				int imgindex=cursor.getInt(cursor.getColumnIndex("imgindex"));
				int state=cursor.getInt(cursor.getColumnIndex("state"));

				mBean=new punchclockBean(imgindex,title,pid,state);

			}
			if(chooseType==ChooseType.THERD)
			{
				int state=cursor.getInt(cursor.getColumnIndex("state"));
				String time=cursor.getString(cursor.getColumnIndex("remindtime"));
				mBean=new punchclockBean(title,time, state, pid);
			}
			if(chooseType==ChooseType.ALL)
			{

				String repeat=cursor.getString(cursor.getColumnIndex("repeat"));
				String startdate=cursor.getString(cursor.getColumnIndex("startdate"));
				String enddate=cursor.getString(cursor.getColumnIndex("enddate"));
				String remindtime=cursor.getString(cursor.getColumnIndex("remindtime"));
			}

			mBack.query(mBean);
			//	System.out.println("数据查询");

		}
		cursor.close();
		// System.out.println("执行数据库查询");
	}
	public punchclockBean getdates(int pid)
	{
		String sql="select * from "+table +" where pid=?";
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{pid+""});
		punchclockBean mBean=null;
		if (cursor.moveToFirst())
		{
			String startdate=cursor.getString(cursor.getColumnIndex("startdate"));
			String enddate=cursor.getString(cursor.getColumnIndex("enddate"));
			String time=cursor.getString(cursor.getColumnIndex("remindtime"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			int imageindex=cursor.getInt(cursor.getColumnIndex("imgindex"));
			String repetition=cursor.getString(cursor.getColumnIndex("repeat"));
			//	System.out.println("------>"+cursor.getString(cursor.getColumnIndex("isremind")));
			//	boolean isremind=cursor.getString(cursor.getColumnIndex("isremind")).trim().equals("1")?true:false;
			boolean isremind=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isremind")));
			mBean=new punchclockBean(imageindex, title, startdate, enddate, repetition, time,isremind);

		}
		return mBean;

	}

	public int update(punchclockBean bean)
	{

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getTitle());
		cv.put("imgindex",bean.getImageId());
		cv.put("repeat",bean.getRepetition());
		cv.put("startdate",bean.getStartdate());
		cv.put("enddate",bean.getEnddate());
		cv.put("remindtime",bean.getTime());
		cv.put("isremind",bean.isIsremind()+"");
		return db.update(table, cv,  "pid=?", new String[]{bean.getPid()+""});
	}
	public int delete(int pid)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("pstate",0);
		return db.update(table, cv,  "pid=?", new String[]{pid+""});
/*		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("pstate",0);
		return db.delete(table, "pid=?", new String[]{pid+""});*/
	}

	public List<Integer> show(String date)
	{
 /*   	 String sql="select   DISTINCT imgindex "
					+ " from punch "
					+ " where  pstate=1 and (? between startdate and enddate) and   (repeat like '%'||strftime('%w',?)||'%' or repeat='7') "
					+ " order by pid limit 0,5";	*/
		String sql="select   DISTINCT imgindex "
				+ " from punch p , record_punch r "
				+ " where  p.pstate=1 and (? between  p.startdate and  p.enddate) and   (repeat like '%'||strftime('%w',?)||'%' or  p.repeat='7') and p.pid=r.pid and ?=r.pdate "
				+ " order by  p.pid limit 0,5";
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date,date,date});
		List<Integer> mid=new ArrayList<Integer>();
		while (cursor.moveToNext())// 遍历所有的信息
		{
			int imgindex=cursor.getInt(cursor.getColumnIndex("imgindex"));
			mid.add(imgindex);

		}
		return mid;
	}

}
