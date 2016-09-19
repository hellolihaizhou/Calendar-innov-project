package punchclock.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihaizhou.mycalendar.R;

import database.dao.record_punchDao;
import punchclock.view.HistogramView;
import utils.Timeutils;


public class FragmentWeek extends Fragment{
	HistogramView weeks_view;
	SimpleDateFormat format=new SimpleDateFormat("E");

	private record_punchDao rDao;
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rDao=new record_punchDao(getContext());
		String[] mtexts=new String[]{"周一","周二","周三","周四","周五","周六","周日"};
		int[] Progress=new int[mtexts.length];
		String week=format.format(new Date());
		int index=-1;
		for (int i = 0; i < mtexts.length; i++) {
			//	System.out.println("---->"+mtexts[i]);
			if(mtexts[i].equals(week))
			{
				index=i;
			}
		}
		//   System.out.println("index--->"+index);
		String startdate= Timeutils.getDateCalculate(Timeutils.getCurrentDate(), -index);
		// System.out.println("周一日期---》"+startdate);
		for (int i = 0; i <mtexts.length; i++) {
			String date=Timeutils.getDateCalculate(startdate,i);
			//  System.out.println("日期---》"+date);
			int count=rDao.getDayCount(date);
			int daycount=0;
			if(count>0)
			{
				daycount=rDao.getrCount(date);
				Progress[i]=daycount*100/count;
			}
			else {
				Progress[i]=0;
			}

		}

		weeks_view=(HistogramView)getActivity().findViewById(R.id.weeks_view);
		weeks_view.setDate(Progress, mtexts, 100);
		/*  weeks_view.set
		  weeks_view.Progress =Progress;  
		  weeks_view.max=100;*/
	/*	    HistogramView.texts = mtexts;
		    HistogramView.Progress =Progress;  
		    HistogramView.max=100;*/

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.week_statistic_fragment, container,
				false);
		return messageLayout;
	}

}
