package event.bean;

public class sortsBean {
	public String text;
	/**
	 * 选中状态
	 */
	public boolean selectstate=false;
	public int resid;


	public sortsBean(int resid,String text) {
		this.text = text;
		this.resid = resid;
	}

	public int getIndex() {
		return resid;
	}

	public void setIndex(int index) {
		this.resid = index;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelectstate() {
		return selectstate;
	}
	public void setSelectstate(boolean selectstate) {
		this.selectstate = selectstate;
	}


}
