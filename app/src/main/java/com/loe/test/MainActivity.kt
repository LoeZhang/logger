package com.loe.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.loe.logger.LoeLogger
import com.loe.logger.util.LoeDoubleTouch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd.setOnClickListener()
        {
            LoeLogger.net("sdsddssd" + Math.random(),
                "sds sdsd sdsjsdh可接受的",
                "GDFGDFG水电费水电费223232",
                "{'name':'顺哥哥'}")
        }
        buttonJump.setOnClickListener()
        {
            LoeLogger.d("{'name':'大地撒大声地'}")
        }
    }

    ////////////////////////////////////////////// logger ////////////////////////////////////////////////

    private var doubleTouch = LoeDoubleTouch(this)

    override fun dispatchTouchEvent(e: MotionEvent): Boolean
    {
        doubleTouch.dispatchTouchEvent(e)
        return super.dispatchTouchEvent(e)
    }
}