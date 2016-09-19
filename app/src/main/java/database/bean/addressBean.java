package database.bean;

import java.io.Serializable;

public  class addressBean implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private double latitude;
  private double location;
  private String address="";
  private int eid;
  private int aid;
	public addressBean(int eid,double latitude, double location, String address) {
		this.eid=eid;
		this.latitude = latitude;
		this.location = location;
		this.address = address;
	}
	
    

	public addressBean(int aid, String address, double latitude, double location) {
		super();
		this.aid = aid;
		this.address = address;
		this.latitude = latitude;
		this.location = location;
	}



	public addressBean(double latitude, double location, String address) {
		super();
		this.latitude = latitude;
		this.location = location;
		this.address = address;
	}

    public void setDatas(double latitude, double location, String address)
    {
    	this.latitude = latitude;
		this.location = location;
		this.address = address;
    }

	public int getAid() {
		return aid;
	}



	public void setAid(int aid) {
		this.aid = aid;
	}



	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}



	public addressBean() {
		// TODO Auto-generated constructor stub
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLocation() {
		return location;
	}
	public String getAddress() {
		return address;
	}
  
}
