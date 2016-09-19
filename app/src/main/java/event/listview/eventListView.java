package event.listview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

import com.lihaizhou.mycalendar.R;

import database.bean.eventBean;
import event.adapter.eventAdapter;

public class eventListView {
	public ListView mListView;
	private List<eventBean> mdatas=new ArrayList<eventBean>();
	private eventAdapter mAdapter;
	private Context mContext;
	public eventListViewCallBack mBack=null;
	private int location=-1;
	public interface eventListViewCallBack
	{
		void setOnItemClickListener(int pos);
		void delete(int count);
		void add(int count);
	}
	public void setEventListViewCallBack(eventListViewCallBack Back) {
		this.mBack=Back;
	}
	public eventListView(View view) {
		// TODO Auto-generated constructor stub
		mListView = (ListView) view.findViewById(R.id.listview_event);
		this.mContext=view.getContext();
		mAdapter =new eventAdapter(mContext, mdatas);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if(mBack!=null)
				{
					location=arg2;
					mBack.setOnItemClickListener(mdatas.get(arg2).getEid());

				}
			}
		});

	}


	public void AddData(eventBean mBean)
	{

		mdatas.add(mBean);
		mAdapter.Updatas();
		mBack.add(mdatas.size());
	}

	public void clear()
	{

		if(mdatas.size()>0)
		{
			mdatas.clear();
			mAdapter.Updatas();
		}
	}
	public void update(eventBean eBean) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mdatas.size(); i++) {

			if(mdatas.get(i).getEid()==eBean.getEid())
			{
				// System.out.println("执行列表更新");
				mdatas.set(location, eBean);
				mAdapter.Updatas();
			}

		}


	}
	public void remove(int eid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mdatas.size(); i++) {

			if(mdatas.get(i).getEid()==eid)
			{
				//  System.out.println("执行删除");
				mdatas.remove(location);
				mAdapter.Updatas();
				mBack.delete(mdatas.size());
			}

		}
	}




}
