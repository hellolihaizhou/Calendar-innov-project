package punchclock.activity;


import java.util.Date;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.lihaizhou.mycalendar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import database.bean.punchclockBean;
import database.dao.punchclockDao;
import titlebar.DeleteView;
import titlebar.EditView;
import titlebar.ItemSwitchView;
import titlebar.ItemView;
import titlebar.TitleBarView;
import utils.Timeutils;
import utils.ToastUtils;
import utils.activityutils;


public class addPunchclockActivity extends Activity implements TitleBarView.TitleBarCallBack, ItemView.itemViewCallBack,OnTimeSelectListener, DeleteView.DeleteViewCallBack, ItemSwitchView.ItemSwitchViewCallBack {

	private ItemView item_startdate,item_enddate,item_repetition,item_time;


	private TimePickerView  mTimePickerView;

	private TitleBarView mTitleBar;

	private EditView editView_title;

	private ItemSwitchView itemswitch_time;

	private DeleteView  delete_view;
	private RadioGroup choose_color;
	private  String startdate="",enddate="",time="",weeks="7";

	public final static  int REMIND_TAD=31313131;

	public int ImageId=0;
	public punchclockDao pdao;

	public int[] rid=new int[]{R.id.red_ball,R.id.yellow_ball,R.id.green_ball,R.id.blue_ball,R.id.purple_ball};
	public RadioButton[] rbtn=new RadioButton[rid.length];
	private  int pid=-1;

	private boolean is_remind=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addpunchclock_main);
		initView();
		initData();

	}

	/**
	 *
	 */
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		//System.out.println("Flags----->"+	intent.getFlags());
		pdao=new punchclockDao(getApplicationContext());
		pid=intent.getIntExtra("pid", -1);
		//System.out.println("pid--->"+pid);
		if(pid!=-1) //更新可以进行更新或者删除操作
		{

			mTitleBar.setTitle("打卡详情");
			Bundle bundle=intent.getBundleExtra("data");
			punchclockBean pBean=(punchclockBean) bundle.getSerializable("pbean");
			if(pBean!=null)
			{
				is_remind=pBean.isIsremind();
				delete_view=(DeleteView)findViewById(R.id.delete_view);
				delete_view.setDeleteViewCallBack(this);
				delete_view.setVisibility(View.VISIBLE);
				for (int i = 0; i < rid.length; i++) {
					rbtn[i]=(RadioButton) findViewById(rid[i]);
				}

				editView_title.setText(pBean.getTitle());
				startdate=pBean.getStartdate();
				enddate=pBean.getEnddate();
				time=pBean.getTime();
				weeks=pBean.getRepetition();
				ImageId=pBean.getImageId();

				String str="";

				if(weeks.equals("7"))
				{
					str="每天";
				}
				else if(weeks.length()==1) {
					str=remindActivity.item[Integer.parseInt(weeks)];
				}
				else {
					String[] items=weeks.split(",");
					int size=items.length;
					for (int i = 0; i < items.length; i++) {
						if(i==size-1)
						{
							str+=remindActivity.item[Integer.parseInt(items[i])];

						}
						else
						{
							str+=remindActivity.item[Integer.parseInt(items[i])]+",";
						}
					}
				}
				rbtn[ImageId].setChecked(true);
				item_repetition.setValue(str);
				item_startdate.setValue(Timeutils.edateToCdate(startdate));
				item_enddate.setValue(Timeutils.edateToCdate(enddate));

				itemswitch_time.setValue(is_remind);
				if(is_remind)
				{
					item_time.setVisibility(View.VISIBLE);
					item_time.setValue(time);
				}
			}
		}
		else {
			time=Timeutils.getMinCalculate(Timeutils.getcurrentTime(), 30);
			item_time.setValue(time);
			startdate=Timeutils.getCurrentDate();
			item_startdate.setValue(Timeutils.getCurrentCDate());
		}



	}

	private void initView() {
		// TODO Auto-generated method stub
		mTitleBar=(TitleBarView)findViewById(R.id.titlebar);
		mTitleBar.setTitleBarCallBack(this);

		editView_title=(EditView)findViewById(R.id.editView_title);

		item_startdate=(ItemView)findViewById(R.id.item_startdate);
		item_startdate.mfindViewById(R.id.item_startdate);
		item_startdate.setitemViewCallBack(this);

		item_enddate=(ItemView)findViewById(R.id.item_enddate);
		item_enddate.mfindViewById(R.id.item_enddate);
		item_enddate.setitemViewCallBack(this);



		item_repetition=(ItemView)findViewById(R.id.item_repetition);
		item_repetition.mfindViewById(R.id.item_repetition);
		item_repetition.setitemViewCallBack(this);

		item_time=(ItemView)findViewById(R.id.item_time);
		item_time.mfindViewById(R.id.item_time);
		item_time.setitemViewCallBack(this);


		itemswitch_time=(ItemSwitchView)findViewById(R.id.itemswitch_time);
		itemswitch_time.mfindViewById(R.id.itemswitch_time);
		itemswitch_time.setItemSwitchViewCallBack(this);

		choose_color=(RadioGroup)findViewById(R.id.choose_color);
		choose_color.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < rid.length; i++) {
					if(checkedId==rid[i])
					{
						ImageId=i;
					}
				}
			}
		});


	}
	@Override
	public void onTimeSelect(Date date) {  //选择控件回调
		// TODO Auto-generated method stub
		String str=mTimePickerView.getTitle();

		if(str.equals(getString(R.string.startdate)))
		{
			startdate=Timeutils.getEDate(date);
			item_startdate.setValue(Timeutils.getCDate(date));
		}
		else if (str.equals(getString(R.string.enddate))) {
			enddate=Timeutils.getEDate(date);
			item_enddate.setValue(Timeutils.getCDate(date));
		}
		else {
			time=Timeutils.getDateToTime(date);
			item_time.setValue(time);
		}

	}
	@Override
	public void valueOnclick(int id) {
		if(id==R.id.item_startdate)//开始时间
		{
			settingDate(getString(R.string.startdate),Type.YEAR_MONTH_DAY,Timeutils.getStringToDate(startdate));
			mTimePickerView.show();
		}
		if(id==R.id.item_enddate) //结束时间
		{
			if(startdate!=""&&enddate=="")
			{
				enddate=Timeutils.getDateCalculate(startdate, 5);
			}
			settingDate(getString(R.string.enddate),Type.YEAR_MONTH_DAY,Timeutils.getStringToDate(enddate));
			mTimePickerView.show();
		}
		if(id==R.id.item_repetition) //重复
		{
			activityutils.launchActivityForResult(this,remindActivity.class,REMIND_TAD);
		}
		if(id==R.id.item_time) //提醒
		{
			settingDate(getString(R.string.time),Type.HOURS_MINS,Timeutils.getStringTimeToDate(time));
			mTimePickerView.show();
		}
	}
	@Override
	public void RightOnclick() {
		String title=editView_title.getText();
		String str="";
		if(title.isEmpty())
		{
			str="◎输入打卡标题!";
		}
		if(startdate.isEmpty())
		{
			str+="\n◎请选择开始日期!";
		}
		if(enddate.isEmpty())
		{
			str+="\n◎请选择结束日期!";
		}
		if(!startdate.isEmpty()&&!Timeutils.isMoreThanToday2(startdate))
		{
			str+="\n◎选择开始日期必须大于等于今天!";
		}
		if(!startdate.isEmpty()&&!enddate.isEmpty()&&!Timeutils.issDatecontrastfdate(startdate, enddate))
		{
			str+="\n◎选择结束日期必须大于等于开始日期!";
		}

		if(!str.isEmpty())
		{

			ToastUtils.toastMsg(getApplicationContext(), str, 2);
		}
		else {
			//   System.out.println("weeks--->"+weeks);
			punchclockBean bean=new punchclockBean(ImageId, title, startdate, enddate, weeks, time,is_remind);
			if(pid==-1) //添加操作
			{
				int pid=(int) pdao.add(bean);
				if(pid>=0)
				{
					bean.setPid(pid);
					bean.setRepetition(item_repetition.getValue());
					Intent intent=getIntent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("pbean", bean);
					intent.putExtras(bundle);
					setResult(punchclockActivity.add_TAG, intent);

					punchclockActivity.operate=1;
					finish();

				}

			}
			else {  //更新操作
				bean.setPid(pid);

				if(pdao.update(bean)==1)
				{

					bean.setRepetition(item_repetition.getValue());
					Intent intent=getIntent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("pbean", bean);
					intent.putExtras(bundle);
					setResult(punchclockActivity.Update, intent);
					punchclockActivity.operate=1;
					finish();
					/* showPunch.mBack.update();*/
				}

			}



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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==REMIND_TAD)
		{
			weeks=data.getStringExtra("weeks");
			String str=data.getStringExtra("remind");
			item_repetition.setValue(str);
		}
	}

	@Override
	public void deleteOnClick() {
		// TODO Auto-generated method stub
		if(pdao.delete(pid)==1)
		{
			Intent intent=getIntent();
			setResult(punchclockActivity.delete, intent);
			punchclockActivity.operate=1;
		}
		else {
			ToastUtils.toastMsg(getApplicationContext(), "删除失败！");
		}
		finish();

	}

	@Override
	public void SwitchOnclick(int id, boolean is) {
		// TODO Auto-generated method stub
		if(id==R.id.itemswitch_time){
			is_remind=is;
			if(is){
				item_time.setVisibility(View.VISIBLE);
			}
			else {
				item_time.setVisibility(View.GONE);
			}
		}
	}
}
