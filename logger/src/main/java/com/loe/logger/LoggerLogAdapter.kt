package com.loe.logger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.loe.logger.util.LoggerTimeUtil
import com.loe.logger.util.LoggerTools

class LoggerLogAdapter(private val context: Context, var list: List<LoggerLogFragment.Bean>) :
    BaseAdapter()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var onItemClickListener: AdapterView.OnItemClickListener? = null

    var selectId = ""

    override fun getCount(): Int
    {
        return list.size
    }

    override fun getItem(position: Int): Any
    {
        return list[position]
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View
    {
        var view = view
        val bean = list[position]

        val item: Item
        if (view == null)
        {
            item = Item()
            // 获取Item布局构造view
            view = inflater.inflate(R.layout.logger_log_item, null)
            // 获取布局子元素
            item.textMsg = view!!.findViewById(R.id.textMsg) as TextView
            item.textTime = view!!.findViewById(R.id.textTime) as TextView
            // 将Item绑定到view里
            view.tag = item
        } else
        {
            // 如果view已被构造，则直接获取绑定Item
            item = view.tag as Item
        }
        // 显示
        item.textMsg?.text = bean.tag + "：" + bean.msg
        item.textTime?.text = LoggerTimeUtil.dynamicTimeFormat(bean.time)
        item.textMsg?.setTextColor(if(bean.type == "e") LoggerTools.mainColor else LoggerTools.textColor)
        (view as ViewGroup).getChildAt(0).setBackgroundColor(if (bean.id == selectId) LoggerTools.selectColor else LoggerTools.whiteColor)

        return view
    }

    internal inner class Item
    {
        var textMsg: TextView? = null
        var textTime: TextView? = null
    }
}


