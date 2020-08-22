package com.loe.logger

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.loe.logger.db.LoggerIdTimeDb
import com.loe.logger.db.LoggerSharedManager
import com.orhanobut.logger.Logger
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
    private var dbNet: LoggerIdTimeDb? = null

    fun init(app: Application, isRelease: Boolean)
    {
        if (!isRelease)
        {
            db = LoggerIdTimeDb(app, "appLogger")
            dbNet = LoggerIdTimeDb(app, "appLoggerNet")
            LoggerSharedManager.init(app)
        }
    }

    /**
     * 跳转日志界面
     */
    fun toLogger(context: Context)
    {
        if (db != null) context.startActivity(Intent(context, LoggerActivity::class.java))
    }

    /****************************************** net *****************************************/

    /**
     * log网络请求
     */
    fun net(url: String, params: String, result: String)
    {
        net(url, params, "", result)
    }

    fun net(url: String, params: String, headers: String, result: String)
    {
        if (dbNet != null)
        {
            dbNet?.insert(
                JSONObject()
                    .put("url", url)
                    .put("params", params)
                    .put("headers", headers)
                    .put("result", result)
            )
            // log
            Log.d("PRETTYLOGGER", "url：$url")
            if(headers.isNotEmpty()) Log.d("PRETTYLOGGER", "headers：" + headers.replace("\n", " "))
            Log.d("PRETTYLOGGER", "params：" + params.replace("\n", " "))
            try
            {
                Logger.d(JSONObject(result).toString(4))
            } catch (e: Exception)
            {
                Logger.d(result)
            }

            clearCountNet++
            clearNet(false)
        }
    }

    private var clearCountNet = 0

    fun selectNet(): JSONArray
    {
        return dbNet?.select(50) ?: JSONArray()
    }

    fun clearNet(isForce: Boolean = true, is16: Boolean = false)
    {
        if (is16) clearCountNet = 16
        if (isForce)
        {
            dbNet?.clear()
            clearCountNet = 0
        } else if (clearCountNet > 15)
        {
            if (dbNet != null)
            {
                val sql = """
                delete from ${dbNet!!.name} where
                (select count(id) from ${dbNet!!.name} )> 15 and
                   id in (select id from ${dbNet!!.name} order by time desc limit
                     (select count(id) from ${dbNet!!.name}) offset 15 )
                """.trimIndent()
                dbNet!!.go(sql)
                clearCountNet = 0
            }
        }
    }

    /****************************************** log *****************************************/

    /**
     * log调试信息
     */
    fun d(tag: String?, msg: Any?)
    {
        if (db != null)
        {
            val s = "" + msg
            db?.insert(
                JSONObject()
                    .put("msg", s)
                    .put("type", "d")
                    .put("tag", tag?: "logger")
            )
            // log
            Logger.t(tag)
            if(s.startsWith("{"))
            {
                try
                {
                    val json = JSONObject(s).toString(4)
                    Logger.d(json)
                } catch (e: Exception)
                {
                    Logger.d(s)
                }
            }else
            {
                Logger.d(s)
            }
            clearCount++
            clear(false)
        }
    }

    fun d(msg: Any?)
    {
        d(null, msg)
    }

    /**
     * log调试信息
     */
    fun e(tag: String?, msg: Any?)
    {
        val s = "" + msg
        // log
        Logger.t(tag)
        Logger.e(s)
        if (db != null)
        {
            db?.insert(
                JSONObject()
                    .put("msg", msg)
                    .put("type", "e")
                    .put("tag", tag?:"logger")
            )
            clearCount++
            clear(false)
        }
    }

    fun e(msg: Any?)
    {
        e(null, msg)
    }

    private var clearCount = 0

    fun select(): JSONArray
    {
        return db?.select(60) ?: JSONArray()
    }

    fun clear(isForce: Boolean = true, is26: Boolean = false)
    {
        if (is26) clearCount = 26
        if (isForce)
        {
            db?.clear()
            clearCount = 0
        } else if (clearCount > 25)
        {
            if (db != null)
            {
                val sql = """
                delete from ${db!!.name} where
                (select count(id) from ${db!!.name} )> 25 and
                   id in (select id from ${db!!.name} order by time desc limit
                     (select count(id) from ${db!!.name}) offset 25 )
                """.trimIndent()
                db!!.go(sql)
                clearCount = 0
            }
        }
    }
}