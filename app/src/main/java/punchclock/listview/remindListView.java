package punchclock.listview;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

import com.lihaizhou.mycalendar.R;

import punchclock.adapter.remindAdapter;
import punchclock.bean.remindBean;

public class remindListView {
	public ListView mListView;
	private List<remindBean> mdatas=new ArrayList<remindBean>();
	private remindAdapter mAdapter;
	public remindListView(Activity activity) {
		// TODO Auto-generated constructor stub
		mListView = (ListView) activity.findViewById(R.id.listview_remind);
		mAdapter =new remindAdapter(activity, mdatas);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub

				remindBean alertBean=mdatas.get(arg2);
				if(!alertBean.selectstate)
				{
					alertBean.selectstate=true;
				}
				else {
					alertBean.selectstate=false;
				}
				//   boolean is=alertBean.selectstate==false?true:false;

				mdatas.set(arg2, alertBean);
				mAdapter.Updatas();

			}
		});

	}


	public void AddData(remindBean baseBean)
	{

		mdatas.add(baseBean);
		//mAdapter.Updatas();
	}

	public void clear()
	{
		if(!mdatas.isEmpty())
		{
			mdatas.clear();
		}
	}

	public List<remindBean> getitemselect()
	{
		//	String str="每天";
		List<remindBean> item=new ArrayList<remindBean>();
		for (int i = 0; i <mdatas.size(); i++) {
			remindBean alertBean=mdatas.get(i);
			if(alertBean.selectstate)
			{
				item.add(alertBean);
			}
		}
		return item;

	}




}
