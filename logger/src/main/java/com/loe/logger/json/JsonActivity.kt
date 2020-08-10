package com.loe.logger.json

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.loe.logger.R
import com.loe.logger.db.LoggerSharedManager
import com.loe.logger.util.LoggerTools
import kotlinx.android.synthetic.main.logger_json_activity.*
import java.lang.Exception

class JsonActivity : AppCompatActivity()
{
    companion object
    {
        val DATA_KEY = "jsonRecyclerView-data"

        fun putJson(json: String)
        {
            LoggerSharedManager.putString(DATA_KEY, json)
        }
    }

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
        setContentView(R.layout.logger_json_activity)

        buttonBack.setOnClickListener { finish() }

        LoggerSharedManager.init(applicationContext)
        try
        {
            jsonRecyclerView.bindJson(LoggerSharedManager.getString(DATA_KEY, "{}"))
        }catch (e:Exception)
        {}
    }
}
