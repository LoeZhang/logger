package com.loe.test

import android.app.Application
import com.loe.logger.LoeLogger

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        LoeLogger.init(this, false)
    }
}