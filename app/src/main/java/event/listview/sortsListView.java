package event.listview;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

import com.lihaizhou.mycalendar.R;

import event.adapter.sortsAdapter;
import event.bean.sortsBean;

public class sortsListView {
	public ListView mListView;
	private List<sortsBean> mdatas=new ArrayList<sortsBean>();
	private sortsAdapter mAdapter;
	private int selected=-1;  //选中值下标
	public sortsListView(Activity activity) {
		// TODO Auto-generated constructor stub
		mListView = (ListView) activity.findViewById(R.id.listview_sorts);
		mAdapter =new sortsAdapter(activity, mdatas);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				if(selected>=0)
				{
					sortsBean alertBean=mdatas.get(selected);
					alertBean.selectstate=false;
					mdatas.set(selected, alertBean);


				}

				sortsBean Bean=mdatas.get(arg2);
				Bean.selectstate=true;
				mdatas.set(arg2, Bean);
				selected=arg2;


				mAdapter.Updatas();

			}
		});

	}


	public void AddData(sortsBean baseBean)
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
	public int getSelected()
	{
		return selected;

	}



}
