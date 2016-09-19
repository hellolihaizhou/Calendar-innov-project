package punchclock.listview;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

import com.lihaizhou.mycalendar.R;

import database.bean.punchclockBean;
import punchclock.adapter.punchcolckAdapter;

public class punchclockListView {
	public ListView mListView;
	private List<punchclockBean> mdatas=new ArrayList<punchclockBean>();
	private punchcolckAdapter mAdapter;

	public punchclockListViewCallBack mBack=null;
	public void setPunchclockListViewCallBack(punchclockListViewCallBack Back)
	{
		this.mBack=Back;
	}
	public interface punchclockListViewCallBack
	{
		public void setOnItemClickListener(int pid,int location);
	}
	public punchclockListView(Activity activity) {
		// TODO Auto-generated constructor stub
		mListView = (ListView) activity.findViewById(R.id.listview_punch);
		mAdapter =new punchcolckAdapter(activity, mdatas);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				if(mBack!=null)
				{
					mBack.setOnItemClickListener(mdatas.get(arg2).getPid(),arg2);
				}

			}
		});

	}


	public void AddData(punchclockBean baseBean)
	{

		mdatas.add(baseBean);
		mAdapter.Updatas();
	}

	public void clear()
	{
		if(!mdatas.isEmpty())
		{
			mdatas.clear();
		}
	}


	public void remove(int location) {
		// TODO Auto-generated method stub
		mdatas.remove(location);
		mAdapter.Updatas();
	}


	/**更新listView
	 * @param baseBean
	 * @param location
	 */
	public void update(punchclockBean baseBean,int location) {
		// TODO Auto-generated method stub
		mdatas.set(location, baseBean);
		mAdapter.Updatas();
	}


	public void update_state(int pid)
	{
		for (int i = 0; i < mdatas.size(); i++) {
			if(mdatas.get(i).getPid()==pid)
			{
				mdatas.get(i).setState(1);
				mAdapter.Updatas();
			}
		}
	}



}
