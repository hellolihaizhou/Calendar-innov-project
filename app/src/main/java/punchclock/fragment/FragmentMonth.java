package punchclock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihaizhou.mycalendar.R;

import database.dao.record_punchDao;
import punchclock.view.HistogramView;
import utils.DateUtils;


public class FragmentMonth extends Fragment{
	private record_punchDao rDao;
	private HistogramView month_view;
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//lView=(ListView) getActivity().findViewById(R.id.call_listview);
		rDao=new record_punchDao(getContext());
		initData();
		int[] Progress=new int[10];
		String[] mtexts=new String[10];
		String month= DateUtils.getMonth();
		// System.out.println("month--->"+month);
		for (int j = 0; j< 10; j++) {
			String mmonth=DateUtils.getMonthCalculate(month, j-9);
			// System.out.println("mmonth--->"+rDao.getMonthCount(mmonth));
			int MonthCount=rDao.getMonthCount(mmonth);
			int conut=0;
			if(MonthCount>0)
			{
				int days=DateUtils.getDaysByYearMonth(mmonth);
				//	 System.out.println(mmonth+" days--->"+days);
				for (int i =1; i <=days; i++) {
					String day;
					day=i<10?"0"+i:i+"";
					String date=mmonth+"-"+day;
					//	System.out.println("date--->"+date);
					conut+=rDao.getDayCount(date);
				}
				Progress[j]=MonthCount*100/conut;
			}
			else {
				Progress[j]=0;
			}
			mtexts[j]=mmonth.replace("-", "/");

			//System.out.println("count-->"+conut);
		}

		month_view=(HistogramView)getActivity().findViewById(R.id.month_view);
		month_view.setDate(Progress, mtexts, 100);
		// month_view=new HistogramView(getActivity());
		/*    month_view.setProgress(Progress);
		    month_view.setMax(100);
		    month_view.setTexts(mtexts);*/
	/*	    HistogramView.texts = mtexts;
		    HistogramView.Progress =Progress;  
		    HistogramView.max=100;*/
		// System.out.println(DateUtils.getMonthCalculate(month, -2));
		/*    month_view.texts = mtexts;
		    month_view.Progress =Progress;  
		    month_view.max=100;*/

	}

	private void initData() {
		// TODO Auto-generated method stub


	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.month_statistic_fragment, container,
				false);
		return messageLayout;
	}
}
