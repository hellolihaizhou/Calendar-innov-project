package titlebar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class TitleBarView  extends RelativeLayout {

	private TextView mtv;
	private ImageButton mIbtn_right,mIbtn_left;
	private Activity mactivity;
	public  TitleBarCallBack mBack=null;
	public interface TitleBarCallBack
	{
		public void RightOnclick();
	}
	public  void setTitleBarCallBack(TitleBarCallBack back)
	{
		mBack=back;
	}
	public TitleBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	@SuppressLint({ "NewApi", "Recycle" })
	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mactivity=(Activity) context;
		View view = View.inflate(context, R.layout.titlebar, this);
		mtv = (TextView) view.findViewById(R.id.tv_title);
		mIbtn_left=(ImageButton)view.findViewById(R.id.ibtn_left);
		mIbtn_right=(ImageButton)view.findViewById(R.id.ibtn_right);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
		CharSequence title = a.getText(R.styleable.TitleBar_title_text);
		if(title!=null) mtv.setText(title);
		CharSequence color=a.getText(R.styleable.TitleBar_text_color);
		if(color!=null)
		{
			mtv.setTextColor(Color.parseColor(color.toString()));
		}


		CharSequence right_visibility=a.getText(R.styleable.TitleBar_right_visibility);
		if(right_visibility!=null)
		{
			mIbtn_right.setVisibility(view.GONE);
		}

		Drawable rihgtsrc= a.getDrawable(R.styleable.TitleBar_right_src);
		if(rihgtsrc!=null) mIbtn_right.setBackground(rihgtsrc);
		Drawable leftsrc= a.getDrawable(R.styleable.TitleBar_left_src);
		if(rihgtsrc!=null) mIbtn_left.setBackground(leftsrc);


		mIbtn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mactivity.finish();
			}
		});

		mIbtn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBack!=null){
					mBack.RightOnclick();
				}

			}
		});


	}

	public void setTitle(String title)
	{
		mtv.setText(title);
	}

}
