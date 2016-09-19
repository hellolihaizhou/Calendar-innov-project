

package event.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class CustomenentDialog  {
	public Context mContext;
	public  Dialog aBuilder;
	public View view;
	public DialogCallBack mcallBack;
	public TextView tv;
	// private ImageButton ibtn_close;
	public interface DialogCallBack
	{
		void onClickListen();
	}

	public void setDialog(DialogCallBack callBack) {
		this.mcallBack = callBack;

	}
	public CustomenentDialog(Context context) {
		this.mContext=context;
		// TODO Auto-generated constructor stub
		aBuilder=new AlertDialog.Builder(mContext).create();
		view=View.inflate(mContext, R.layout.newevent, null);
		tv=(TextView) view.findViewById(R.id.tv_title);
	/*	    ibtn_close=(ImageButton)view.findViewById(R.id.ibtn_close);
		    ibtn_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mcallBack.onClickListen();
					aBuilder.dismiss();

				}
			});*/
		//   aBuilder.setCanceledOnTouchOutside(false);
	}

	public void set(String title)
	{
		tv.setText(title);
	}
	public void show() {
		// TODO Auto-generated method stub


		Window window = aBuilder.getWindow();
		window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置

		//    window.setWindowAnimations(R.style.mystyle);  //添加动画

		aBuilder.show();
		aBuilder.getWindow().setContentView(view);
		aBuilder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mcallBack.onClickListen();
			}
		});
	}
     

/*	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(R.id.btn_ok==v.getId())
		{
			mcallBack.check(true);
		}
		else {
			mcallBack.check(false);
		}
		aBuilder.dismiss();
	}*/



}
