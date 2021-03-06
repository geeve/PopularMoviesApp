package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;

import static com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils.getMoiveContentValuesFromJson;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieDetailAsyncTaskLoader extends AsyncTaskLoader<Movie> {
    private String mUrl;
    private Context mContext;

    public MovieDetailAsyncTaskLoader(Context context,String url) {
        super(context);
        this.mUrl = url;
        this.mContext  = context;
    }

    @Override
    public Movie loadInBackground() {

        if(TextUtils.isEmpty(mUrl)){
            return null;
        }
        Movie movie = null;
        try {
            String jsonResponse = NetWorkUtils.makeHttpRequest(NetWorkUtils.createUrl(mUrl));

            movie = OpenMovieJsonUtils.extractMovieFromJson(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movie;
    }
}
