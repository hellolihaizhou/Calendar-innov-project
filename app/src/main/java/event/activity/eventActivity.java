package event.activity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lihaizhou.mycalendar.R;

import database.bean.eventBean;
import database.dao.eventDao;
import event.listview.eventListView;
import utils.Timeutils;


public class eventActivity extends LinearLayout implements eventDao.eventDaoCallBack,eventListView.eventListViewCallBack {

	public   eventListView mListview;
	private  eventDao eDao;
	private  LinearLayout lyout_empty;
	private Context mContext;
	public eventCallBacK mBacK=null;
	public interface eventCallBacK
	{
		void onItemListen(Intent intent);
	};

	public void setEventCallBacK(eventCallBacK BacK)
	{
		this.mBacK=BacK;
	}
	public eventActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public eventActivity(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.mContext=context;

		//View view=View.inflate(context, R.layout.event_main, this);
		LayoutInflater.from(context).inflate(R.layout.event_main, this);
		lyout_empty=(LinearLayout)findViewById(R.id.lyout_empty);
		mListview=new eventListView(this);
		mListview.setEventListViewCallBack(this);

		initData();
		
	/*     EventTimerTherad therads=new EventTimerTherad(mContext);
		 new Thread(therads).start();*/

	}


	private void initData() {

		eDao=new eventDao(mContext);
		eDao.seteventDaoCallBack(this);
		String date= Timeutils.getCurrentDate();
		show(date);
	}
	@Override
	public void query(eventBean Bean) {
		// TODO Auto-generated method stub
		mListview.AddData(Bean);
	}
	@Override
	public void setOnItemClickListener(int eid) {
		// TODO Auto-generated method stub
		eventBean eventBean=eDao.getdates(eid);
		Intent intent=new Intent();
		intent.setClass(mContext, AddeventActivity.class);
		intent.putExtra("eid", eid);
		intent.putExtra("eventBean", eventBean);
		if(mBacK!=null)
		{
			mBacK.onItemListen(intent);
		}

	}
	// mContext.startActivityForResult(intent, AddeventActivity.Activity_TAG);
	public  void Query(String date)
	{

		mListview.clear();
		show(date);

	}

	public  void show(String date)
	{
		if(eDao.getCount(date)>0)
		{
			eDao.Query(eventDao.ChooseType.INDEX,date);
			if(lyout_empty.getVisibility()==View.VISIBLE)lyout_empty.setVisibility(View.GONE);
		}
		else {
			lyout_empty.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void delete(int count) {
		// TODO Auto-generated method stub
		if(count<=0)
		{
			lyout_empty.setVisibility(View.VISIBLE);
		}
	}


	@Override
	public void add(int count) {
		// TODO Auto-generated method stub
		if(count>0)
		{
			if(lyout_empty.getVisibility()==View.VISIBLE)lyout_empty.setVisibility(View.GONE);
		}
	}






}
