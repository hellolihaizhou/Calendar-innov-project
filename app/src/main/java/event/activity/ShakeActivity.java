package event.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

import database.bean.eventBean;
import database.dao.eventDao;
import utils.MySharedPreferences;

public class ShakeActivity implements ShakeListener.OnShakeListener,CustomenentDialog.DialogCallBack {
	Vibrator mVibrator;
	private ShakeListener mShakeListener = null;
	private Context mContext;
	private boolean isShake;
	private eventDao eDao;
	MySharedPreferences mySharedPreferences;

	public void setIsShake(boolean isShake) {
		this.isShake = isShake;
		if(isShake)
		{
			mShakeListener.start();
		}
		else {
			mShakeListener.stop();;
		}
	}
	public void stop()
	{
		mShakeListener.stop();;
	}

	public void start()
	{
		mShakeListener.start();
	}
	private Handler mHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {

			mShakeListener.stop();
			//  customenentDialog.show();
		};
	};
	private CustomenentDialog customenentDialog;
	public  ShakeActivity(Context Context)
	{     this.mContext=Context;
		if(mShakeListener==null)
		{   customenentDialog=new CustomenentDialog(mContext);
			customenentDialog.setDialog(this);
			mVibrator = (Vibrator)mContext.getSystemService(mContext.VIBRATOR_SERVICE);
			mShakeListener=new ShakeListener(mContext);
			mShakeListener.setOnShakeListener(this);
			eDao=new eventDao(mContext);
		}

	}
	@Override
	public void onShake() {
		// TODO Auto-generated method stub

		mShakeListener.stop();
		startVibrato(); //开始 震动
		eventBean eBean=eDao.newestrecord();
		if(eBean!=null)
		{
			customenentDialog.set(eBean.getTitle());
			customenentDialog.show();
			mHandler.sendEmptyMessage(0);
		}




	}

	private void startVibrato() {
		// TODO Auto-generated method stub
		//定义震动
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复

	}
	@Override
	public void onClickListen() {
		// TODO Auto-generated method stub
		mShakeListener.start();
	}
}
