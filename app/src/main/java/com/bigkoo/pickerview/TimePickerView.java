package com.bigkoo.pickerview;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;



import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import com.lihaizhou.mycalendar.R;


/**
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {

    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN , YEAR_MONTH
    }

    WheelTime wheelTime;
    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;

    public TimePickerView(Context context, Type type) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //��������
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----ʱ��ת��
        final View timepickerview = findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type);

        //Ĭ��ѡ�е�ǰʱ��
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);

    }

    /**
     * ���ÿ���ѡ���ʱ�䷶Χ
     *
     * @param startYear
     * @param endYear
     */
    public void setRange(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * ����ѡ��ʱ��
     * @param date
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

//    /**
//     * ָ��ѡ�е�ʱ�䣬��ʾѡ����
//     *
//     * @param date
//     */
//    public void show(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (date == null)
//            calendar.setTimeInMillis(System.currentTimeMillis());
//        else
//            calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        wheelTime.setPicker(year, month, day, hours, minute);
//        show();
//    }

    /**
     * �����Ƿ�ѭ������
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onTimeSelect(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
        }
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }
    
    public String getTitle()
    {
    	return tvTitle.getText().toString();
    }
}
