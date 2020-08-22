package com.loe.logger

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.loe.logger.json.JsonActivity
import com.loe.logger.util.LoggerTools
import kotlinx.android.synthetic.main.logger_log_fragment.*
import org.json.JSONObject

class LoggerLogFragment : Fragment(), View.OnClickListener
{
    class Bean(json: JSONObject)
    {
        var id: String = json.optString("id")
        var type: String = json.optString("type")
        var tag: String = json.optString("tag")
        var msg: String = json.optString("msg")
        var msgJson: String? = null
        var time: Long = json.optLong("time")
    }

    protected lateinit var inflater: LayoutInflater

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        this.inflater = inflater
        return inflater.inflate(R.layout.logger_log_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initInfo()
        loadData()
    }

    private lateinit var list: java.util.ArrayList<Bean>
    private lateinit var adapter: LoggerLogAdapter

    fun loadData()
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

        if (list.size > 30)
        {
            LoeLogger.clear(false, true)
        }
    }

    private fun initList()
    {
        list = ArrayList()
        adapter = LoggerLogAdapter(activity!!, list)
        listView.adapter = adapter
    }

    private fun initInfo()
    {
        buttonJson.setOnClickListener()
        {
            startActivity(Intent(activity!!, JsonActivity::class.java))
        }
        listView.setOnItemClickListener()
        { adapterView, view, i, l ->
            select(i)
        }

        textMsg.setOnClickListener(this)
    }

    private fun select(i: Int)
    {
        val bean = list[i]
        adapter.selectId = bean.id
        textTag.text = bean.tag + "："
        textMsg.text = bean.msg
        if (bean.msgJson == null)
        {
            try
            {
                bean.msgJson = JSONObject(bean.msg).toString(4)
            } catch (e: Exception)
            {
                bean.msgJson = bean.msg
            }
        }
        textMsg.text = bean.msgJson
        JsonActivity.putJson(bean.msg)
        adapter.notifyDataSetChanged()
    }

    override fun onClick(view: View)
    {
        if (view.tag == null) view.tag = 0L

        val lastTime = view.tag as Long
        if (System.currentTimeMillis() - lastTime < 500)
        {
            LoggerTools.copyToClipboard(activity!!, (view as TextView).text.toString())
            LoggerTools.show(activity!!, "已复制到剪切板")
            view.tag = 0L
        } else
        {
            view.tag = System.currentTimeMillis()
        }
    }
}