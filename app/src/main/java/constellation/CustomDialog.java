
package constellation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lihaizhou.mycalendar.R;


public class CustomDialog {




	public View view;
	private GridView gv;
	TransportPosListener transportPos=null;
	private boolean isshow=false;
	private Dialog aBuilder;
	public interface TransportPosListener
	{
		void transportConstellationPos(int pos);
	}

	public void setTransportPosListener( TransportPosListener mtransportPos)
	{
		this.transportPos=mtransportPos;
	}

	public CustomDialog(Context context) {

		aBuilder=new AlertDialog.Builder(context).create();
		view=View.inflate(context, R.layout.constellation_gridview, null);
		gv = (GridView) view.findViewById(R.id.constellation_fragment);
		GViewAdapter mAdapter=new GViewAdapter(context, constellationmain.arraylist);
		gv.setAdapter(mAdapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(transportPos!=null)
				{
					transportPos.transportConstellationPos(position);
				}

				aBuilder.dismiss();
				isshow=false;
			}
		});
		// TODO Auto-generated constructor stub
	}


	public void show() {
		// TODO Auto-generated method stub
		if(isshow)
		{
			return ;
		}
		Window window = aBuilder.getWindow();
		window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
		//    window.setWindowAnimations(R.style.mystyle);  //添加动画
		isshow=true;
		aBuilder.show();
		aBuilder.getWindow().setContentView(view);
		aBuilder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				isshow=false;

			}
		});
	}

}
