package database.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class record_punchDao {
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="record_punch";
	public  punchclockDaoCallBack mBack;
	public interface punchclockDaoCallBack
	{
		public void query(punchclockBean Bean);
	}

	public void setPunchclockDaoCallBack(punchclockDaoCallBack back)
	{
		this.mBack=back;
	}

	public record_punchDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}

	}
	public long add(punchclockBean bean)
	{
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("pid", bean.getPid());
		cv.put("pdate",dateFormat.format(new Date()));
		cv.put("state",1);
		return db.insert(table, null, cv);
	}

	public int getCount()
	{
		int count=0;
		String sql="select count(*) as count from  "+table;

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,null);
		while (cursor.moveToNext())// 遍历所有的收入信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}


	/**
	 * @author asus
	 *
	 */
	public int delete(int pid)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		return db.delete(table, " pid=? and pdate=? ", new String[]{pid+"",dateFormat.format(new Date())});
	}

	/**按日期查询打卡总数
	 * @param date 日期
	 * @return
	 */
	public int getDayCount(String date)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		int count=0;
		String sql="select  count(*) as count,(select state from record_punch  r where r.pid=p.pid) as state"
				+ " from punch p "
				+ " where    (? between startdate and enddate) and (repeat like '%'||strftime('%w',?)||'%' or repeat='7') ";

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date,date});
		while (cursor.moveToNext())// 遍历所有的收入信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}

	/**当日已完成打卡总数
	 * @param date
	 * @return
	 */
	public int getrCount(String date)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		int count=0;
		String sql="select count(*) as count from record_punch  where pdate=?";

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date});
		while (cursor.moveToNext())// 遍历所有的收入信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}

	/**根据月份获取 完成打卡总数
	 * @param date  yyyy-MM
	 * @return
	 */
	public int getMonthCount(String date)
	{
		String sql="select count(*) as count  from record_punch where strftime('%Y-%m',pdate)=?";
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		int count=0;
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,new String[]{date});
		while (cursor.moveToNext())// 遍历所有的收入信息
		{
			count=cursor.getInt(cursor.getColumnIndex("count"));
		}

		return count;
	}



}
