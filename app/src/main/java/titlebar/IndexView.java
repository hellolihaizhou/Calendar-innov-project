package titlebar;

import java.text.BreakIterator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;


public class IndexView extends RelativeLayout {

	private TextView mtv;
	private ImageView miview;
	public IndexView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	@SuppressLint("NewApi")
	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.index_view, this);
		mtv = (TextView) view.findViewById(R.id.mtv);
		miview=(ImageView)view.findViewById(R.id.mivew);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.modlue);
		CharSequence txt = a.getText(R.styleable.modlue_text);
		Drawable drawable= a.getDrawable(R.styleable.modlue_src);
		if(txt!=null) mtv.setText(txt);
		if(drawable!=null) miview.setBackground(drawable);
		a.recycle();

	}



}