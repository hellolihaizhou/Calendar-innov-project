package punchclock.activity;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lihaizhou.mycalendar.R;

import database.dao.punchclockDao;

public class showPunch extends RelativeLayout {
	public static showPunchCallBack mBack;
	public interface showPunchCallBack
	{
		void update();
	}
	public void setshowPunchCallBack(showPunchCallBack Back)
	{
		this.mBack=Back;
	}

	public int[] rid=new int[]{R.id.red_ball,R.id.yellow_ball,R.id.green_ball,R.id.blue_ball,R.id.purple_ball};
//	public int[] img=new int[]{R.drawable.firstcolor_normal,R.drawable.secondcolor_normal,R.drawable.thirdcolor_normal,R.drawable.fourcolor_normal,R.drawable.fivecolor_normal};

	public int[] img=new int[]{R.drawable.first,R.drawable.second,R.drawable.thrid,R.drawable.four,R.drawable.five};

	private ImageView[] imgview=new ImageView[5];
	private punchclockDao pDao;
	public showPunch(Context context, AttributeSet attrs) {
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.showpunch, this);
		pDao=new punchclockDao(context);
		//	pDao.setPunchclockDaoCallBack(this);
		for (int i = 0; i < rid.length; i++) {
			imgview[i]=(ImageView)findViewById(rid[i]);
		}



	}


/*	@Override
	public void query(punchclockBean Bean) {
		// TODO Auto-generated method stub
		if(Bean!=null)
		imgview[Bean.getImageindex()].setBackgroundResource(img[Bean.getImageindex()]);
	}*/

	public  void show(String date)
	{
		//System.out.println("date--->"+date);
		List<Integer> mid=pDao.show(date);
		for (int i = 0; i < rid.length; i++) { //重置所有状态
			imgview[i].setBackgroundResource(R.drawable.no_press);
		}
		if(mid.size()>0)
		{
			for (int i = 0; i < mid.size(); i++) {
				imgview[mid.get(i)].setBackgroundResource(img[mid.get(i)]);
			}
		}
	}

}
