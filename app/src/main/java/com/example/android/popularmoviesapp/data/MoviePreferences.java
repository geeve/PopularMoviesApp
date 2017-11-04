package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/10/21 0021.
 * com.example.android.popularmoviesapp.data,PopularMoviesApp
 */

public final class MoviePreferences {

    /**区分电影列表排序方式，0为按受欢迎程度排序，1为按评分，2为按收藏排序*/
    public static final String PREF_ORDER_BY = "order_by";

    public static final int PREF_ORDER_BY_POP = 0;
    public static final int PREF_ORDER_BY_VOTE = 1;
    public static final int PREF_ORDER_BY_FARI = 2;

    /***
     * 设置排序偏好的值
     * @param context Context used to get the SharedPreferences
     * @param orderBy 排序要求
     */
    public static void setPrefOrderBy(Context context, int orderBy){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        switch (orderBy){
            case PREF_ORDER_BY_POP:
                editor.putString(PREF_ORDER_BY,String.valueOf(PREF_ORDER_BY_POP));
                break;
            case PREF_ORDER_BY_VOTE:
                editor.putString(PREF_ORDER_BY,String.valueOf(PREF_ORDER_BY_VOTE));
                break;
            case PREF_ORDER_BY_FARI:
                editor.putString(PREF_ORDER_BY,String.valueOf(PREF_ORDER_BY_FARI));
                break;
            default:
                editor.putString(PREF_ORDER_BY,String.valueOf(PREF_ORDER_BY_POP));
        }

        editor.apply();
    }

    /***
     * 获得排序偏好设定值
     * @param context
     * @return
     */
    public static int getPrefOrderBy(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return Integer.valueOf(sp.getString(PREF_ORDER_BY,"0"));
    }

}
