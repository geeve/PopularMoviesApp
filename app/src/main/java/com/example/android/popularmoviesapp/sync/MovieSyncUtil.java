package com.example.android.popularmoviesapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.android.popularmoviesapp.data.MovieContract;

/**
 * Created by Administrator on 2017/10/21 0021.
 * com.example.android.popularmoviesapp.sync,PopularMoviesApp
 */

public class MovieSyncUtil {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        if(sInitialized){
            return;
        }

        sInitialized = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID};

                Cursor cursor = context.getContentResolver().query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

                if(cursor == null || cursor.getCount() == 0){
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intentToSynceImmediate = new Intent(context,MovieSyncIntentService.class);
        context.startService(intentToSynceImmediate);
    }
}
