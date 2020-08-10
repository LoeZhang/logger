package com.loe.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.loe.logger.LoeLogger
import com.loe.logger.util.LoggerTools
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd.setOnClickListener()
        {
            LoeLogger.net("sdsddssd" + Math.random(), "sds sdsd sdsjsdh可接受的", "{'name':'顺哥哥'}")
        }
        buttonJump.setOnClickListener()
        {
            LoeLogger.d("{'name':'大地撒大声地'}")
        }
    }

    ////////////////////////////////////////////// logger ////////////////////////////////////////////////

    private var touchCount = 1
    private var lastTouchTime = 0L
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean
    {
        if (ev?.action == MotionEvent.ACTION_DOWN)
        {
            if (System.currentTimeMillis() - lastTouchTime < 400)
            {
                touchCount++
                if (touchCount == 3 && ev?.rawY < LoggerTools.dp_px(80.0))
                {
                    LoeLogger.toLogger(this)
                }
            } else
            {
                touchCount = 1
            }
            lastTouchTime = System.currentTimeMillis()
        }
        if (ev?.action == MotionEvent.ACTION_MOVE)
        {
            touchCount = 0
        }
        return super.dispatchTouchEvent(ev)
    }
}