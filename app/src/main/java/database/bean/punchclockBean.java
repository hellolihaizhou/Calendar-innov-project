package database.bean;

import java.io.Serializable;

/**
 * Created by lihaizhou on 16-6-3. This class is used to store the data for each row in ListView
 */
public class punchclockBean implements Serializable {
	/**
	 * 图片资源下标
	 */
	private int imageindex;
	/**
	 * 打卡标题
	 */
	private String title;

	/**
	 * 开始日期
	 */
	private String startdate;

	/**
	 * 结束日期
	 */
	private String enddate;

	private String repetition;
	/**
	 * 重复
	 */


	/**
	 * 提醒时间
	 */
	private String  time;


	/**
	 * 打卡状态
	 */
	private int state;

	private int pid;

	private boolean isremind;


	/**加载首页
	 * @param imageindex 图片下标
	 * @param title     打卡标题
	 * @param pid    id
	 */
	public punchclockBean(int imageindex, String title, int pid, int state) {
		super();
		this.imageindex = imageindex;
		this.title = title;
		this.pid = pid;
		this.state=state;
	}


	/**
	 * @param imageindex 图片下标
	 * @param title     打卡标题
	 * @param startdate  开始日期
	 * @param enddate  结束日期
	 * @param repetition    重复
	 * @param time     提醒时间
	 */
	public punchclockBean(int imageindex, String title, String startdate, String enddate, String repetition, String time, boolean isremind) {
		super();
		this.imageindex = imageindex;
		this.title = title;
		this.startdate = startdate;
		this.enddate = enddate;
		this.repetition = repetition;
		this.time = time;
		this.isremind=isremind;
	}




	public boolean isIsremind() {
		return isremind;
	}


	public void setIsremind(boolean isremind) {
		this.isremind = isremind;
	}


	/**提醒线程
	 * @param title     打卡标题
	 * @param time     提醒时间
	 * @param state   状态
	 * @param pid    id
	 */
	public punchclockBean(String title, String time, int state, int pid) {
		super();
		this.title = title;
		this.time = time;
		this.state = state;
		this.pid = pid;
	}




	public punchclockBean(int imageindex) {
		super();
		this.imageindex = imageindex;
	}


	public void setState(int state) {
		this.state = state;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public int getPid() {
		return pid;
	}


	public int getState() {
		return state;
	}


	public int getImageId() {
		return imageindex;
	}
	public void setImageId(int imageId) {
		this.imageindex = imageId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImageindex() {
		return imageindex;
	}
	public String getStartdate() {
		return startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public String getRepetition() {
		return repetition;
	}
	public String getTime() {
		return time;
	}


	public void setRepetition(String repetition) {
		this.repetition = repetition;
	}


}

