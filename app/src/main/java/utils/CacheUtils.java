package utils;

import android.content.Context;

/**
 * Created by clever boy on 2017/4/27.
 */

public class CacheUtils {
    public static void setCache(String url, String json, Context ctx){
        PrefUtils.putString(url,json,ctx);
    }
    public static String getCache(String url,Context ctx){
        return PrefUtils.getString(url,null,ctx);
    }
}
