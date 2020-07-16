package com.loe.logger.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoggerSharedManager
{
	public static SharedPreferences sp;

	public static void init(Context context)
	{
        if(sp == null) init(context, "user", Context.MODE_PRIVATE);
	}

	public static void init(Context context, String name)
	{
        if(sp == null) init(context, name, Context.MODE_PRIVATE);
	}

	public static void init(Context context, String name, int mode)
	{
	    if(sp == null) sp = context.getSharedPreferences(name, mode);
	}

	/** 获取存储字符串 */
	public static String getString(String key)
	{
		return sp.getString(key, "");
	}

	/** 获取存储字符串 */
	public static String getString(String key, String dft)
	{
		return sp.getString(key, dft);
	}

	/** 录入存储字符串 */
	public static void putString(String key, String value)
	{
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/** 获取存储整数 */
	public static int getInt(String key)
	{
		return sp.getInt(key, 0);
	}

	/** 获取存储整数 */
	public static int getInt(String key, int defValue)
	{
		return sp.getInt(key, defValue);
	}

	/** 录入存储整数 */
	public static void putInt(String key, int value)
	{
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/** 获取存储长整数 */
	public static long getLong(String key)
	{
		return sp.getLong(key, 0);
	}

	/** 录入存储长整数 */
	public static void putLong(String key, long value)
	{
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/** 获取存储小数 */
	public static float getFloat(String key)
	{
		return sp.getFloat(key, 0);
	}

	/** 录入存储小数 */
	public static void putFloat(String key, float value)
	{
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/** 获取存储双小数 */
	public static double getDouble(String key)
	{
		return Double.parseDouble(sp.getString(key, "0"));
	}

	/** 录入存储双小数 */
	public static void putDouble(String key, double value)
	{
		Editor editor = sp.edit();
		editor.putString(key, value+"");
		editor.commit();
	}

	/** 获取存储布尔值 */
	public static boolean getBoolean(String key)
	{
		return sp.getBoolean(key, false);
	}

	/** 录入存储布尔值 */
	public static void putBoolean(String key, boolean value)
	{
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void clear()
	{
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
}
