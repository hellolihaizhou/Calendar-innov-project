package countdown.listview;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lihaizhou.mycalendar.R;

import countdown.Adapter.CountListAdapter;
import database.bean.CountBean;


public class CountListView  extends Activity{


	public Activity mActivity;
	public ListView mListView;
	private List<CountBean> mdatas=new ArrayList<CountBean>();
	private CountListAdapter mAdapter;
	public ListViewCallBack mBack;
	public interface ListViewCallBack
	{
		void onItemListen(CountBean mBean,int location);
	};
	public void setListViewCallBack(ListViewCallBack Back)
	{
		this.mBack=Back;
	}

	public CountListView(Activity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity=activity;
		mListView = (ListView)this.mActivity.findViewById(R.id.displayCountLv);
		mAdapter =new CountListAdapter(activity, mdatas);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				mBack.onItemListen(mdatas.get(arg2),arg2);
			}
		});
/*		mListView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});*/
/*		mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
		         menu.add(0,1,0,"设置封面");
                 menu.add(0,2,0,"删除");

			}

		});*/


		//registerForContextMenu(mListView);
	}
/*	  @Override
	    public void onCreateContextMenu(ContextMenu menu, View v,
	            ContextMenuInfo menuInfo) {
	         //   menu.setHeaderTitle("人物简介");
	            //添加菜单项
	            menu.add(0,1,0,"设置封面");
                menu.add(0,2,0,"删除");
                CountMainPage.MENU_TAG="list";
	        super.onCreateContextMenu(menu, v, menuInfo);  
	    } */

	public void AddData(CountBean baseBean)
	{
		mdatas.add(baseBean);
		mAdapter.Updatas();
	}
	public void AddData(int location,CountBean baseBean)
	{
		mdatas.add(location,baseBean);
		mAdapter.Updatas();
	}
	public void Remove(int cid)
	{
		for (int i = 0; i < mdatas.size(); i++) {

			if(mdatas.get(i).getCid()==cid)
			{
				mdatas.remove(i);
				mAdapter.Updatas();
			}
		}
	}
	public CountBean getData(int location)
	{
		return mdatas.get(location);
	}

	public void Update(CountBean bean) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mdatas.size(); i++) {

			if(mdatas.get(i).getCid()==bean.getCid())
			{
				mdatas.set(i, bean);
				mAdapter.Updatas();
			}
		}
	}

	public CountBean getLocationData(int i)
	{

		return mdatas.get(i);
/*	   for (int i = 0; i < mdatas.size(); i++) {
		if(mdatas.get(i).getCid()==cid)
		{
			return mdatas.get(i);
		}
	  }
	return null;*/

	}



	public CountBean getCidData(int cid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mdatas.size(); i++) {
			if(mdatas.get(i).getCid()==cid)
			{
				return mdatas.get(i);
			}
		}
		return null;
	}

	public int getPos(int cid) {
		// TODO Auto-generated method stub
		int pos=0;
		for (int i = 0; i < mdatas.size(); i++) {
			if(mdatas.get(i).getCid()==cid)
			{
				pos=i;
			}
		}
		return pos;
	}

	public int getsize() {
		// TODO Auto-generated method stub
		return mdatas.size();
	}
}
