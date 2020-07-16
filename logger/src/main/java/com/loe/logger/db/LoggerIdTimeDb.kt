package com.loe.logger.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class LoggerIdTimeDb(context: Context, val name: String)
{
    init
    {
        init(context)

        try
        {
            go("""
                CREATE TABLE $name ( 
                    id      VARCHAR(128) PRIMARY KEY      NOT NULL,
                    time    INTEGER(13),
                    data    VARCHAR
                );
            """.trimIndent())
        } catch (e: Exception)
        {
        }
    }

    fun insert(bean: LoggerIdTimeBean): Long
    {
        return insert(bean.id, bean.data)
    }

    fun insert(data: JSONObject): Long
    {
        return insert(System.currentTimeMillis().toString() + "", data)
    }

    fun insert(id: String, data: JSONObject): Long
    {
        val values = ContentValues()
        values.put("id", id)
        values.put("time", System.currentTimeMillis())
        values.put("data", data.toString())
        return insert(values)
    }

    /**
     * 插入元素
     */
    fun insert(values: ContentValues): Long
    {
        return db!!.insert(name, null, values)
    }

    fun replace(bean: LoggerIdTimeBean): Long
    {
        return replace(bean.id, bean.data)
    }

    fun replace(id: String, data: JSONObject): Long
    {
        val values = ContentValues()
        values.put("id", id)
        values.put("time", System.currentTimeMillis())
        values.put("data", data.toString())
        return replace(values)
    }

    /**
     * 替换元素
     */
    fun replace(values: ContentValues): Long
    {
        return db!!.replace(name, null, values)
    }

    fun update(bean: LoggerIdTimeBean): Long
    {
        return updateAndTime(bean.id, bean.time, bean.data)
    }

    fun update(id: String, data: JSONObject): Long
    {
        val values = ContentValues()
        values.put("data", data.toString())
        return update(values, "id=?", id)
    }

    fun updateAndTime(id: String, time: Long, data: JSONObject): Long
    {
        val values = ContentValues()
        values.put("time", time)
        values.put("data", data.toString())
        return update(values, "id=?", id)
    }

    /**
     * 更新元素
     */
    fun update(values: ContentValues, whereClause: String, vararg strings: String): Long
    {
        return db!!.update(name, values, whereClause, strings).toLong()
    }

    fun delete(bean: LoggerIdTimeBean): Long
    {
        return delete(bean.id)
    }

    fun delete(id: String): Long
    {
        return delete("id=?", id)
    }

    /**
     * 删除元素
     */
    fun delete(whereClause: String, vararg strings: String): Long
    {
        return db!!.delete(name, whereClause, strings).toLong()
    }

    /**
     * 清除元素
     */
    fun clear()
    {
        db!!.execSQL("delete from $name")
    }

    /**
     * 删除表
     */
    fun drop()
    {
        db!!.execSQL("drop table $name")
    }

    fun go(sql: String)
    {
        db!!.execSQL(sql)
    }

    /**
     * 查询元素
     */
    fun select(isDesc: Boolean, limit: Int): JSONArray
    {
        val limitString = if (limit > 0) " limit $limit" else ""
        val desc = if (isDesc) " desc" else ""
        val list = JSONArray()
        val js = query(String.format("select * from %s order by time$desc$limitString", name))
        for (i in 0 until js.length())
        {
            val json = js.optJSONObject(i)
            try
            {
                list.put(
                    JSONObject(json.optString("data"))
                        .put("id", json.optString("id"))
                        .put("time", json.optLong("time"))
                )
            } catch (e: JSONException)
            {
            }

        }
        return list
    }

    /**
     * 查询元素
     */
    fun select(limit: Int): JSONArray
    {
        return select(true, limit)
    }

    /**
     * 查询元素
     */
    @JvmOverloads
    fun selectAsList(isDesc: Boolean = true): ArrayList<LoggerIdTimeBean>
    {
        val desc = if (isDesc) " desc" else ""
        val list = ArrayList<LoggerIdTimeBean>()
        val js = query(
            String.format(
                "select * from %s order by time$desc",
                name
            )
        )
        for (i in 0 until js.length())
        {
            val json = js.optJSONObject(i)
            try
            {
                list.add(
                    LoggerIdTimeBean(
                        json.optString("id"), json.optLong("time"),
                        JSONObject(json.optString("data"))
                    )
                )
            } catch (e: JSONException)
            {
            }

        }
        return list
    }

    /**
     * 查询元素
     */
    fun select(id: String): LoggerIdTimeBean?
    {
        val js = query(
            String.format(
                "select * from %s where id='%s'",
                name, id
            )
        )
        if (js.length() > 0)
        {
            val json = js.optJSONObject(0)
            try
            {
                return LoggerIdTimeBean(
                    json.optString("id"), json.optLong("time"),
                    JSONObject(json.optString("data"))
                )
            } catch (e: JSONException)
            {
            }

        }
        return null
    }

    /**
     * 查询元素
     */
    @JvmOverloads
    fun query(sql: String = "select * from $name"): JSONArray
    {
        val js = JSONArray()
        var cursor: Cursor? = null
        try
        {
            cursor = db!!.rawQuery(sql, null)
            val columnNames = cursor!!.columnNames
            if (cursor.count != 0)
            {
                while (cursor.moveToNext())
                {
                    val json = JSONObject()
                    for (i in columnNames.indices)
                    {
                        json.put(columnNames[i], cursor.getString(i))
                    }
                    // 添加json到列表
                    js.put(json)
                }
            }
            cursor.close()
        } catch (e: Exception)
        {
            if (cursor != null && !cursor.isClosed)
            {
                cursor.close()
            }
        }

        return js
    }

    /**
     * 查询元素
     */
    fun query(sql: String, vararg strings: String): JSONArray
    {
        val js = JSONArray()
        var cursor: Cursor? = null
        try
        {
            cursor = db!!.rawQuery(sql, strings)
            val columnNames = cursor!!.columnNames
            if (cursor.count != 0)
            {
                while (cursor.moveToNext())
                {
                    val json = JSONObject()
                    for (i in columnNames.indices)
                    {
                        json.put(columnNames[i], cursor.getString(i))
                    }
                    // 添加json到列表
                    js.put(json)
                }
            }
            cursor.close()
        } catch (e: Exception)
        {
            if (cursor != null && !cursor.isClosed)
            {
                cursor.close()
            }
        }

        return js
    }

    /**
     * 开启事物
     */
    fun goTransaction(callBack: ()->Unit): Boolean
    {
        //开启事务
        db!!.beginTransaction()
        try
        {
            callBack()
            //设置事务标志为成功，当结束事务时就会提交事务
            db!!.setTransactionSuccessful()
        } catch (e: Exception)
        {
            return false
        } finally
        {
            //结束事务
            db!!.endTransaction()
            return true
        }
    }

    companion object
    {
        private var db: SQLiteDatabase? = null

        fun init(context: Context)
        {
            if (db == null)
            {
                db = openOrCreate(context, "idTime")
            }
        }

        /**
         * 创建或打开数据库
         */
        fun openOrCreate(context: Context, name: String): SQLiteDatabase
        {
            return openOrCreate(context.filesDir.toString(), name)
        }

        /**
         * 创建或打开数据库
         */
        fun openOrCreate(fileDir: String, name: String): SQLiteDatabase
        {
            return SQLiteDatabase.openOrCreateDatabase("$fileDir/$name.db", null)
        }
    }
}