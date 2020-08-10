package com.loe.logger

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.loe.logger.util.LoggerTools
import com.loe.logger.viewpager.LoggerFragTextItem
import kotlinx.android.synthetic.main.logger_activity.*

class LoggerActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // 状态栏透明 >=6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = LoggerTools.mainColor
        }
        setContentView(R.layout.logger_activity)

        initView()
    }

    private fun initView()
    {
        buttonClear.setOnClickListener()
        {
            when(viewPager.index)
            {
                0 ->
                {
                    LoeLogger.clearNet()
                    (viewPager.nowFragment as LoggerNetFragment).loadData()
                }
                1 ->
                {
                    LoeLogger.clear()
                    (viewPager.nowFragment as LoggerLogFragment).loadData()
                }
            }
        }
        buttonBack.setOnClickListener()
        {
            finish()
        }

        viewPager.init(supportFragmentManager, viewLine, 0.5,
            LoggerFragTextItem(LoggerNetFragment::class.java, menu0, LoggerTools.textColor, LoggerTools.mainColor),
            LoggerFragTextItem(LoggerLogFragment::class.java, menu1, LoggerTools.textColor, LoggerTools.mainColor)
        )
    }
}