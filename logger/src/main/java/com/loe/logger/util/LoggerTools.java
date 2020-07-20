package com.loe.logger.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.Toast;

/**
 * 类注释
 *
 * @author Administrator
 * @since 2020/7/16-11:18
 */
public class LoggerTools
{
    public static final int mainColor = Color.parseColor("#dd2222");
    public static final int yellowColor = Color.parseColor("#ffffaa");
    public static final int textColor = Color.parseColor("#444444");
    public static final int selectColor = Color.parseColor("#f5f6fa");
    public static final int whiteColor = Color.parseColor("#ffffff");

    /**
     * 复制文本
     */
    public static void copyToClipboard(Context context, String text)
    {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
    }

    public static void show(Context context, String text)
    {
        if(text!=null && !text.isEmpty()) Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static double dp_px(double dp)
    {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }
}
