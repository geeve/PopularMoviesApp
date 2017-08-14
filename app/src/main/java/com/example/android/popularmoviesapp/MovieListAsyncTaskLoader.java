package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieListAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private String mUrl;

    public MovieListAsyncTaskLoader(Context context,String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        if(TextUtils.isEmpty(mUrl)){
            return null;
        }

        return MovieListUtil.fetchMovieData(mUrl,false);
    }
}
