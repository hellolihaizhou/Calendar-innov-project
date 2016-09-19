package punchclock.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lihaizhou.mycalendar.R;

import java.util.List;

import punchclock.bean.remindBean;
import punchclock.listview.remindListView;
import titlebar.TitleBarView;

public class remindActivity extends Activity implements TitleBarView.TitleBarCallBack {
	private TitleBarView mTitleBar;
	private remindListView mListview;
	public static String[] item=new String[]{"周日","周一","周二","周三","周四","周五","周六"};
	private int[] index=new int[]{0,1,2,3,4,5,6};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_main);
		initView();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mListview=new remindListView(remindActivity.this);
		for (int i = 0; i < item.length; i++) {
			remindBean baseBean=new remindBean(item[i],false,index[i]);
			mListview.AddData(baseBean);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		mTitleBar=(TitleBarView)findViewById(R.id.titlebar_remind);
		mTitleBar.setTitleBarCallBack(this);
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		List<remindBean> mList=mListview.getitemselect();
		int size=mList.size();
		if(size>0)
		{
			String str="";
			String weeks="";
			if(size==item.length)
			{
				str="每天";
				weeks="7";
			}
			else if(size>0){
				for (int i = 0; i < size; i++) {
					if(i==size-1)
					{
						str+=mList.get(i).getText();
						weeks+=mList.get(i).getIndex();
					}
					else
					{
						str+=mList.get(i).getText()+",";
						weeks+=mList.get(i).getIndex()+",";
					}

				}

			}

			Intent intent=getIntent();
			Bundle bundle = new Bundle();
			bundle.putString("remind", str);
			bundle.putString("weeks", weeks);
			intent.putExtras(bundle);
			setResult(addPunchclockActivity.REMIND_TAD, intent);
		}

		super.finish();
	}
	@Override
	public void RightOnclick() {
		// TODO Auto-generated method stub

	}
}
