package event.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import event.activity.EventTimerTherad;

public class eventService extends Service{

	public static EventTimerTherad meventThread=null;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("TAG", "BindService-->onBind()");
		return null;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//	System.out.println("开启线程");

		if(meventThread==null)
		{
			Log.i("TAG", "开启meventThread");
			meventThread=new EventTimerTherad(this);
			new Thread(meventThread).start();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Intent localIntent = new Intent();
		localIntent.setClass(this, eventService.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return START_STICKY_COMPATIBILITY;
		//return super.onStartCommand(intent, flags, startId);
	}


}
