package utils;




import android.content.Context;

import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

public class ToastUtils {
	/**
	 * Toast 消息
	 * @param context 上下文
	 * @param msg  通知内容
	 * @param time 通知时间
	 */
	public static void toastMsg(Context context,String msg,int time) {

		Basemsg( context, msg, time,Gravity.CENTER);
	}
	/**
	 * Toast 消息
	 * @param context 上下文
	 * @param msg  通知内容
	 *
	 */
	public static void toastMsg(Context context,String msg)
	{
		Basemsg( context, msg,Toast.LENGTH_LONG,Gravity.CENTER);
	}


	public static void toastMsg(Context context,String msg,int time,int location)
	{
		Basemsg( context, msg, time, location);
	}
	private static void Basemsg(Context context,String msg,int time,int location)
	{

		if(msg.substring(0,1).charAt(0)=='\n')  //判断第一字符是否有换行符(\n)
		{
			//Log.i("TAG","换行");
			msg=msg.substring(1, msg.length());
		}
		Toast toast=Toast.makeText(context, msg, time);
		if(location==Gravity.BOTTOM)
		{

			toast.setGravity(location,0,200);  //底部显示
		}
		else if(location==Gravity.TOP)
		{

			toast.setGravity(location,0,300);  //底部显示
		}
		else {
			toast.setGravity(location, 0,0);  //居中显示
		}

		toast.show();
	}

}
