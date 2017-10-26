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
 * Created by Administrator on 2017/10/22 0022.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class ReviewsAsyncTaskLoader extends AsyncTaskLoader<ContentValues[]> {

    private String mUrl;

    private Context mContext;

    public ReviewsAsyncTaskLoader(Context context,String url) {
        super(context);

        this.mContext = context;
        this.mUrl = url;
    }

    @Override
    public ContentValues[] loadInBackground() {
        if(TextUtils.isEmpty(mUrl)){
            return null;
        }
        ContentValues[] contentValues = null;
        try {
            String jsonResponse = NetWorkUtils.makeHttpRequest(NetWorkUtils.createUrl(mUrl));

            contentValues = OpenMovieJsonUtils.getMovieReviewsFromJson(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contentValues;
    }
}
