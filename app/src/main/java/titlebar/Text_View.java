package titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class Text_View extends RelativeLayout   implements OnClickListener{
	TextView tv_txt,tv_name;
	public Text_View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public TextViewCallBack mBack=null;

	public interface TextViewCallBack
	{
		public void setMyOnClickListener();
	}

	public void setOnClickListener( TextViewCallBack Back)
	{
		this.mBack=Back;
	}
	public Text_View(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.text_view, this);
		tv_name=(TextView) view.findViewById(R.id.tv_name);
		tv_txt=(TextView) view.findViewById(R.id.tv_txt);
		tv_txt.setOnClickListener(this);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
		CharSequence hint = a.getText(R.styleable.EditTextView_hint);
		CharSequence txt = a.getText(R.styleable.EditTextView_txt);
		if(txt!=null)
		{
			tv_name.setText(txt);
		}
		if(hint!=null)
		{
			tv_txt.setHint(hint);
		}

	}

	public void setHint(String hint)
	{
		tv_txt.setHint(hint);
	}
	public void setText(String text)
	{
		tv_txt.setText(text);
	}
	public String getText()
	{
		return tv_txt.getText().toString().trim();
	}
	public void setName(String name)
	{
		tv_name.setText(name);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tv_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mBack!=null)
				{
					mBack.setMyOnClickListener();
				}

			}
		});
	}




}
