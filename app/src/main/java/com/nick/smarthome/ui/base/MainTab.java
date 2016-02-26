package com.nick.smarthome.ui.base;


import com.nick.smarthome.R;
import com.nick.smarthome.ui.fragment.MyInformationFragment;
import com.nick.smarthome.ui.fragment.NearbyTestFragment;
import com.nick.smarthome.ui.fragment.SearchFragment;

public enum MainTab {

	NEARBY(0, R.string.main_tab_name_nearby, R.drawable.tab_icon_nearby,
			NearbyTestFragment.class),

	SEARCH(1, R.string.main_tab_name_search, R.drawable.tab_icon_search,
			SearchFragment.class),

	ME(2, R.string.main_tab_name_my, R.drawable.tab_icon_me,
			MyInformationFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
