package com.loe.logger

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.loe.logger.db.LoggerSharedManager
import com.loe.logger.json.JsonActivity
import com.loe.logger.util.LoggerTools
import kotlinx.android.synthetic.main.logger_activity.*
import org.json.JSONObject

class LoggerActivity : AppCompatActivity(), View.OnLongClickListener
{
    class Bean(json: JSONObject)
    {
        var id: String = json.optString("id")
        var url: String = json.optString("url")
        var params: String = json.optString("params")
        var result: String = json.optString("result")
        var resultJson: String? = null
        var time: Long = json.optLong("time")
    }

    private lateinit var list: java.util.ArrayList<Bean>
    private lateinit var adapter: LoggerAdapter

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

        initList()
        initInfo()
        loadData()
    }

    private fun loadData()
    {
        list.clear()
        val js = LoeLogger.select()
        for (i in 0 until js.length())
        {
            list.add(Bean(js.optJSONObject(i)))
        }
        adapter.notifyDataSetChanged()

        if (list.size > 0)
        {
            select(0)
        }

        if(list.size > 20)
        {
            LoeLogger.clear(false, true)
        }
    }

    private fun initList()
    {
        list = ArrayList()
        adapter = LoggerAdapter(this, list)
        listView.adapter = adapter
    }

    private fun initInfo()
    {
        buttonJson.setOnClickListener()
        {
            startActivity(Intent(this, JsonActivity::class.java))
        }
        listView.setOnItemClickListener()
        { adapterView, view, i, l ->
            select(i)
        }

        buttonClear.setOnClickListener()
        {
            LoeLogger.clear()
            loadData()
        }
        buttonBack.setOnClickListener()
        {
            finish()
        }

        textUrl.setOnLongClickListener(this)
        textParams.setOnLongClickListener(this)
        textResult.setOnLongClickListener(this)
    }

    private fun select(i: Int)
    {
        val bean = list[i]
        adapter.selectId = bean.id
        textUrl.text = bean.url
        textParams.text = bean.params
        if (bean.resultJson == null)
        {
            try
            {
                bean.resultJson = JSONObject(bean.result).toString(4)
            } catch (e: Exception)
            {
                bean.resultJson = bean.result
            }

        }
        textResult.text = bean.resultJson
        LoggerSharedManager.init(applicationContext)
        JsonActivity.putJson(bean.result)
        adapter.notifyDataSetChanged()
    }

    override fun onLongClick(view: View?): Boolean
    {
        LoggerTools.copyToClipboard(this, (view as TextView).text.toString())
        LoggerTools.show(this, "已复制到剪切板")
        return false
    }
}