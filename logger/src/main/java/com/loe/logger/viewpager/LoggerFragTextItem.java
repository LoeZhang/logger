package com.loe.logger.viewpager;

import android.support.v4.app.Fragment;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class LoggerFragTextItem extends LoggerViewPagerItem
{
    private Class clas;

    private TextView textView;

    private int unSelectColor;
    private int selectColor;

    public LoggerFragTextItem(Class clas, TextView textView, int color0, int color1)
    {
        this.clas = clas;
        this.view = textView;
        this.textView = textView;

        unSelectColor = color0;
        selectColor = color1;

        initFragment();
    }

    public void initFragment()
    {
        try
        {
            this.fragment = (Fragment) clas.newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 当点击了消息tab时，改变控件的图片和文字颜色
     */
    @Override
    public void selectIn()
    {
        textView.setTextColor(selectColor);

    }

    /**
     * 当取消了消息tab时，恢复控件的图片和文字颜色
     */
    @Override
    public void selectMove()
    {
        textView.setTextColor(unSelectColor);
    }
}
