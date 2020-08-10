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
import kotlinx.android.synthetic.main.logger_net_fragment.*
import org.json.JSONObject

class LoggerNetFragment : Fragment(), View.OnLongClickListener
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

    protected lateinit var inflater: LayoutInflater

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        this.inflater = inflater
        return inflater.inflate(R.layout.logger_net_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initInfo()
        loadData()
    }

    private lateinit var list: java.util.ArrayList<Bean>
    private lateinit var adapter: LoggerNetAdapter

    fun loadData()
    {
        list.clear()
        val js = LoeLogger.selectNet()
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
            LoeLogger.clearNet(false, true)
        }
    }

    private fun initList()
    {
        list = ArrayList()
        adapter = LoggerNetAdapter(activity!!, list)
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
        JsonActivity.putJson(bean.result)
        adapter.notifyDataSetChanged()
    }

    override fun onLongClick(view: View?): Boolean
    {
        LoggerTools.copyToClipboard(activity!!, (view as TextView).text.toString())
        LoggerTools.show(activity!!, "已复制到剪切板")
        return false
    }
}