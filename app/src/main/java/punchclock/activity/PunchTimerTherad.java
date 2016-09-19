package punchclock.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import database.bean.punchclockBean;
import database.dao.punchclockDao;
import database.dao.record_punchDao;
import utils.Timeutils;
import utils.WindowUtils;


public class PunchTimerTherad implements Runnable,punchclockDao.punchclockDaoCallBack, WindowUtils.WindowCallBack {
	private static int delayTime=1000;  //l s
	private boolean isrun=true;
	private List<punchclockBean> mDates=new ArrayList<punchclockBean>();//当日提醒列表
	public static PunchTimerTheradCallBack mBack;
	private boolean isUpdate=false;
	private WindowUtils mWindowUtils;

	private punchclockDao pDao;
	private record_punchDao rPunchDao;

	public static boolean is=false;
	public static boolean is_p=false;

	public interface PunchTimerTheradCallBack
	{
		public void getnotification(int eid);
	}
	public static void setPunchTimerTheradCallBack(PunchTimerTheradCallBack back)
	{
		mBack=back;
	}
	public void addTime(punchclockBean mBean)
	{
		mDates.add(mBean);
	}

	public Context mContext;
	private int i;

	public Handler mhandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			mWindowUtils.showPopupWindow("您的打卡项目“"+mDates.get(msg.what).getTitle()+"”请确认是否完成");
		};
	};
	public PunchTimerTherad(Context context)
	{

		this.mContext=context;

		mWindowUtils=new WindowUtils(context);
		mWindowUtils.setBtnText("取消", "确认");
		mWindowUtils.setWindowCallBack(this);


		pDao=new punchclockDao(context);
		pDao.setPunchclockDaoCallBack(this);
		pDao.Query(punchclockDao.ChooseType.THERD);

		rPunchDao=new record_punchDao(context);
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
				pDao.Query(punchclockDao.ChooseType.THERD);
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
						mDates.get(i).setState(1);
						mhandler.sendEmptyMessage(i);
						this.i=i;
           /* 		Message message=new Message();
            		message.what=i;
            		message.obj="您的打卡项目“"+mDates.get(i).getTitle()+"”请确认是否完成";
            		mhandler.sendMessage(message);*/
						System.out.println("打卡已提醒");
					}
				}
			}
		}
	}
	@Override
	public void query(punchclockBean Bean) {
		// TODO Auto-generated method stub
		mDates.add(Bean);
	}
	@Override
	public void RightonClikck() {
		// TODO Auto-generated method stub

		punchclockBean bean=mDates.get(i);
		bean.setState(1);

		if(rPunchDao.add(bean)>0)
		{
			mDates.remove(i);
			if(is)
			{
				showPunch.mBack.update();
			}
			if(is_p)
			{
				punchclockActivity.pListView.update_state(bean.getPid());
			}

			//  mDates.set(i, bean);
		}
	}
	@Override
	public void LeftonClikck() {
		// TODO Auto-generated method stub

	/*	punchclockBean bean=mDates.get(i);
		bean.setState(-1);
		if(rPunchDao.add(bean)>0)
		{*/
		mDates.remove(i);
		//}

		// mDates.set(i, bean);
	}

	public void remove(int pid)
	{
		for (int i = 0; i <mDates.size(); i++) {
			if(mDates.get(i).getPid()==pid)
			{
				mDates.remove(i);
				System.out.println("执行列表删除");
			}
		}

	}



	public void update(punchclockBean bean)
	{
		for (int i = 0; i <mDates.size(); i++) {
			if(mDates.get(i).getPid()==bean.getPid())
			{
				mDates.set(i, bean);
				System.out.println("执行列表更新");
			}
		}
	}

}
