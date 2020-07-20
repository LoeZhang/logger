package com.loe.logger.viewpager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoggerFragAdapter extends FragmentPagerAdapter
{
	private List<Fragment> fragments; // Fragment数组

	public LoggerFragAdapter(FragmentManager fragmentManager,
                             List<LoggerViewPagerItem> items)
	{
		super(fragmentManager);
		fragments = new ArrayList<Fragment>();
		for (LoggerViewPagerItem fitem : items)
		{
			if(fitem.args!=null)
			{
				fitem.fragment.setArguments(fitem.args);
			}
			fragments.add(fitem.fragment);
		}
	}

	@Override
	public Fragment getItem(int position)
	{
		return fragments.get(position);
	}

	@Override
	public int getCount()
	{
		return fragments.size();
	}
}
