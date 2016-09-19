package constellation;

public class Bean {
 private int startdate;
 private int enddate;
 private String date;
 public Bean( int resid,String text,String date) {
	this.date = date;
	this.text = text;
	this.resid = resid;
}
 
public Bean(int resid, String text, String date, int startdate, int enddate) {
	this.resid = resid;
	this.text = text;
	this.date = date;
	this.startdate = startdate;
	this.enddate = enddate;
}

public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public int getResid() {
	return resid;
}
public void setResid(int resid) {
	this.resid = resid;
}

public int getStartdate() {
	return startdate;
}

public int getEnddate() {
	return enddate;
}

private String text;
 private int resid;
 
 
}
