package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	static SimpleDateFormat CdateFormat =new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	static SimpleDateFormat EdateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat mdateFormat =new SimpleDateFormat("yy/MM/dd HH:mm");


	/**英文格式  DataTime
	 * @param date
	 * @return     yyyy-MM-dd  HH:mm
	 */
	public static String getEDataTime(Date date)
	{
		return EdateFormat.format(date);
	}

	/**中文格式  DataTime
	 * @param date
	 * @return yyyy年MM月dd日  HH:mm
	 */
	public static String getCDataTime(Date date)
	{
		return CdateFormat.format(date);
	}
	/**
	 * @param datetime  yyyy-MM-dd  HH:mm
	 * @return
	 */
	public static Date getStringDateTimeToDate(String datetime)
	{
		Date date=null;
		try {
			date=EdateFormat.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}
	/**日期对比 第二个大于等于第一个
	 * @param fdate 第一个
	 * @param sdate 第二个
	 * @return
	 */
	public static boolean fDateContrastSdate(Date fdate,Date sdate)
	{
		boolean is=false;
		//	System.out.println("sdate------->"+sdate.getTime()+"---------->"+fdate.getTime());
		if(sdate.getTime()-fdate.getTime()>=0)
		{
			is=true;
		}
		return is;
	}

	public static String StringdatatimeTos(String datatime)
	{
		Date date=null;
		try {
			date=EdateFormat.parse(datatime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mdateFormat.format(date);
	}

	/**英文datetime转中文格式
	 * @param edate  yyyy-MM-dd HH:mm
	 * @return  yyyy年MM月dd日 HH:mm
	 */
	public static String edateToCdate(String edate)
	{	String str="";
		try {
			//	 s=dateFormat.parse(edate).getTime();
			str=CdateFormat.format(EdateFormat.parse(edate).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
