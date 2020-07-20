package com.loe.logger.viewpager;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class LoggerSlideViewPager extends ViewPager implements OnClickListener
{
    private List<LoggerViewPagerItem> items;
    private FragmentManager fragmentManager;
    private LoggerFragAdapter pagerAdapter;
    private View line;
    private LinearLayout.LayoutParams params;
    private int W;
    public int index = 0;
    private double wRate;
    private int delW;
    /**
     * 是否可滑动
     */
    private boolean canMove = true;

    public LoggerSlideViewPager(Context context)
    {
        super(context);
    }

    public LoggerSlideViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void init(FragmentManager fragmentManager, final View line, LoggerViewPagerItem... items)
    {
        init(fragmentManager, line, 1, items);
    }

    public void init(FragmentManager fragmentManager, final View line, final double rate, LoggerViewPagerItem... items)
    {
        this.items = Arrays.asList(items);
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() == items.length)
        {
            for (int i = 0; i < fragments.size(); i++)
            {
                this.items.get(i).fragment = fragments.get(i);
            }
        }
        this.fragmentManager = fragmentManager;
        this.wRate = rate;
        // 初始化布局元素
        initItems();
        // 初始化ViewPager
        initPager();
        // 初始化item宽度
        this.line = line;
        params = (LinearLayout.LayoutParams) line.getLayoutParams();
        line.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        W = line.getWidth() / LoggerSlideViewPager.this.items.size();
                        delW = (int) (W * (1 - wRate) / 2);
                        params.width = (int) (W * wRate);
                        line.setLayoutParams(params);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        {
                            line.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
        onAttachedToWindow();
    }

    /**
     * 防止后台清理导致的下标不对应
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (line != null)
        {
            line.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                    {
                        @Override
                        public void onGlobalLayout()
                        {
                            setPosition(index * W);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            {
                                line.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                    });
        }

    }

    /**
     * 初始化item元素事件属性
     */
    private void initItems()
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.get(i).id = i;
            if (items.get(i).viewGroup != null)
            {
                items.get(i).viewGroup.setOnClickListener(this);
            }
            else
            {
                items.get(i).view.setOnClickListener(this);
            }
        }
    }

    /**
     * 初始化ViewPager及监听事件
     */
    private void initPager()
    {
        pagerAdapter = new LoggerFragAdapter(fragmentManager, items);
        setAdapter(pagerAdapter);
        // 设置切换效果
        //setPageTransformer(true, new RotatePageTransformer());
        // 设置缓存视图个数
        setOffscreenPageLimit(items.size() - 1);

        addOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int index)
            {
                selectItem(index);
                LoggerSlideViewPager.this.index = index;
            }

            @Override
            public void onPageScrolled(int i, float scale, int arg2)
            {
                setPosition((int) ((i + scale) * W));
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
        // 第一次启动时选中第0个Fragment
        selectItem(0);
    }

    public void addOnPageSelectedListener(final IndexCallBack callBack)
    {
        addOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int index)
            {
                callBack.logic(index);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
    }

    /**
     * 根据view设置选中的Fragment页
     */
    @Override
    public void onClick(View view)
    {
        for (LoggerViewPagerItem fitem : items)
        {
            if (view.equals(fitem.viewGroup) || view.equals(fitem.view))
            {
                // 更新选项卡
                selectItem(fitem.id);
                // 更新页面
                setCurrentItem(fitem.id);
            }
        }
    }

    /**
     * 设置滑动位置
     */
    private void setPosition(int position)
    {
        params.setMargins(position + delW, 0, 0, 0);
        line.setLayoutParams(params);
    }

    /**
     * 选择页面
     */
    public void select(int i)
    {
        // 更新选项卡
        selectItem(i);
        // 更新页面
        setCurrentItem(i);
    }

    public LoggerViewPagerItem get(int index)
    {
        return items.get(index);
    }

    /**
     * 建立单个选中状态
     */
    private void selectItem(int index)
    {
        // 清除所有选择状态
        for (LoggerViewPagerItem item : items)
        {
            item.selectMove();
        }
        // 添加单个选择状态
        items.get(index).selectIn();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (canMove)
        {
            return super.onInterceptTouchEvent(ev);
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (canMove)
        {
            return super.onTouchEvent(ev);
        }
        else
        {
            return false;
        }
    }

    public Fragment getNowFragment()
    {
        return get(index).fragment;
    }

    /**
     * 设置是否可滑动
     */
    public void setCanMove(boolean canMove)
    {
        this.canMove = canMove;
    }

    public interface IndexCallBack
    {
        void logic(int i);
    }
}