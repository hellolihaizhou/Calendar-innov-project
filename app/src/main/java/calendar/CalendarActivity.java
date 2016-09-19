package calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


import calendar.custom.ScrollableLayout;
import calendar.utils.serviceUtils;
import countdown.activity.CountMainPage;
import database.bean.eventBean;
import event.*;
import event.activity.AddeventActivity;
import event.activity.ShakeActivity;
import event.activity.eventActivity;
import event.service.eventService;

import more.moreActivty;
import punchclock.activity.punchclockActivity;
import punchclock.service.PunchService;
import utils.MySharedPreferences;
import utils.Timeutils;

import com.lihaizhou.mycalendar.R;
import com.nineoldandroids.view.ViewHelper;




/**
 * 日历显示activity
 * @param <T>
 */
public class CalendarActivity<T> extends FragmentActivity implements OnGestureListener ,View.OnClickListener,eventActivity.eventCallBacK {

    private GestureDetector gestureDetector = null;
    private CalendarAdapter mAdapter = null;
    private GridView gridView = null;
    private ScrollableLayout mScrollLayout;
    private RelativeLayout mTopLayout;
    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";

    private float location;             // 最终决定的收缩比例值
    private float currentLoction = 1f;  // 记录当天的收缩比例值
    private float selectLoction = 1f;   // 记录选择那一天的收缩比例值
    private RelativeLayout constellation_part;
    protected TextView tv_Month,tv_Year,tv_show_today;
    protected LinearLayout lyout_count,lyout_punch,lyout_add,lyout_more;
    
    
    private ShakeActivity mShakeActivity;
    private MySharedPreferences mPreferences;
 
    private eventActivity mEventActivity;
    
    private Context mContext;
    public Handler handler=new Handler()
    {
    	public void handleMessage(android.os.Message msg) {
    		
    	};
    };
    public CalendarActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
       
        mContext=this;
        mShakeActivity=new ShakeActivity(mContext);
        if(!serviceUtils.isServiceWork(this, "com.punchclock.service.PunchService"))
        {
        	Intent my=new Intent(this, PunchService.class);
	    	startService(my);	
	    	System.out.println("启动PunchService服务");
        }
        
        if(!serviceUtils.isServiceWork(this, "com.event.service.eventService"))
        {
        	Intent my=new Intent(this, eventService.class);
	    	startService(my);	
	    	System.out.println("启动eventService服务");
        }
        PunchService.mThread.is=true;
        
        mEventActivity=(eventActivity) findViewById(R.id.eventactivty);
        mEventActivity.setEventCallBacK(this);
        gridView = (GridView) findViewById(R.id.gridview);
        tv_Month=(TextView)findViewById(R.id.tv_Month);
        tv_Year=(TextView)findViewById(R.id.tv_Year);
        tv_show_today=(TextView)findViewById(R.id.tv_show_today);
        tv_show_today.setOnClickListener(this);
    //    constellation_part=(RelativeLayout)findViewById(R.id.constellation_part);
        lyout_count=(LinearLayout)findViewById(R.id.lyout_count);
        lyout_count.setOnClickListener(this);
        lyout_punch=(LinearLayout)findViewById(R.id.lyout_punch);
        lyout_punch.setOnClickListener(this);
        lyout_add=(LinearLayout)findViewById(R.id.lyout_add);
        lyout_add.setOnClickListener(this);
        
        lyout_more=(LinearLayout)findViewById(R.id.lyout_more);
        lyout_more.setOnClickListener(this);
        
  
        // TODO 计算当天的位置和收缩比例
        SpecialCalendar calendar = new SpecialCalendar();
        boolean isLeapYear = calendar.isLeapYear(year_c);
        int days = calendar.getDaysOfMonth(isLeapYear,month_c);
        int dayOfWeek = calendar.getWeekdayOfMonth(year_c,month_c);
        int todayPosition = day_c;
        if (dayOfWeek != 7){
            days = days + dayOfWeek;
            todayPosition += dayOfWeek -1;
        }else{
            todayPosition -= 1;
        }
        /**
         * 如果 少于或者等于35天显示五行 多余35天显示六行
         * 五行: 收缩比例是：0.25，0.5，0.75，1
         * 六行: 收缩比例是：0.2，0.4，0.6，0.8，1
         */
        if (days <= 35){
            Constants.scale = 0.25f;
            currentLoction = (4 - todayPosition/7) * Constants.scale;
        }else{
            Constants.scale = 0.2f;
            currentLoction = (5 - todayPosition/7) * Constants.scale;
        }
        location = currentLoction;
        mTopLayout = (RelativeLayout) findViewById(R.id.rl_head);
        mScrollLayout = (ScrollableLayout)findViewById(R.id.scrollableLayout);
        mScrollLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {

                ViewHelper.setTranslationY(mTopLayout, currentY * location);
            }
        });

  
        mScrollLayout.getHelper().setCurrentContainer(constellation_part);
        gestureDetector = new GestureDetector(this);
        mAdapter = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
		 mPreferences=new MySharedPreferences(getApplication(), "sp_setting");
		
		//ispunch=mPreferences.getBoolean("punch", true);
        mAdapter.setIsPunch(mPreferences.getBoolean("punch", true));
        mShakeActivity.setIsShake(mPreferences.getBoolean("Shake", false));
        addGridView();
        gridView.setAdapter(mAdapter);

     //   gridView.setAdapter(mAdapter);
        tv_show_today.setText(mAdapter.getShowDay());
        tv_Month.setText( mAdapter.getShowMonth()+"月");
        tv_Year.setText( mAdapter.getShowYear()+"年");

    }

    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            //像左滑动
            jumpMonth++;     //下一个月
            upDateView();
        	tv_show_today.setEnabled(true);
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            jumpMonth--;     //上一个月
            upDateView();
            tv_show_today.setEnabled(true);
            return true;
        }
        return false;
    }

    private void upDateView(){
        addGridView();   //添加一个gridView
        mAdapter = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(mAdapter);
        tv_Month.setText( mAdapter.getShowMonth()+"月");
        tv_Year.setText( mAdapter.getShowYear()+"年");
        
        String m=mAdapter.getShowMonth();
        String month=m.length()==1?"0"+m:m;
        String str=mAdapter.getShowYear()+"-"+month;
        SimpleDateFormat sss=new SimpleDateFormat("yyyy-MM");
        if(str.equals(sss.format(new Date())))//本月
        {
        	 mEventActivity.Query(Timeutils.getCurrentDate());
        }
        else {
        	 mEventActivity.Query(str+"-01");	
		}
       
    }

   
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return this.gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }



    //添加gridview
    private void addGridView() {

        // TODO 如果滑动到其他月默认定位到第一行，划回本月定位到当天那行
        if (jumpMonth == 0){
            location = currentLoction;
        }else{
            location = 1f;
        }
        // TODO 选择的月份 定位到选择的那天
        if (((jumpMonth + month_c)+"").equals(Constants.zMonth)){
            location = selectLoction;
        }
        Log.d("location", "location == " + location + "   currentLoction == " + currentLoction);

        gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return CalendarActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = mAdapter.getStartPositon();
                int endPosition = mAdapter.getEndPosition();
                String scheduleDay;
                String scheduleYear;
                String scheduleMonth;
                location = (float) ((5 - position/7) * 0.2);
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    scheduleDay = mAdapter.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    //String scheduleLunarDay = mAdapter.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
                    scheduleYear = mAdapter.getShowYear();
                    scheduleMonth = mAdapter.getShowMonth();
                    Constants.zYear = scheduleYear;
                    Constants.zMonth = scheduleMonth;
                    Constants.zDay = scheduleDay;

                    if (Constants.scale == 0.2f){
                        location = (5 - position/7) * Constants.scale;
                    }else{
                        location = (4 - position/7) * Constants.scale;
                    }
                    selectLoction = location;
                    mAdapter.notifyDataSetChanged();
                    String month=scheduleMonth.length()==1?"0"+scheduleMonth:scheduleMonth;
                    String day=scheduleDay.length()==1?"0"+scheduleDay:scheduleDay;
                    String date=scheduleYear + "-" + month + "-" + day;
                 //   System.out.println("点击日期----》"+date);
                    mEventActivity.Query(date);
                    // String
                    //Toast.makeText(CalendarActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    @Override
    public void onClick(View v) {
         if(mPreferences.getBoolean("Shake", false))
         {
        	 mShakeActivity.stop();
         }
    
    	switch (v.getId()) {
    	case R.id.tv_show_today:
    		if(jumpMonth!=0)
    		tv_show_today.setEnabled(false);
    	      jumpMonth=0;     
              upDateView();
            //  mAdapter.notifyDataSetChanged();
    		break;
case R.id.lyout_count:
	//CountMainPage.goToActivity(this);
	 launchActivityForResult(this,CountMainPage.class,CountMainPage.Activity_TAG);
	break;
 case R.id.lyout_punch:
	 launchActivityForResult(this,punchclockActivity.class,punchclockActivity.Activity_TAG);
//	 goToAcivity(punchclockActivity.get,punchclockActivity.Activity_TAG);
	 break;
   case R.id.lyout_add:
	   launchActivityForResult(this,AddeventActivity.class,AddeventActivity.Activity_TAG);
	   break;
   case R.id.lyout_more:
	   launchActivityForResult(this, moreActivty.class,moreActivty.Activity_TAG);
	   break;
default:
	break;
}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 页面被销毁时，清空选择的日期数据
        PunchService.mThread.is=false;
        Constants.zYear = "";
        Constants.zMonth ="";
        Constants.zDay = "";
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
		Intent intent = new Intent(context, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		context.startActivityForResult(intent, requestCode);
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	 //   mShakeActivity=new ShakeActivity(getApplicationContext());
	//	System.out.println("resultCode------>  "+resultCode);
		   if(mPreferences.getBoolean("Shake", false))
	         {
	        	 mShakeActivity.start();
	         }
		if(resultCode==moreActivty.Activity_TAG)
		{
		   mAdapter.setIsPunch(data.getBooleanExtra("ispunch", true));
		   mAdapter.update();
		}
		if(resultCode==AddeventActivity.Activity_TAG)
		{
			String operate=data.getStringExtra("operate");
		//	System.out.println("operate---->"+operate);
			if(operate.equals("add")){
				mEventActivity.mListview.AddData((eventBean) data.getSerializableExtra("eBean"));
			}else if(operate.equals("update")){
				mEventActivity.mListview.update((eventBean) data.getSerializableExtra("eBean"));
			}else if(operate.equals("delete")){
				mEventActivity.mListview.remove(data.getIntExtra("eid", -1));
			}
		
		}
	
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("---------->onRestart");
		super.onRestart();
	}
      
	@Override
	public void onItemListen(Intent intent) {
		// TODO Auto-generated method stub
		 startActivityForResult(intent, AddeventActivity.Activity_TAG);
	}
}