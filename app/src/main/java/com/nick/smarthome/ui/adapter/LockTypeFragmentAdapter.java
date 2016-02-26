package com.nick.smarthome.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nick.smarthome.ui.fragment.PrivateLockFragment;
import com.nick.smarthome.ui.fragment.PublicLockFragment;

public class LockTypeFragmentAdapter extends FragmentPagerAdapter {

	PrivateLockFragment privateLockFragment;
	private int pageCount=2;
	public LockTypeFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public LockTypeFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
	}
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			if (privateLockFragment != null) {
				return privateLockFragment;
			}else{
				privateLockFragment = PrivateLockFragment.newInstance(position);
				return privateLockFragment;//私用
			}
		case 1:
			return PublicLockFragment.newInstance(position);//公用
		}
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	@Override
	public int getCount() {
		return pageCount;
	}

}
