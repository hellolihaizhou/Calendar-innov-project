package event.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Handler;

import database.bean.eventBean;
import database.dao.eventDao;
import event.customAlert.CustomWindowUtils;
import utils.Timeutils;

public class EventTimerTherad implements Runnable,eventDao.eventDaoCallBack {
	private static int delayTime=1000;  //l s
	private boolean isrun=true;
	private List<eventBean> mDates=new ArrayList<eventBean>();//当日提醒列表
	//public static PunchTimerTheradCallBack mBack;
	private boolean isUpdate=false;


	private eventDao eDao;
	public void addTime(eventBean mBean)
	{
		mDates.add(mBean);
		System.out.println("添加到event执行列表");
	}

	public Context mContext;
	public CustomWindowUtils mCustomWindowUtils;
	private int i;
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			mCustomWindowUtils.showPopupWindow(mDates.get(msg.what).getTitle());
		};
	};
	public EventTimerTherad(Context context)
	{

		this.mContext=context;
		eDao=new eventDao(context);
		eDao.seteventDaoCallBack(this);
		//   record_eventDao=new record_eventDao(context);
		eDao.Query(eventDao.ChooseType.THERD, Timeutils.getCurrentDate());
		mCustomWindowUtils=new CustomWindowUtils(mContext);
		//   rPunchDao=new record_punchDao(context);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isrun) {
			try {
				Thread.sleep(delayTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(Timeutils.timeCompat("00:00")&&!isUpdate) //0点更新提醒 事件
			{
				if(mDates.size()>0)
				{
					mDates.clear();
				}
				eDao.Query(eventDao.ChooseType.THERD, Timeutils.getCurrentCDate());
				isUpdate=true;
				System.out.println("0点更新！");
			}
			else if(!Timeutils.timeCompat("00:00")){
				isUpdate=false;
			}

			if(mDates.size()>0)
			{

				for (int i = 0; i <mDates.size(); i++) {
					if(mDates.get(i).getState()==0&&Timeutils.timeCompat(mDates.get(i).getTime()))
					{

						mHandler.sendEmptyMessage(i);
						mDates.get(i).setState(1);
						System.out.println("提醒");

					}
				}
			}
		}
	}



	public void remove(int eid)
	{
		for (int i = 0; i <mDates.size(); i++) {
			if(mDates.get(i).getEid()==eid)
			{
				mDates.remove(i);
				System.out.println("event执行列表删除");
			}
		}

	}
	public void update(eventBean bean)
	{
		boolean is=false;
		for (int i = 0; i <mDates.size(); i++) {
			if(mDates.get(i).getEid()==bean.getEid())
			{
				is=true;
				mDates.set(i, bean);
				System.out.println("event执行列表更新");
			}
		}
		if(!is)
		{
			mDates.add(bean);
		}
	}
	@Override
	public void query(eventBean Bean) {
		// TODO Auto-generated method stub
		mDates.add(Bean);
	}

}
