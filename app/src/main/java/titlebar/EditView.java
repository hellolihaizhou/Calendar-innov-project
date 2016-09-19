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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

public class EditView extends RelativeLayout  implements TextWatcher {
	EditText edit_txt;
	ImageButton ibtn_clear;
	public EditView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public EditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.edit_view, this);

		edit_txt=(EditText) view.findViewById(R.id.edit_txt);
		ibtn_clear=(ImageButton)view.findViewById(R.id.ibtn_clear);
		final TextView tv_txt=(TextView)view.findViewById(R.id.tv_txt);

		ibtn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit_txt.setText("");
				ibtn_clear.setVisibility(View.GONE);
			}
		});

		edit_txt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)//获得焦点
				{
					if(edit_txt.getText().length()>0)
					{
						if(ibtn_clear.getVisibility()==View.GONE)
						{
							ibtn_clear.setVisibility(View.VISIBLE);
						}
					}
				}
				else {
					if(ibtn_clear.getVisibility()==View.VISIBLE)
					{
						ibtn_clear.setVisibility(View.GONE);
					}
				}
			}
		});
		edit_txt.addTextChangedListener(this);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
		CharSequence edit_hint = a.getText(R.styleable.EditTextView_hint);
		CharSequence txt = a.getText(R.styleable.EditTextView_txt);
		CharSequence ispwd = a.getText(R.styleable.EditTextView_ispwd);  //密码框
		//CharSequence enabled = a.getText(R.styleable.EditTextView_enabled);  //密码框
		CharSequence num = a.getText(R.styleable.EditTextView_num);  //数组框
		if(ispwd!=null)
		{
			//Log.i("TAG", "--->"+ispwd);
			edit_txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		if(num!=null)
		{
			edit_txt.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		if(txt!=null)
		{
			tv_txt.setText(txt);
		}
		if(edit_hint!=null)
		{
			edit_txt.setHint(edit_hint);
		}

	}
	public void setText(String text)
	{
		edit_txt.setText(text);
	}
	public String getText()
	{
		return edit_txt.getText().toString().trim();
	}

	public void setFocusable(boolean is)
	{



		if(is)
		{
			edit_txt.setFocusable(true);
			edit_txt.setFocusableInTouchMode(true);
			edit_txt.requestFocus();
		}
		else {
			edit_txt.clearFocus();
			edit_txt.setFocusable(is);
		}

	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		//Log.i("TAG", "onTextChanged");
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		//  Log.i("TAG", "输入文字后的状态");
		if (edit_txt.getText().length()>0) {
			if(ibtn_clear.getVisibility()==View.GONE)
			{
				ibtn_clear.setVisibility(View.VISIBLE);
			}

		}
		else {
			if(ibtn_clear.getVisibility()==View.VISIBLE)
			{
				ibtn_clear.setVisibility(View.GONE);
			}

		}
	}

}
