package com.loe.logger.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager选项卡基类
 *
 * @author zls
 * @version 1.0
 * @time 2016-4-16下午3:33:04
 */
public abstract class LoggerViewPagerItem
{
    public ViewGroup viewGroup;
    public View view;
    public Bundle args;
    public Fragment fragment;
    public int id = 0;

    public abstract void selectIn();

    public abstract void selectMove();

    public LoggerViewPagerItem addArg(String key, int value)
    {
        if (args == null)
        {
            args = new Bundle();
        }
        args.putInt(key, value);
        return this;
    }

    public LoggerViewPagerItem addArg(String key, String value)
    {
        if (args == null)
        {
            args = new Bundle();
        }
        args.putString(key, value);
        return this;
    }

    public LoggerViewPagerItem addArg(String key, boolean value)
    {
        if (args == null)
        {
            args = new Bundle();
        }
        args.putBoolean(key, value);
        return this;
    }

    public LoggerViewPagerItem addArg(String key, double value)
    {
        if (args == null)
        {
            args = new Bundle();
        }
        args.putDouble(key, value);
        return this;
    }
}