package database.dao;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import database.DBOpenHelper;
import database.bean.CountBean;

public class conutdownDao {
	static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="conutdown";
	public  conutdownDaoCallBack mBack;
	public interface conutdownDaoCallBack
	{
		public void query(CountBean Bean);
		public void Queryend();
	}

	public void setConutdownDaoCallBack(conutdownDaoCallBack back)
	{
		this.mBack=back;
	}

	public conutdownDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}

	}
	public long add(CountBean bean)
	{

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getCountTitle());
		cv.put("date",bean.getDate());
		cv.put("imgurl", bean.getImgurl());
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

	public void Query()
	{
		String sql="select * from "+table;

		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(sql,null);
		int i=0;
		while (cursor.moveToNext())// 遍历所有的收入信息
		{
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String date=cursor.getString(cursor.getColumnIndex("date"));
			int cid=cursor.getInt(cursor.getColumnIndex("cid"));
			String imgurl=cursor.getString(cursor.getColumnIndex("imgurl"));
			//CountBean mBean=new CountBean(title,getdifferDay(date), date,cid);
			CountBean mBean=new CountBean(cid, title, date, getdifferDay(date), imgurl);
			mBack.query(mBean);
			i+=1;
		}
		if(i>0)
		{
			mBack.Queryend();
		}

	}

	/**获取相差天数
	 * @param mdate 日期   yyyy年MM月dd日
	 * @return long
	 */
	public static long getdifferDay(String mdate)
	{
		//	String  countDateValue = new SimpleDateFormat("yyyy年MM月dd日").format(date);

		long days=0;

		try {
			long s=dateFormat.parse(mdate).getTime();
			long current=dateFormat.parse(getCurrentDate()).getTime();
			days= (s-current) / (1000 * 60 * 60 * 24);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return days;
	}
	/**获取当天日期  英文格式
	 * @return String
	 */
	public static String getCurrentDate()
	{

		return dateFormat.format(new Date());
	}

	public int delete(int cid)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		return db.delete(table, "cid=?", new String[]{cid+""});
	}

	public int Update(CountBean bean)
	{
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("title", bean.getCountTitle());
		cv.put("date",bean.getDate());
		cv.put("imgurl", bean.getImgurl());
		return db.update(table, cv, "cid=?", new String[]{bean.getCid()+""});
	}
}
