package punchclock.bean;

public class remindBean {
	public String text;
	/**
	 * 选中状态
	 */
	public boolean selectstate=false;
	public int index;


	public remindBean(String text, boolean selectstate, int index) {
		this.text = text;
		this.selectstate = selectstate;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
