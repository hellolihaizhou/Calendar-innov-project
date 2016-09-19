package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.bool;
import android.R.string;
import android.annotation.SuppressLint;


public class Timeutils {
	@SuppressLint("SimpleDateFormat")
	static Date  date=new Date();
	@SuppressLint("SimpleDateFormat")
	static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	static SimpleDateFormat CdateFormat =new SimpleDateFormat("yyyy年MM月dd日");

	@SuppressLint("SimpleDateFormat")
	static SimpleDateFormat timeFormat =new SimpleDateFormat("HH:mm");
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

	/**获取当天日期  中文格式
	 * @return
	 */
	public static String getCurrentCDate()
	{
		return  CdateFormat.format(new Date());
	}


	/** 后一个日期 是否大于等于 前一个
	 * @param sdate  前一个
	 * @param fdate  后一个
	 * @return
	 */
	public static boolean issDatecontrastfdate(String fdate,String sdate)
	{
		boolean is=false;
		try {
			long f=dateFormat.parse(fdate).getTime();
			long s=dateFormat.parse(sdate).getTime();
			if(s>=f)
			{
				is=true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;


	}
	/**当前日期是否是今天
	 * @param date
	 * @return
	 */
	public static boolean isToday(String date)
	{
		boolean is=false;
		if(getCurrentDate()==date)
		{
			is=true;
		}
		return is;
	}

	/**英文日期转中文格式
	 * @param edate  yyyy-MM-dd
	 * @return  yyyy年MM月dd日
	 */
	public static String edateToCdate(String edate)
	{	String str="";
		try {
			//	 s=dateFormat.parse(edate).getTime();
			str=CdateFormat.format(dateFormat.parse(edate).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;


	}

	/***当前日期是否大于等于今天
	 * @param date
	 * @return
	 */
	public static boolean isMoreThanToday2(String date)
	{
		boolean is=false;

		try {
			long s=dateFormat.parse(date).getTime();
			long current=dateFormat.parse(getCurrentDate()).getTime();
			if(s>=current)
			{
				is=true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	/**前期日期是否大于今天
	 * @param date
	 * @return
	 */
	public static boolean isMoreThanToday(String date)
	{
		boolean is=false;

		try {
			long s=dateFormat.parse(date).getTime();
			long current=dateFormat.parse(getCurrentDate()).getTime();
			if(s>current)
			{
				is=true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;

	}

	/**英文日期格式
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getEDate(Date date)
	{
		return dateFormat.format(date);
	}

	/**中文日期格式
	 * @param date
	 * @return  yyyy年MM月dd日
	 */
	public static String getCDate(Date date)
	{
		return CdateFormat.format(date);
	}

	/**时间
	 * @param date
	 * @return
	 */
	public static String getDateToTime(Date date)
	{
		return timeFormat.format(date);
	}

	/**当前时间
	 * @return
	 */
	public static String getcurrentTime()
	{

		return timeFormat.format(new Date());
	}

	/**时间加减分钟
	 * @param time 时间
	 * @param min 分钟
	 * @return
	 */
	public static String getMinCalculate(String time,int min)
	{
		if(!time.isEmpty()&&time!=null)
		{
			try {
				date=timeFormat.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		long s=date.getTime()+min*1000*60;
		return timeFormat.format(s);
	}

	public static Date getStringTimeToDate(String time)
	{
		Date date=new Date();
		if(!time.isEmpty()&&time!=null)
		{
			try {
				date=timeFormat.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return date;
	}

	public static boolean timeCompat(String time)
	{
		Date  date=new Date();
		boolean is=false;
		if(timeFormat.format(date).equals(time.trim()))
		{
			is=true;
		}
		return is;
	}

	/**英文日期转date
	 * @param mString  yyyy-MM-dd
	 * @return
	 */

	public static Date getStringToDate(String mString)
	{
		Date date=new Date();
		if(!mString.isEmpty()&&mString!=null)
		{
			try {
				date=dateFormat.parse(mString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return date;
	}

	/**日期增加函数
	 * @param days
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String  getDateCalculate(String mdate,int days)
	{

		if(!mdate.isEmpty()&&mdate!=null)
		{
			try {
				date=dateFormat.parse(mdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		long time=date.getTime()+days*1000*60*60*24;
		return dateFormat.format(time);
	}
}
