package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;


/**日期操作类
 * @author asus
 *
 */
public class DateUtils {
	static SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat MonthFormat =new SimpleDateFormat("yyyy-MM");
	static SimpleDateFormat ssdateFormat =new SimpleDateFormat("yyyy年\n MM/dd");
	static Date  date=new Date();
	/**判断当前日期是否在起始日期和结束日期中间
	 * @param startdate  起始日期
	 * @param enddate   结束日期
	 * @return
	 */
	public static boolean isSdateToEdate(String startdate,String enddate)
	{
		boolean is=false;
		try {
			long sdate=dateFormat.parse(startdate).getTime();
			long edate=dateFormat.parse(enddate).getTime();
			long mdate=new Date().getTime();
			if(edate>=mdate&&mdate>=sdate)
			{
				is=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return is;

	}

	/**获取当天是星期几
	 * @return
	 */
	public static String getWeek()
	{
		Date date=new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

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
		return ssdateFormat.format(time);
	}

	public static String getDate()
	{
		return  ssdateFormat.format(new Date());
	}


	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	public static int getDaysByYearMonth(String date_month) {

		Date date = null;
		try {
			date = MonthFormat.parse(date_month);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, date.getYear());
		a.set(Calendar.MONTH, date.getMonth() - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static String getMonth()
	{
		return MonthFormat.format(new Date());
	}

	/**月份计算 加减
	 * @param date_month
	 * @param months
	 * @return
	 */
	public static String getMonthCalculate( String date_month,int months) {

		Date date = null;
		try {
			date = MonthFormat.parse(date_month);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);

		cl.add(Calendar.MONTH,months);


		date = cl.getTime();
		return MonthFormat.format(date);
	}
}
