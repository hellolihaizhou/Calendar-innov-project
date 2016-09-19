package punchclock.fragment;


import java.text.DecimalFormat;
import java.util.Iterator;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihaizhou.mycalendar.R;

import database.dao.record_punchDao;
import punchclock.view.HistogramView;
import utils.DateUtils;
import utils.Timeutils;


public class FragmentDay extends Fragment{
	HistogramView day_view;
	private record_punchDao rDao;
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//lView=(ListView) getActivity().findViewById(R.id.call_listview);

		rDao=new record_punchDao(getContext());
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		// String[] mtexts=new String[]{"2016年\n  8/22","2016年\n  8/23","8/24","8/25","8/26","8/27","8/29","8/30","8/31","9/1"};
		// String[] mtexts=new String[]{"周一","周二","周三","周四","周五"," 周六","周日"};

		//  int[] Progress=new int[]{80,60,12,80,60,12,12,80,60,12};
		//   int[] Progress=new int[]{80,60,12,80,60,12,12};
		int[] Progress=new int[10];
		String[] mtexts=new String[10];
		String mdate= Timeutils.getCurrentDate();
		for (int i = 0; i <10; i++) {

			mtexts[i]= DateUtils.getDateCalculate(mdate, i-9);
			String date=Timeutils.getDateCalculate(mdate,  i-9);
			int count=0;
			int finishCount=rDao.getrCount(date);  //当日已完成数
			if(finishCount>0)
			{
				count=rDao.getDayCount(date);
				finishCount=finishCount*100;
				Progress[i]=finishCount/count;
			}
			else {
				Progress[i]=0;
			}
			//	System.out.println(date+"------->总数："+rDao.getDayCount(date)+"已完成--》"+rDao.getrCount(date));

		}


		day_view=(HistogramView) getActivity().findViewById(R.id.day_view);
		day_view.setDate(Progress, mtexts, 100);
		 /*    day_view.texts = mtexts;
		        day_view.Progress =Progress;  
		        day_view.max=100;*/




	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		View messageLayout = inflater.inflate(R.layout.day_statistic_fragment,
				container, false);
		return messageLayout;
	}



}
