

package titlebar.customAlert;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

public class CustomDialog  implements OnClickListener{
	public Context mContext;
	public  Dialog aBuilder;
	public TextView tv_title;
	public View view;
	public TextView btn_ok,btn_cancel;
	public DialogCallBack mcallBack;
	public interface DialogCallBack
	{
		public void check(boolean is);
	}

	public void setDialog(DialogCallBack callBack) {
		this.mcallBack = callBack;

	}
	public CustomDialog(Context context) {
		this.mContext=context;
		// TODO Auto-generated constructor stub
		aBuilder=new AlertDialog.Builder(mContext).create();

		view=View.inflate(mContext, R.layout.aler, null);
		tv_title=(TextView) view.findViewById(R.id.tv_title);
		btn_ok=(TextView)view.findViewById(R.id.btn_ok);
		btn_cancel=(TextView)view.findViewById(R.id.btn_cancle);
		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		aBuilder.setCanceledOnTouchOutside(false);
	}

	public void set(String title)
	{
		tv_title.setText(title);
	}
	public void show() {
		// TODO Auto-generated method stub


		Window window = aBuilder.getWindow();
		window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置

		//    window.setWindowAnimations(R.style.mystyle);  //添加动画

		aBuilder.show();
		aBuilder.getWindow().setContentView(view);
	}
	@Override
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
	}



}
