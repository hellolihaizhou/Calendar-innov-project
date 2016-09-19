
package countdown.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.PskKeyManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import countdown.listview.CountListView;
import countdown.listview.CountListView.ListViewCallBack;
import countdown.utils.GestureListener;
import database.bean.CountBean;
import database.dao.conutdownDao;
import titlebar.TitleBarView;
import titlebar.TitleBarView.TitleBarCallBack;
import utils.MySharedPreferences;
import utils.ToastUtils;
import utils.pictureUtils;



public class CountMainPage extends Activity implements TitleBarCallBack,conutdownDao.conutdownDaoCallBack,ListViewCallBack{

	protected RelativeLayout background;
	protected ImageView addCount;
	protected TitleBarView mbarView;
	private CountListView mListView;

	protected TextView tv_title,tv_days,tv_date;
	private LinearLayout lyout_info;
	private TextView empty_count;

	private conutdownDao  cDao;
	private MySharedPreferences mSharedPreferences;
	private int sp_cid;
	private static int conunt;
	private static final int RESULT_LOAD_IMAGE = 1;
	public static String MENU_TAG="bg";

	public static final int AddConut=333;
	public static final int delete=111;
	public static final int update=222;

	public static final int Activity_TAG=1111;

	public int pos;

	public static void goToActivity(Activity mActivity)
	{
		Intent intent=new Intent();
		intent.setClass(mActivity, CountMainPage.class);
		mActivity.startActivity(intent);
	}
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setContentView(R.layout.count_main_page);
		initView();
		initData();

		lyout_info.setLongClickable(true);
		lyout_info.setOnTouchListener(new MyGestureListener(this));

	}

	private void initData() {
		// TODO Auto-generated method stub
		mSharedPreferences=new MySharedPreferences(getApplicationContext(), "sp_conutdown");
		sp_cid=mSharedPreferences.getInt("sp_cid");
		cDao=new conutdownDao(this);
		cDao.setConutdownDaoCallBack(this);
		conunt=cDao.getCount();
		if(conunt>0)
		{
			cDao.Query();
			setVisibility(View.VISIBLE);

		}
		else {
			setVisibility(View.GONE);
			setPunchBackground("");
		}

	}
	private void initView() {
		// TODO Auto-generated method stub

		lyout_info=(LinearLayout)findViewById(R.id.lyout_info);
		empty_count=(TextView)findViewById(R.id.empty_count);

		background=(RelativeLayout)findViewById(R.id.count_background);
		mbarView=(TitleBarView)findViewById(R.id.titlebar);
		mbarView.setTitleBarCallBack(this);

		tv_title=(TextView)findViewById(R.id.tv_titles);
		tv_days=(TextView)findViewById(R.id.tv_days);
		tv_date=(TextView)findViewById(R.id.tv_date);

		mListView=new CountListView(this);
		mListView.setListViewCallBack(this);
	}


	/**显示数据控件
	 * @param visibility
	 */
	public void setVisibility(int visibility)
	{
		lyout_info.setVisibility(visibility);
		empty_count.setVisibility(visibility==View.VISIBLE?View.GONE:View.VISIBLE);
	}

	/**设置封面
	 * @param bean
	 */
	public void setDefult(CountBean bean)
	{
		tv_title.setText(bean.getCountTitle());
		tv_days.setText(bean.getCountDays()+"");
		tv_date.setText(bean.getDate());

	}


	/**
	 * make sure on different machine image height can adapte , always occupied 3/5 the screen height
	 */
	private void setBackgroundHight() {
		int W = getWindowManager().getDefaultDisplay().getHeight();//获取屏幕高度
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (W / 5) * 3);
		background.setLayoutParams(params);
	}

	/**设置背景
	 * @param picturePath
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setPunchBackground(String picturePath )
	{
		if(picturePath.isEmpty())
		{
			background.setBackgroundResource(R.drawable.countbackground);
		}
		else
		{
			try {
				Bitmap  bitmap=pictureUtils.getSmallBitmap(picturePath);
				if(bitmap!=null)
				{
					background.setBackground(new BitmapDrawable(this.getResources(),bitmap));
				}
     /*
             if(bitmap!=	null&&!bitmap.isRecycled()){
                    bitmap.recycle();
                    System.gc();
                    bitmap=null;
                }*/
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		background.getBackground().setAlpha(200);

	}


	@Override
	public void RightOnclick() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, AddConutDownActivity.class);
		startActivityForResult(intent, AddConut);
	}
	@Override
	public void query(CountBean bean) {
		// TODO Auto-generated method stub
		mListView.AddData(bean);
	}
	@Override
	public void Queryend() {
		// TODO Auto-generated method stub
		if(sp_cid>=0)
		{
			CountBean countBean =mListView.getCidData(sp_cid);

			if(countBean!=null)
			{
				pos=mListView.getPos(countBean.getCid());
				setDefult(countBean);
				setPunchBackground(countBean.getImgurl());

			}
			else {
				setDefult(mListView.getData(0));
				setPunchBackground("");
			}

		}
		else {
			if(conunt>0)
			{
				setDefult(mListView.getData(0));
				setPunchBackground("");
			}

		}


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//	System.out.println("resultCode------->"+resultCode);
		if(resultCode==AddConut)
		{
			conunt+=1;
			CountBean bean=(CountBean)data.getSerializableExtra("countBean");
			mListView.AddData(bean);
			pos=mListView.getPos(bean.getCid());
			if(conunt>0)
			{
				if(conunt==1)
				{

					setDefult(bean);
					setVisibility(View.VISIBLE);
				}
				boolean is=data.getBooleanExtra("is", false);
				if(is)
				{
					//System.out.println("-------》"+bean.getImgurl());
					setPunchBackground(bean.getImgurl());
					setDefult(bean);
					sp_cid= bean.getCid();
					mSharedPreferences.setInt("sp_cid", sp_cid);


				}

			}
		}

		if(resultCode==delete)
		{
			int cid=data.getIntExtra("cid", -1);
			mListView.Remove(cid);
			conunt-=1;
			if(conunt>0)
			{  if(sp_cid==cid)
			{
				sp_cid=-1;
				mSharedPreferences.setInt("sp_cid",sp_cid);
				//	setDefult(mListView.getCidData(0));
				setPunchBackground("");
			}
				pos=0;
			}
			else if(conunt==0)
			{
				setPunchBackground("");
				setVisibility(View.GONE);
			}

		}

		if(resultCode==update)
		{
			CountBean bean=(CountBean)data.getSerializableExtra("countBean");
			mListView.Update(bean);
			boolean is=data.getBooleanExtra("is", false);
			if(is)
			{
				sp_cid= bean.getCid();
				mSharedPreferences.setInt("sp_cid",sp_cid);
				setDefult(bean);
				setPunchBackground(bean.getImgurl());
			}
			else {

				if(	sp_cid==bean.getCid())
				{
					sp_cid=-1;
					mSharedPreferences.setInt("sp_cid",-1);
					setDefult(mListView.getLocationData(0));
					setPunchBackground("");
				}
			}

			pos=mListView.getPos(bean.getCid());
		}

	}

	@Override
	public void onItemListen(CountBean mBean, int location) {
		// TODO Auto-generated method stub
		boolean is=false;
		if(sp_cid==mBean.getCid())
		{
			is=true;
		}
		Intent intent=new Intent();
		intent.putExtra("location",location);
		intent.putExtra("is", is);
		intent.putExtra("CountBean", mBean);
		intent.setClass(this, AddConutDownActivity.class);
		startActivityForResult(intent, AddConut);
	}


	/**
	 * 继承GestureListener，重写left和right方法
	 */
	private class MyGestureListener extends GestureListener {
		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			Log.i("TAG", "向左滑");
			if(conunt>0)
			{


				// System.out.println("pos--->"+pos+"---------"+mListView.getsize());
				if(pos<mListView.getsize()-1)
				{
					pos+=1;
					setDefult(mListView.getData(pos));

					CountBean countBean=mListView.getData(pos);
					setPunchBackground(countBean.getImgurl());
				}

			}
			return super.left();
		}

		@Override
		public boolean right() {
			Log.i("TAG", "向右滑");

			if(pos>=1)
			{
				pos-=1;
				CountBean countBean=mListView.getData(pos);
				setDefult(countBean);
				setPunchBackground(countBean.getImgurl());
			}
			return super.right();
		}


	}



}
