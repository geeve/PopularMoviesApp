package com.example.android.popularmoviesapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

import java.net.URL;

/**
 * Created by Administrator on 2017/10/21 0021.
 * com.example.android.popularmoviesapp.sync,PopularMoviesApp
 */

public class MovieSyncTask {
    synchronized public static void syncMovie(Context context){
        try{
            URL movieRequestUrl = NetWorkUtils.getRequestUrl(context);

            String jsonMovieResponse = NetWorkUtils.makeHttpRequest(movieRequestUrl);

            ContentValues[] contentValues = OpenMovieJsonUtils.getMoiveContentValuesFromJson(context,jsonMovieResponse);

            if(contentValues != null && contentValues.length != 0){
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null);

                movieContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,contentValues);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
