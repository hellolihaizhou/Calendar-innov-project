package event.activity;



import java.util.Date;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.lihaizhou.mycalendar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import database.bean.addressBean;
import database.bean.eventBean;
import database.dao.eventDao;
import event.service.eventService;
import titlebar.DeleteView;
import titlebar.EditView;
import titlebar.ItemSwitchView;
import titlebar.ItemView;
import titlebar.TitleBarView;
import utils.DateTimeUtils;
import utils.DateUtils;
import utils.Timeutils;
import utils.ToastUtils;
import utils.activityutils;

public class AddeventActivity extends Activity implements ItemView.itemViewCallBack, TitleBarView.TitleBarCallBack,OnTimeSelectListener, ItemSwitchView.ItemSwitchViewCallBack, DeleteView.DeleteViewCallBack {

	public static final int Activity_TAG=3333;
	public static final int SORTS_TAD = 222;
	public static final int ADDR_TAD = 333;

	private TimePickerView  mTimePickerView;

	private ItemView item_sort,item_addr,item_starttime,item_endtime,item_time;

	private ItemSwitchView itemsview_allday;

	private TitleBarView mTitlebar;

	private EditView editview_title;

	private DeleteView deleteView;

	private int sorts=0; //分类选项下标

	private boolean allday=true;

	private String starttime="",endtime="",time="09:00";

	private eventDao eDao;
	private addressBean aBean=null;
	private int meid=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addevent_main);
		initView();
		initData();
	}
	private void initData() {
		// TODO Auto-generated method stub
		eDao=new eventDao(getApplicationContext());
		Intent intent=getIntent();
		meid=intent.getIntExtra("eid", -1);
		if(meid>=0)
		{

			mTitlebar.setTitle("活动详情");
			eventBean mBean=(eventBean) intent.getSerializableExtra("eventBean");

			this.aBean=mBean.getaddressBean();
			String addr=mBean.getaddressBean().getAddress();
			if(!addr.trim().isEmpty())
			{
				item_addr.setValue(addr);
			}


			editview_title.setText(mBean.getTitle()+"");
			allday=mBean.getIsallday();
			starttime=mBean.getStarttime();
			endtime=mBean.getEndtime();
			sorts=mBean.getSorts();


			item_sort.setValue(sortsActivity.item[sorts]);


			if(allday) //全体事件 有时间显示
			{
				time=mBean.getTime();
				item_time.setValue(time);
				item_starttime.setValue(Timeutils.edateToCdate(starttime));
				item_endtime.setValue(Timeutils.edateToCdate(endtime));
			}
			else {
				item_time.setVisibility(View.GONE);
				item_starttime.setValue(DateTimeUtils.edateToCdate(starttime));
				item_endtime.setValue(DateTimeUtils.edateToCdate(endtime));
			}


		}
		else {  //添加
			deleteView.setVisibility(View.GONE);
			aBean=new addressBean();
			starttime=Timeutils.getCurrentDate();
			item_starttime.setValue(Timeutils.getCurrentCDate());
			item_time.setValue(time);
		}
		itemsview_allday.setValue(allday);
	}
	private void initView() {
		// TODO Auto-generated method stub

		deleteView=(DeleteView)findViewById(R.id.delete_menu);
		deleteView.setDeleteViewCallBack(this);


		mTitlebar=(TitleBarView)findViewById(R.id.titlebar_event);
		mTitlebar.setTitleBarCallBack(this);

		editview_title=(EditView)findViewById(R.id.editView_title);

		item_sort=(ItemView)findViewById(R.id.item_sort);
		item_sort.setitemViewCallBack(this);
		item_sort.mfindViewById(R.id.item_sort);

		item_addr=(ItemView)findViewById(R.id.item_addr);
		item_addr.setitemViewCallBack(this);
		item_addr.mfindViewById(R.id.item_addr);


		item_starttime=(ItemView)findViewById(R.id.item_starttime);
		item_starttime.setitemViewCallBack(this);
		item_starttime.mfindViewById(R.id.item_starttime);

		item_endtime=(ItemView)findViewById(R.id.item_endtime);
		item_endtime.setitemViewCallBack(this);
		item_endtime.mfindViewById(R.id.item_endtime);


		item_time=(ItemView)findViewById(R.id.item_time);
		item_time.setitemViewCallBack(this);
		item_time.mfindViewById(R.id.item_time);

		itemsview_allday=(ItemSwitchView)findViewById(R.id.itemsview_allday);
		itemsview_allday.setItemSwitchViewCallBack(this);
		itemsview_allday.mfindViewById(R.id.itemsview_allday);



	}
	@Override
	public void valueOnclick(int id) {
		// TODO Auto-generated method stub
		if(id==R.id.item_sort)
		{
			activityutils.launchActivityForResult(this,sortsActivity.class,SORTS_TAD);
		}
		if(id==R.id.item_addr)
		{
			activityutils.launchActivityForResult(this,MapActivity.class,ADDR_TAD);
		}
		if(id==R.id.item_starttime)
		{

			if(allday)
			{
				settingDate(getString(R.string.starttime),Type.YEAR_MONTH_DAY,Timeutils.getStringToDate(starttime));
			}
			else {
				settingDate(getString(R.string.starttime),Type.ALL,new Date());
			}

		}

		if(id==R.id.item_endtime)
		{

			if(allday)
			{
				endtime=Timeutils.getDateCalculate(starttime, 3);
				settingDate(getString(R.string.endtime),Type.YEAR_MONTH_DAY,Timeutils.getStringToDate(endtime));
			}
			else {
				settingDate(getString(R.string.endtime),Type.ALL,new Date());
			}
		}

		if(id==R.id.item_time)
		{
			settingDate(getString(R.string.time),Type.HOURS_MINS,Timeutils.getStringTimeToDate(time));
		}
	}
	/**时间选择控件设置
	 * @param title 标题
	 * @param type 类型
	 * @param date
	 */
	public void settingDate(String title,Type type,Date date)
	{
		mTimePickerView= new TimePickerView(this,type);
		mTimePickerView.setTitle(title);
		mTimePickerView.setTime(date);
		mTimePickerView.setCyclic(false);
		mTimePickerView.setCancelable(true);
		mTimePickerView.setOnTimeSelectListener(this);
		mTimePickerView.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==SORTS_TAD)
		{
			sorts = data.getIntExtra("selected", 0);
			item_sort.setValue(data.getStringExtra("sorts"));
		}
		if(requestCode==ADDR_TAD)
		{
			if(data!=null)
			{
				//  System.out.println("添加地址");
				aBean=(addressBean) data.getSerializableExtra("addressBean");
				item_addr.setValue(aBean.getAddress());
			}

		}
	}
	@Override
	public void RightOnclick() {
		String title=editview_title.getText();

		String str="";
		if(title.isEmpty())
		{
			str="◎请输入活动名称!";
		}
		if(starttime.isEmpty())
		{
			str+="\n◎请选择开始时间!";
		}

		if(endtime.isEmpty())
		{
			str+="\n◎请选择结束时间!";
		}

		if(!endtime.isEmpty()&&!starttime.isEmpty())
		{
			if(allday)  //全天模式 无时间
			{
				if(!Timeutils.isMoreThanToday2(starttime)&&meid==-1)
				{
					str+="\n◎选择开始时间必须大于等于今天!";
				}
				if(!Timeutils.issDatecontrastfdate(starttime, endtime))
				{
					str+="\n◎选择结束时间必须大于等于开始时间!";
				}

			}
			else {
				if(!Timeutils.isMoreThanToday2(starttime.substring(0, 11))&&meid==-1)
				{
					str+="\n◎选择开始时间必须大于等于今天!";
				}

				if(!DateTimeUtils.fDateContrastSdate(DateTimeUtils.getStringDateTimeToDate(starttime), DateTimeUtils.getStringDateTimeToDate(endtime)))
				{
					str+="\n◎选择结束时间必须大于等于开始时间!";
				}

			}
		}

		if(str.isEmpty()) //添加数据库
		{
			if(!allday)//非全天模式
			{
				time=starttime.substring(11);
				// System.out.println("time--->"+time);
			}
			eventBean eBean=new eventBean(title, sorts, starttime, endtime, time,allday, aBean);
			if(meid>=0)//更新
			{
				eBean.setEid(meid);
				int edao_code=eDao.update(eBean);
				if(edao_code==1)
				{
					System.out.println("更新成功");

					Intent intent=getIntent();
					intent.putExtra("operate","update");
					intent.putExtra("eBean", eBean);
					setResult(Activity_TAG, intent);
					//  eventActivity.mListview.update(eBean);
					if(allday)
					{
						if(DateUtils.isSdateToEdate(eBean.getStarttime(), eBean.getEndtime())) //添加到当天的执行列表
						{

							eventService.meventThread.update(eBean);
						}
						else {
							eventService.meventThread.remove(eBean.getEid());
						}
					}
					else {
						if(DateUtils.isSdateToEdate(eBean.getStarttime().substring(0, 11).trim(), eBean.getEndtime().substring(0, 11).trim())) //添加到当天的执行列表
						{
							eventService.meventThread.update(eBean);

						}
						else {
							eventService.meventThread.remove(eBean.getEid());
						}
					}

				}
				else {
					ToastUtils.toastMsg(getApplicationContext(), "更新失败！",2);
				}

			}
			else {
				int eid=(int) eDao.add(eBean);
				if(eid>=0){
					eBean.setEid(eid);
					Intent intent=getIntent();
					intent.putExtra("operate","add");
					intent.putExtra("eBean", eBean);
					setResult(Activity_TAG, intent);
					//  eventActivity.mListview.AddData(eBean);
					if(allday)
					{
						if(DateUtils.isSdateToEdate(eBean.getStarttime(), eBean.getEndtime())) //添加到当天的执行列表
						{

							eventService.meventThread.addTime(eBean);
						}
					}
					else {
						if(DateUtils.isSdateToEdate(eBean.getStarttime().substring(0, 11).trim(), eBean.getEndtime().substring(0, 11).trim())) //添加到当天的执行列表
						{
							eventService.meventThread.addTime(eBean);

						}
					}


				}
				else {
					ToastUtils.toastMsg(getApplicationContext(), "添加失败！",2);

				}

			}
			finish();

		}
		else {
			ToastUtils.toastMsg(getApplicationContext(), str,2);
		}


	}
	@Override
	public void onTimeSelect(Date date) {
		// TODO Auto-generated method stub
		if(mTimePickerView.getTitle().equals(getString(R.string.starttime)))
		{
			if(allday) //全天
			{
				starttime=Timeutils.getEDate(date);

				item_starttime.setValue(Timeutils.getCDate(date));
			}
			else {
				starttime=DateTimeUtils.getEDataTime(date);
				item_starttime.setValue(DateTimeUtils.getCDataTime(date));
			}

		}
		else if(mTimePickerView.getTitle().equals(getString(R.string.endtime))){
			if(allday) //全天
			{
				endtime=Timeutils.getEDate(date);
				item_endtime.setValue(Timeutils.getCDate(date));
			}
			else {
				endtime=DateTimeUtils.getEDataTime(date);
				item_endtime.setValue(DateTimeUtils.getCDataTime(date));
			}
		}
		else {
			time=Timeutils.getDateToTime(date);
			item_time.setValue(time);
		}


	}
	@Override
	public void SwitchOnclick(int id, boolean is) {
		// TODO Auto-generated method stub
		if(id== R.id.itemsview_allday)
		{
			allday=is;
			if(is)
			{
				if(item_time.getVisibility()==View.GONE){item_time.setVisibility(View.VISIBLE);item_time.setValue(time);}
				starttime=starttime.substring(0, 11);
				item_starttime.setValue(item_starttime.getValue().substring(0,11));
				if(endtime!=""){
					endtime=endtime.substring(0,11).trim();
					item_endtime.setValue(item_endtime.getValue().substring(0,11));
				}


			}
			else {

				if(item_time.getVisibility()==View.VISIBLE)item_time.setVisibility(View.GONE);
				starttime=starttime+" "+ Timeutils.getcurrentTime();
				item_starttime.setValue(item_starttime.getValue()+" "+Timeutils.getcurrentTime());
				if(endtime!=""){
					endtime=endtime+" "+Timeutils.getcurrentTime();
					item_endtime.setValue(item_endtime.getValue()+" "+Timeutils.getcurrentTime());
				}

			}

		}
	}
	@Override
	public void deleteOnClick() {
		// TODO Auto-generated method stub
		if(eDao.delete(meid)==1)
		{
			eventService.meventThread.remove(meid);
			Intent intent=getIntent();
			intent.putExtra("operate","delete");
			intent.putExtra("eid", meid);
			setResult(Activity_TAG, intent);
			finish();
		}
		else {
			ToastUtils.toastMsg(getApplicationContext(), "删除失败！", 2);
		}
	}

}
