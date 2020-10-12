package com.loe.logger.util

import android.content.Context
import android.content.res.Resources
import android.view.MotionEvent
import com.loe.logger.LoeLogger
import kotlin.math.pow

class LoeDoubleTouch(private val context: Context)
{
    private var touchCount = 1
    private var lastTouchTime = 0L
    private var lastX = 0.0
    private var lastY = 0.0
    private val range = px(10).toDouble().pow(2)

    fun dispatchTouchEvent(e: MotionEvent)
    {
        when (e.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
            }
            MotionEvent.ACTION_UP ->
            {
                if (System.currentTimeMillis() - lastTouchTime < 500)
                {
                    touchCount++
                    if (touchCount == 3 && e.rawY < LoggerTools.dp_px(100.0))
                    {
                        LoeLogger.toLogger(context)
                    }
                } else
                {
                    touchCount = 1
                }
                lastTouchTime = System.currentTimeMillis()
            }
            else ->
            {
                if((lastX - e.rawX).pow(2) + (lastY - e.rawY).pow(2) > range)
                {
                    touchCount = 0
                }
            }
        }
    }

    private fun px(dp: Int): Int
    {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}