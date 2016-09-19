package database.bean;

import java.io.Serializable;

/**
 * Created by lihaizhou on 16-6-20.
 */
public class CountBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 倒计时标题
	 */
	private String CountTitle;
	/**
	 * 相差天数
	 */
	private long CountDays;

	/**
	 * 日期
	 */
	private String date;

	private String imgurl;

	private int Cid;

	public int getCid() {
		return Cid;
	}


	public void setCid(int cid) {
		Cid = cid;
	}


/*	public CountBean(String countTitle, long countDays, String date,int cid) {
		super();
		CountTitle = countTitle;
		CountDays = countDays;
		this.date = date;
		this.Cid=cid;
	}*/


	public String getImgurl() {
		return imgurl;
	}


	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}




	public CountBean(String countTitle, long countDays, String date, String imgurl) {
		super();
		CountTitle = countTitle;
		CountDays = countDays;
		this.date = date;
		this.imgurl = imgurl;
	}


	public CountBean(int cid, String countTitle, String date, long countDays, String imgurl) {
		super();
		Cid = cid;
		CountTitle = countTitle;
		this.date = date;
		CountDays = countDays;
		this.imgurl = imgurl;
	}


	public long getCountDays() {
		return CountDays;
	}



	public void setCountDays(long countDays) {
		CountDays = countDays;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public void setCountTitle(String countTitle) {
		this.CountTitle = countTitle;
	}



	public String getCountTitle() {
		return CountTitle;
	}


}
