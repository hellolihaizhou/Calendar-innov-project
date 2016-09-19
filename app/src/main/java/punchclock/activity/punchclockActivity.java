package punchclock.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import database.bean.punchclockBean;
import database.dao.punchclockDao;
import database.dao.record_punchDao;
import punchclock.listview.punchclockListView;
import punchclock.service.PunchService;
import titlebar.TitleBarView;
import utils.DateUtils;
import utils.activityutils;

public class punchclockActivity extends Activity implements TitleBarView.TitleBarCallBack,punchclockDao.punchclockDaoCallBack,punchclockListView.punchclockListViewCallBack,OnClickListener{
	public static final int  Activity_TAG=12345;
	/**
	 * 添加打卡标记
	 */
	protected static final int add_TAG=1234554;

	protected static final int Update_delect_TAG=1234555;

	protected static final int Update=11111;
	public static int delete=2222222;
	private TextView tv_empty;
	private ImageButton ibnt_statistics;
	private TitleBarView mTitleBar;
	public static punchclockListView pListView;
	public punchclockDao pDao;
	public record_punchDao rDao;
	private static int count=0;

	private  int location=-1;

	private int pid;
	public static int operate=-1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.punchclock_main);
		PunchService.mThread.is_p=true;
		operate=-1;
		initView();
		initData();


	}
	private void initData() {
		// TODO Auto-generated method stub
		pListView=new punchclockListView(this);
		pListView.setPunchclockListViewCallBack(this);
		pDao=new punchclockDao(this);
		pDao.setPunchclockDaoCallBack(this);
		rDao=new record_punchDao(this);
		count=pDao.getCount();
		if(count>0)
		{
			pDao.Query(punchclockDao.ChooseType.INDEX);
		}
		else {
			tv_empty.setVisibility(View.VISIBLE);
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		mTitleBar=(TitleBarView)findViewById(R.id.mtitlebar);
		mTitleBar.setTitleBarCallBack(this);
		tv_empty=(TextView)findViewById(R.id.empty_count);
		ibnt_statistics=(ImageButton)findViewById(R.id.ibtn_statistics);
		ibnt_statistics.setOnClickListener(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("resultCode------>  "+resultCode);
		if(resultCode==add_TAG)
		{

			//	count+=1;
			//	setView(count);
			punchclockBean baseBean=(punchclockBean)data.getSerializableExtra("pbean");
			//	pListView.AddData(baseBean);
			String weeks=baseBean.getRepetition();
			if(DateUtils.isSdateToEdate(baseBean.getStartdate(), baseBean.getEnddate())) //日期对比
			{
				if(weeks.indexOf(DateUtils.getWeek().replace("星期","周"))!=-1||weeks.equals("每天")) //重复值对比
				{

					count+=1;
					setView(count);
					//	punchclockBean baseBean=(punchclockBean)data.getSerializableExtra("pbean")
					pListView.AddData(baseBean);
					if(baseBean.isIsremind())
					{
						PunchService.mThread.addTime(baseBean);
					}

					System.out.println("添加到执行列表");
				}

			}

		}
		if(resultCode==Update)//更新
		{


			punchclockBean baseBean=(punchclockBean)data.getSerializableExtra("pbean");
			//	pListView.update(baseBean,location);

			PunchService.mThread.remove(pid);
			String weeks=baseBean.getRepetition();
			if(DateUtils.isSdateToEdate(baseBean.getStartdate(), baseBean.getEnddate())) //日期对比
			{
				if(weeks.indexOf(DateUtils.getWeek().replace("星期","周"))!=-1||weeks.equals("每天")) //重复值对比
				{
					System.out.println("更新到执行列表");
					rDao.delete(pid);  //删除当天已有的打卡记录
					if(baseBean.isIsremind())
					{
						PunchService.mThread.addTime(baseBean);
					}

					pListView.update(baseBean,location);
				}

			}
		}
		if(resultCode==delete)  //删除
		{
			count-=1;
			pListView.remove(location);
			PunchService.mThread.remove(pid);
			//System.out.println("删除事件");
			setView(count);
		}
	}
	public void setView(int mcount)
	{
		mHandler.sendEmptyMessage(mcount);

	}

	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what>0)
				//	if(tv_empty.getVisibility()==View.VISIBLE){   tv_empty.setVisibility(View.GONE);}
				//if(tv_empty.getVisibility()==View.VISIBLE){   tv_empty.setVisibility(View.GONE);}
				tv_empty.setVisibility(View.GONE);
			else {
				//	if(tv_empty.getVisibility()==View.GONE){   tv_empty.setVisibility(View.VISIBLE);}
				//if(tv_empty.getVisibility()==View.GONE){   tv_empty.setVisibility(View.VISIBLE);}
				tv_empty.setVisibility(View.VISIBLE);
			}
		};

	};
	@Override
	public void RightOnclick() {
		// TODO Auto-generated method stub

		activityutils.launchActivityForResult(this,addPunchclockActivity.class,add_TAG);
	}
	@Override
	public void query(punchclockBean Bean) {
		// TODO Auto-generated method stub
		pListView.AddData(Bean);
	}

	@Override
	public void setOnItemClickListener(int pid, int location) {
		// TODO Auto-generated method stub
		this.location=location;
		this.pid=pid;
		Intent intent = new Intent(this, addPunchclockActivity.class);
		intent.putExtra("TAG", Update_delect_TAG);
		Bundle bundle=new Bundle();
		bundle.putSerializable("pbean", pDao.getdates(pid));
		intent.putExtra("data", bundle);
		intent.putExtra("pid", pid);
		startActivityForResult(intent, Update_delect_TAG);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.ibtn_statistics)
		{
			Intent intent=new Intent();
			intent.setClass(this, punchStatisticsActivity.class);
			startActivity(intent);
		}
	}
	@Override
	public void finish() {

		// TODO Auto-generated method stub
		PunchService.mThread.is_p=false;
		if(operate==1)
		{
			showPunch.mBack.update();
		}
		super.finish();
	}




}
