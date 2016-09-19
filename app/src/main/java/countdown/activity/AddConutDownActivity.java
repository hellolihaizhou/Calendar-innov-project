package countdown.activity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.bigkoo.pickerview.TimePickerView.Type;
import com.lihaizhou.mycalendar.R;

import countdown.*;
import countdown.activity.CountMainPage;
import database.bean.CountBean;
import database.dao.conutdownDao;
import titlebar.*;
import titlebar.*;
import titlebar.DeleteView;
import titlebar.EditView;
import titlebar.ItemSwitchView;
import titlebar.ItemView;
import titlebar.TitleBarView;
import utils.*;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddConutDownActivity extends Activity implements TitleBarView.TitleBarCallBack, ItemView.itemViewCallBack,OnTimeSelectListener, DeleteView.DeleteViewCallBack, ItemSwitchView.ItemSwitchViewCallBack {
	protected ItemView item_date,item_bg;
	protected TitleBarView mtitlebar;
	private ItemSwitchView iSwitchView_bg;
	private TimePickerView   chooseCountDate;
	private static final int RESULT_LOAD_IMAGE = 1;
	private  String actualPicturePath="";
	private conutdownDao cDao;
	private String mdate="";
	private EditView editView_title;
	private DeleteView deletevew;

	private int mcid=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_count);
		initView();
		initData();
	}
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();

		chooseCountDate= new TimePickerView(this,Type.YEAR_MONTH_DAY);
		chooseCountDate.setTitle("选择日期");
		chooseCountDate.setCyclic(false);
		chooseCountDate.setCancelable(true);
		chooseCountDate.setOnTimeSelectListener(this);
		int location=intent.getIntExtra("location", -1);
		if(location!=-1) //
		{
			CountBean bean=(CountBean) intent.getSerializableExtra("CountBean");
			mcid=bean.getCid();
			actualPicturePath=bean.getImgurl();
			boolean is=intent.getBooleanExtra("is", false);
			if(!actualPicturePath.isEmpty())
			{
				File file=new File(actualPicturePath);
				item_bg.setValue(file.getName());
			}

			if(is)
			{
				iSwitchView_bg.setValue(is);
			}
			editView_title.setText(bean.getCountTitle());
			mdate=bean.getDate();
			item_date.setValue(Timeutils.edateToCdate(mdate));
			chooseCountDate.setTime(Timeutils.getStringToDate(mdate));
		}
		else {

			chooseCountDate.setTime(Timeutils.getStringToDate(Timeutils.getDateCalculate(Timeutils.getCurrentDate(), 3)));
			deletevew.setVisibility(View.GONE);

		}

		cDao=new conutdownDao(getApplicationContext());


	}
	private void initView() {
		// TODO Auto-generated method stub
		editView_title=(EditView)findViewById(R.id.editView_title);
		item_date=(ItemView)findViewById(R.id.item_date);
		item_bg=(ItemView)findViewById(R.id.item_backcolor);
		iSwitchView_bg=(ItemSwitchView)findViewById(R.id.item_switch_bg);
		mtitlebar=(TitleBarView)findViewById(R.id.titlebar);

		mtitlebar.setTitleBarCallBack(this);

		item_date.mfindViewById(R.id.item_date);
		item_bg.mfindViewById(R.id.item_backcolor);
		iSwitchView_bg.mfindViewById(R.id.item_switch_bg);
		iSwitchView_bg.setItemSwitchViewCallBack(this);
		item_date.setitemViewCallBack(this);
		item_bg.setitemViewCallBack(this);

		deletevew=(DeleteView)findViewById(R.id.delete_menu);
		deletevew.setDeleteViewCallBack(this);
	}
	@Override
	public void valueOnclick(int id) {
		// TODO Auto-generated method stub
		if(id==R.id.item_date)  //日期选择框
		{

			chooseCountDate.show();

		}

		if(id==R.id.item_backcolor)
		{
			item_bg.setEnabled(false);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(item_date.getWindowToken(), 0);
			Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(chooseImageIntent, RESULT_LOAD_IMAGE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		item_bg.setEnabled(true);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();

			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			actualPicturePath = cursor.getString(columnIndex);
			File file=new File(actualPicturePath);
			cursor.close();
			item_bg.setValue(file.getName());
			if(!iSwitchView_bg.getValue())
			{
				iSwitchView_bg.setValue(true);
			}


		}
	}

	@Override
	public void onTimeSelect(Date date) {
		// TODO Auto-generated method stub
		mdate=Timeutils.getEDate(date);
		//String  countDateValue = new SimpleDateFormat("yyyy年MM月dd日").format(date);
		item_date.setValue(Timeutils.getCDate(date));
	}

	@Override
	public void RightOnclick() { //添加按钮
		// TODO Auto-generated method stub
		boolean is=iSwitchView_bg.getValue();
		String countTitle=editView_title.getText();
		String str="";
		if(countTitle.isEmpty())
		{
			str="◎请输入标题！";
		}
		if(mdate.isEmpty())
		{
			str+="\n◎请选择日期！";
		}

		if(!mdate.isEmpty()&&!Timeutils.isMoreThanToday(mdate))
		{
			str+="\n◎选择日期必须大于今天！";
		}
		if(is==true&&actualPicturePath.isEmpty())
		{
			str+="\n◎请选择图片！";
		}

		if(!str.isEmpty())
		{
			ToastUtils.toastMsg(getApplicationContext(), str,3);
			return;
		}
		CountBean countBean=new CountBean(countTitle, Timeutils.getdifferDay(mdate), mdate, actualPicturePath);

		if(mcid==-1) //添加操作
		{

			int cid=(int) cDao.add(countBean);
			if(cid>=0)
			{
				countBean.setCid(cid);
				Intent intent=getIntent();
				intent.putExtra("countBean", countBean);
				intent.putExtra("is", is);
				setResult(CountMainPage.AddConut, intent);

			}
			else {
				Toast.makeText(getApplicationContext(), "添加失败！", 3).show();
			}

		}
		else { //更新 操作
			countBean.setCid(mcid);
			if(cDao.Update(countBean)==1)
			{
				Intent intent=getIntent();
				intent.putExtra("countBean", countBean);
				intent.putExtra("is", is);
				setResult(CountMainPage.update, intent);
			}
			else {
				Toast.makeText(getApplicationContext(), "更新失败！", 3).show();
			}
		}

		finish();

	}
	@Override
	public void deleteOnClick() {
		// TODO Auto-generated method stub
		if(cDao.delete(mcid)==1)
		{
			Intent intent=getIntent();
			intent.putExtra("cid", mcid);
			setResult(CountMainPage.delete, intent);

		}
		else {
			Toast.makeText(getApplicationContext(), "删除失败！", Toast.LENGTH_LONG).show();
		}
		finish();
	}
	@Override
	public void SwitchOnclick(int id, boolean is) {
		// TODO Auto-generated method stub

	}

}
