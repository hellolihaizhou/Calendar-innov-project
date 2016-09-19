package titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lihaizhou.mycalendar.R;

import titlebar.customAlert.CustomDialog;


public class DeleteView  extends RelativeLayout implements CustomDialog.DialogCallBack {

	private LinearLayout lyout_delete;
	private RelativeLayout delete_menu;
	public  DeleteViewCallBack mBack=null;
	private CustomDialog mDialog;
	public interface DeleteViewCallBack
	{
		void deleteOnClick();
	}
	public  void setDeleteViewCallBack(DeleteViewCallBack back)
	{
		mBack=back;
	}
	public DeleteView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;

	@SuppressLint("Recycle")
	public DeleteView(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.mContext=context;
		View view = View.inflate(context, R.layout.delete_view, this);
		delete_menu=(RelativeLayout) view.findViewById(R.id.delete_menu);
		lyout_delete=(LinearLayout) view.findViewById(R.id.layout_delete);
		mDialog=new CustomDialog(mContext);
		mDialog.setDialog(this);

		lyout_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mDialog.show();
			}
		});


	}
	@Override
	public void check(boolean is) {
		// TODO Auto-generated method stub
		if(is)
		{
			mBack.deleteOnClick();
		}
	}


}
