package com.loe.logger.db

import org.json.JSONArray
import org.json.JSONObject

class LoggerIdTimeBean(val id: String, var time: Long, var data: JSONObject)
{

    @JvmOverloads
    constructor(id: String, data: JSONObject = JSONObject()) : this(
        id,
        System.currentTimeMillis(),
        data
    )
    {
    }

    fun put(key: String, value: Any): LoggerIdTimeBean
    {
        data.put(key, value)
        return this
    }

    fun getArray(key: String): JSONArray
    {
        return data.optJSONArray(key)
    }

    fun getBoolean(key: String): Boolean
    {
        return data.optBoolean(key)
    }

    fun getDouble(key: String): Double
    {
        return data.optDouble(key)
    }

    fun getInt(key: String): Int
    {
        return data.optInt(key)
    }

    fun getJson(key: String): JSONObject
    {
        return data.optJSONObject(key)
    }

    fun getLong(key: String): Long
    {
        return data.optLong(key)
    }

    fun getString(key: String): String
    {
        return data.optString(key)
    }

    operator fun get(key: String): Any
    {
        return data.opt(key)
    }

    fun pasteData()
    {
        data.put("id", id)
        data.put("time", time)
    }

    fun upTime()
    {
        time = System.currentTimeMillis()
    }
}