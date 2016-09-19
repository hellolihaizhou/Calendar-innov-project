package punchclock.activity;



import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lihaizhou.mycalendar.R;

import punchclock.fragment.FragmentDay;
import punchclock.fragment.FragmentMonth;
import punchclock.fragment.FragmentWeek;

public class punchStatisticsActivity extends FragmentActivity implements OnClickListener{

	//private RadioGroup rGrop_tab;
	private int[] rid=new int[]{R.id.tab_first,R.id.tab_middle,R.id.tab_last};
	private RadioButton[]  rbtn=new RadioButton[rid.length];
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for (int i = 0; i < rid.length; i++) {
			if(v.getId()==rid[i])
			{
				Selected(i);
				mViewPager.setCurrentItem(i);
			}
		/*	else {
				int colors=ContextCompat.getColor(getApplicationContext(),R.color.tab_check);
				rbtn[i].setTextColor(colors);
			}*/
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.punch_statistics_main);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		for (int i = 0; i < rbtn.length; i++) {
			rbtn[i]=(RadioButton) findViewById(rid[i]);
			rbtn[i].setOnClickListener(this);
			if(i!=0)
			{
				int colors=ContextCompat.getColor(getApplicationContext(),R.color.tab_check);
				rbtn[i].setTextColor(colors);
			}

		}
		rbtn[0].setChecked(true);
		int colors=ContextCompat.getColor(getApplicationContext(),R.color.white);
		rbtn[0].setTextColor(colors);


		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		FragmentDay mFragmentDay=new FragmentDay();
		FragmentWeek mFragmentWeek=new FragmentWeek();
		FragmentMonth mFragmentMonth=new FragmentMonth();



		mFragments.add(mFragmentDay);
		mFragments.add(mFragmentWeek);
		mFragments.add(mFragmentMonth);

		mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}
			private int currentIndex;
			@Override
			public void onPageSelected(int position) {
				Selected(position);
				currentIndex = position;
			}



			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}

	public void Selected(int i)
	{

		for (int j = 0; j < rid.length; j++) {
			if(j==i)
			{
				int colors=ContextCompat.getColor(getApplicationContext(),R.color.white);
				rbtn[j].setTextColor(colors);
				rbtn[j].setEnabled(false);
				rbtn[j].setChecked(true);
			}
			else {
				int colors=ContextCompat.getColor(getApplicationContext(),R.color.tab_check);
				rbtn[j].setTextColor(colors);
				rbtn[j].setEnabled(true);
			}

		}
	}
}
