package com.loe.logger

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.loe.logger.db.LoggerIdTimeDb
import org.json.JSONArray
import org.json.JSONObject

/**
 * Logger管理器
 *
 * @author Administrator
 * @since 2020/7/10-16:52
 */
object LoeLogger
{
    private var db: LoggerIdTimeDb? = null

    fun init(app: Application, isRelease: Boolean)
    {
        if (!isRelease)
        {
            db = LoggerIdTimeDb(app, "appLogger")
        }
    }

    fun add(data: JSONObject)
    {
        if (db != null)
        {
            db?.insert(data)
            clearCount++
            clear(false)
        }
    }

    fun select(): JSONArray
    {
        return db?.select(50) ?: JSONArray()
    }

    private var clearCount = 0

    fun clear(isForce: Boolean = true, is16:Boolean = false)
    {
        if(is16) clearCount = 16
        if (isForce)
        {
            db?.clear()
            clearCount = 0
        } else if (clearCount > 15)
        {
            if (db != null)
            {
                val sql = """
                delete from ${db!!.name} where
                (select count(id) from ${db!!.name} )> 15 and
                   id in (select id from ${db!!.name} order by time desc limit
                     (select count(id) from ${db!!.name}) offset 15 )
                """.trimIndent()
                db!!.go(sql)
                clearCount = 0
            }
        }
    }

    fun toLogger(context: Context)
    {
        if (db != null) context.startActivity(Intent(context, LoggerActivity::class.java))
    }
}