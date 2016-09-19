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

public class MultilineEditView extends RelativeLayout   {
	EditText edit_linetxt;
	public MultilineEditView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MultilineEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.multilineedit_view, this);

		edit_linetxt=(EditText) view.findViewById(R.id.lineedit_txt);

		final TextView tv_txt=(TextView)view.findViewById(R.id.tv_txt);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
		CharSequence edit_hint = a.getText(R.styleable.EditTextView_hint);
		CharSequence txt = a.getText(R.styleable.EditTextView_txt);
		//	System.out.println("---->"+edit_hint);
		if(txt!=null)
		{
			tv_txt.setText(txt);
		}
		if(edit_hint!=null)
		{
			edit_linetxt.setHint(edit_hint);
		}

	}
	public void setText(String text)
	{
		edit_linetxt.setText(text);
	}
	public String getText()
	{
		return edit_linetxt.getText().toString().trim();
	}

	public void setFocusable(boolean is)
	{



		if(is)
		{
			edit_linetxt.setFocusable(true);
			edit_linetxt.setFocusableInTouchMode(true);
			edit_linetxt.requestFocus();
		}
		else {
			edit_linetxt.clearFocus();
			edit_linetxt.setFocusable(is);
		}

	}


}
