package event.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lihaizhou.mycalendar.R;

import event.bean.sortsBean;
import event.listview.sortsListView;
import titlebar.TitleBarView;

public class sortsActivity extends Activity implements TitleBarView.TitleBarCallBack {
	private TitleBarView mTitleBar;
	public static   String[] item=new String[]{"事件","生活","爱情","生日","节日","娱乐","学习","工作"};
	public static  int[] resid=new int[]{R.drawable.sort_1,R.drawable.sort_2,R.drawable.sort_3,R.drawable.sort_4,R.drawable.sort_5,R.drawable.sort_6,R.drawable.sort_7,R.drawable.sort_8};
	private sortsListView mListview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sorts_main);
		initView();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mListview=new sortsListView(this);
		for (int i = 0; i < item.length; i++) {
			sortsBean bean=new sortsBean(resid[i], item[i]);
			mListview.AddData(bean);
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
		int selected=mListview.getSelected();
		Intent intent=getIntent();
		Bundle bundle = new Bundle();
		bundle.putInt("selected", mListview.getSelected());
		bundle.putString("sorts", item[selected]);
		intent.putExtras(bundle);
		setResult(AddeventActivity.SORTS_TAD, intent);
		super.finish();
	}
	@Override
	public void RightOnclick() {
		// TODO Auto-generated method stub

	}
}
