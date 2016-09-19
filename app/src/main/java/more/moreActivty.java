package more;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lihaizhou.mycalendar.R;

import titlebar.ItemSwitchView;
import utils.MySharedPreferences;

public class moreActivty extends Activity implements ItemSwitchView.ItemSwitchViewCallBack {
	public static final int Activity_TAG=4444;
	private ItemSwitchView itemsview_punch,itemsview_shark;
	private MySharedPreferences mMySharedPreferences;
	private boolean mpunch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_main);
		initView();
		initData();

	}
	private void initData() {
		// TODO Auto-generated method stub
		mMySharedPreferences=new MySharedPreferences(getApplicationContext(), "sp_setting");
		mpunch=mMySharedPreferences.getBoolean("punch", true);
		itemsview_punch.setValue(mpunch);
		itemsview_shark.setValue(mMySharedPreferences.getBoolean("Shake", false));
	}
	private void initView() {
		// TODO Auto-generated method stub
		itemsview_punch=(ItemSwitchView)findViewById(R.id.itemsview_punch);
		itemsview_punch.mfindViewById(R.id.itemsview_punch);
		itemsview_punch.setItemSwitchViewCallBack(this);

		itemsview_shark=(ItemSwitchView)findViewById(R.id.itemsview_shark);
		itemsview_shark.mfindViewById(R.id.itemsview_shark);
		itemsview_shark.setItemSwitchViewCallBack(this);
	}
	@Override
	public void SwitchOnclick(int id, boolean is) {
		// TODO Auto-generated method stub
		if(id==R.id.itemsview_punch)
		{
			mMySharedPreferences.setBoolean("punch", is);
		}

		if(id==R.id.itemsview_shark)
		{
			mMySharedPreferences.setBoolean("Shake", is);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(mpunch!=mMySharedPreferences.getBoolean("punch", true))
		{
			Intent intent=getIntent();
			intent.putExtra("ispunch", !mpunch);
			setResult(Activity_TAG, intent);
		}
		super.finish();
	}
}
