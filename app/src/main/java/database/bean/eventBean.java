package database.bean;

import java.io.Serializable;

public class eventBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
    private int sorts;
    private String starttime;
    private String endtime;
    private boolean isallday;
    private addressBean addressBean=null;
    private String time;
    private int eid;
    private int state;
	public eventBean(String title, int sorts, String starttime, String endtime,String time, Boolean isallday, addressBean mBean) {
		super();
		this.title = title;
		this.sorts = sorts;
		this.time=time;
		this.starttime = starttime;
		this.endtime = endtime;
		this.isallday = isallday;
		this.addressBean = mBean;
	}
	
/*	public eventBean(String title, int sorts, String starttime, String endtime, String isallday) {
		this.title = title;
		this.sorts = sorts;
		this.starttime = starttime;
		this.endtime = endtime;
		this.isallday = isallday;
	}*/

	public int getSorts() {
		return sorts;
	}

	public eventBean(int eid, String title, int sorts, addressBean addressBean) {
	super();
	this.eid = eid;
	this.title = title;
	this.sorts = sorts;
	this.addressBean = addressBean;
}
	
	

	public eventBean(int eid, String title, String time) {
		super();
		this.eid = eid;
		this.title = title;
		this.time = time;
	}

	public eventBean(int eid, String title, String time, int state) {
		this.eid = eid;
		this.title = title;
		this.time = time;
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public boolean getIsallday() {
		return isallday;
	}

	public String getTitle() {
		return title;
	}
	public addressBean getaddressBean() {
		if(addressBean==null)addressBean=new addressBean();
		return addressBean;
	}

	public String getTime() {
		return time;
	}
	
	
}
