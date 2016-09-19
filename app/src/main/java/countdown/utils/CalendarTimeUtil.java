package countdown.utils;

import android.content.Context;
import android.widget.Toast;
import java.text.ParseException;
import java.util.Calendar;


/**
 * Created by lihaizhou on 16-7-18.
 */
public class CalendarTimeUtil {
    public Boolean calculateCountTime(Context context,int year, int month, int day) throws ParseException {
        Calendar cur_calendar = Calendar.getInstance();
        int cur_year = cur_calendar.get(Calendar.YEAR);
        int cur_mon = cur_calendar.get(Calendar.MONTH) + 1;
        int cur_day = cur_calendar.get(Calendar.DAY_OF_MONTH);
        Calendar set_Calendar = cur_calendar.getInstance();
        set_Calendar.set(year, month, day);
        if((year == cur_year || year < cur_year)&& (day < cur_day || day == cur_day || month < cur_mon))
        {
            Toast.makeText(context, "Set date must be later than today", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
