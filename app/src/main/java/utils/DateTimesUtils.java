package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimesUtils {
	static SimpleDateFormat EdateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static Date  date=new Date();

	/**获取日期时间  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateTimeS()
	{
		return EdateFormat.format(new Date());
	}

	public static String  getDateCalculate(String mdate,int days)
	{

		if(!mdate.isEmpty()&&mdate!=null)
		{
			try {
				date=EdateFormat.parse(mdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		long time=date.getTime()+days*1000*60*60*24;
		return EdateFormat.format(time);
	}
}
