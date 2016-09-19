package constellation;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

import constellation.Bean;
import constellation.CustomDialog;


public class constellationmain extends LinearLayout implements CustomDialog.TransportPosListener {
	private ImageView switch_constellation,show_constellation_pic;
	private CustomDialog customDialog;
	private View view;
	private TextView showConstellationComment,show_today_constellation;
	private String[] constellationCommentArrays;
	private Context mContext;
	public static  ArrayList<Bean> arraylist = new ArrayList<Bean>();
	SimpleDateFormat ssdateFormat =new SimpleDateFormat("MMdd");
	public constellationmain(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public constellationmain(Context context, AttributeSet attrs) {
		super(context);
		this.mContext=context;
		view=View.inflate(context, R.layout.constellation_part, this);
		switch_constellation=(ImageView) view.findViewById(R.id.switch_constellation);
		show_constellation_pic=(ImageView)view.findViewById(R.id.show_constellation_pic);
		showConstellationComment=(TextView)view.findViewById(R.id.show_constellation_detail);
		show_today_constellation=(TextView)view.findViewById(R.id.show_today_constellation);
		customDialog=new CustomDialog(view.getContext());
		customDialog.setTransportPosListener(this);
		showConstellationComment.setText(getconstellationComment());
		switch_constellation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				customDialog.show();
			}
		});

		initData();

		setDate(ssdateFormat.format(new Date()));
	}
	private void setDate(String date)
	{
		int s=Integer.parseInt(date);
		for (int i = 0; i < arraylist.size(); i++) {
			if(s>=arraylist.get(i).getStartdate()&&arraylist.get(i).getEnddate()>=s)
			{
				show_constellation_pic.setImageResource(arraylist.get(i).getResid());
				show_today_constellation.setText("今日运势-"+arraylist.get(i).getText());
			}
		}
	}
	private void initData() {
		// TODO Auto-generated method stub
		Integer constellationPic[] = {R.drawable.constellation_baiyang,R.drawable.constellation_jinniu,R.drawable.constellation_shuangzi,R.drawable.constellation_juxie,R.drawable.constellation_shizi,R.drawable.constellation_chunv,R.drawable.constellation_tianping,R.drawable.constellation_tianxie,R.drawable.constellation_sheshou,R.drawable.constellation_moxie,R.drawable.constellation_shuiping,R.drawable.constellation_shuiping};
		String  constellationText[] = mContext.getResources().getStringArray(R.array.ConstellationArry);
		String  constellationDate[] = mContext.getResources().getStringArray(R.array.ConstellationDateArry);
		for(int i=0;i<constellationPic.length;i++){
			String[] strdate=constellationDate[i].split("-");
			int startdate=Integer.parseInt(strdate[0].replace("/", ""));
			int enddate=Integer.parseInt(strdate[1].replace("/", ""));
			Bean bean=new Bean(constellationPic[i] , constellationText[i], constellationDate[i],startdate,enddate);
			arraylist.add(bean);
		}
	}
	public String getconstellationComment() {
		String constellationComment = null;
		constellationCommentArrays = this.getResources().getStringArray(R.array.ConstellationCommentArray);
		int id = (int) (Math.random() * (constellationCommentArrays.length - 1));//随机产生一个index索引
		constellationComment = constellationCommentArrays[id];
		return constellationComment;
	}
	@Override
	public void transportConstellationPos(int pos) {
		// TODO Auto-generated method stub
		show_constellation_pic.setImageResource(arraylist.get(pos).getResid());
		show_today_constellation.setText("今日运势-"+arraylist.get(pos).getText());
	}

}
