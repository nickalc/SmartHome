package com.nick.smarthome.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * tabhost
 * @author  nick
 * @version 创建时间：2015年12月09日
 * 
 */

public class MyFragmentTabHost extends TabFragmentHost {
	
	private String mCurrentTag;
	
	private String mNoTabChangedTag;
	
	public MyFragmentTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onTabChanged(String tag) {
		
		if (tag.equals(mNoTabChangedTag)) {
			setCurrentTabByTag(mCurrentTag);
		} else {
			super.onTabChanged(tag);
			mCurrentTag = tag;
		}
	}
	
	public void setNoTabChangedTag(String tag) {
		this.mNoTabChangedTag = tag;
	}
}
