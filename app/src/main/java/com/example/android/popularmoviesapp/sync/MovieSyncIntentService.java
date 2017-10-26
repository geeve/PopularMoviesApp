package com.example.android.popularmoviesapp.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Administrator on 2017/10/21 0021.
 * com.example.android.popularmoviesapp.sync,PopularMoviesApp
 */

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieSyncTask.syncMovie(this);
    }
}
