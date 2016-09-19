package database.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.DBOpenHelper;
import database.bean.eventBean;


/**打卡
 * @author asus
 *
 */
public class record_eventDao {
	private DBOpenHelper helper=null;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象
	private String table="record_event";
	public record_eventDao(Context context)// 定义构造函数
	{
		if(helper==null)
		{
			helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
		}

	}
	public long add(eventBean bean)
	{
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		ContentValues cv = new ContentValues();
		cv.put("eid", bean.getEid());
		cv.put("edate",dateFormat.format(new Date()));
		cv.put("state",1);
		return db.insert(table, null, cv);
	}
}
