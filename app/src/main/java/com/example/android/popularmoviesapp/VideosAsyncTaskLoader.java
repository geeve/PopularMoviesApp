package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/10/26 0026.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class VideosAsyncTaskLoader extends AsyncTaskLoader<ContentValues[]> {

    private String mUrl;
    private Context mContext;

    public VideosAsyncTaskLoader(Context context,String url) {
        super(context);

        this.mUrl = url;
        this.mContext = context;
    }

    @Override
    public ContentValues[] loadInBackground() {
        if(TextUtils.isEmpty(mUrl)){
            return null;
        }

        ContentValues[] contentValues = null;

        String jsonString = null;
        try {
            jsonString = NetWorkUtils.makeHttpRequest(NetWorkUtils.createUrl(mUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            contentValues = OpenMovieJsonUtils.getMovieVideosFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contentValues;
    }
}
