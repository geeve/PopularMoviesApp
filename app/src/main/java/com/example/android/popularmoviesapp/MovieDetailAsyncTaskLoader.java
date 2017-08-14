package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieDetailAsyncTaskLoader extends AsyncTaskLoader<Movie> {
    private String mUrl;

    public MovieDetailAsyncTaskLoader(Context context,String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public Movie loadInBackground() {

        if(TextUtils.isEmpty(mUrl)){
            return null;
        }

        return MovieListUtil.fetchMovieData(mUrl,true).get(0);
    }
}
