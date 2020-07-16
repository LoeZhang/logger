package com.loe.test

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.loe.logger.LoeLogger
import com.loe.logger.util.LoggerTools
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd.setOnClickListener()
        {
            val json = JSONObject()
                .put("url","sdsddssd"+Math.random())
                .put("params", "sds sdsd sdsjsdh可接受的")
                .put("result", "{'name':'顺哥哥'}")
            LoeLogger.add(json)
        }
        buttonJump.setOnClickListener()
        {
            LoeLogger.toLogger(this)
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