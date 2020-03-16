package com.kingbogo.superplayer.demo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2020-03-16
 */
public class ToastUtil {
    
    public static void toast(Context context, String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
}
