package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesapp.Movie;

import static android.R.attr.version;

/**
 * Created by Administrator on 2017/9/4 0004.
 * com.example.android.popularmoviesapp.data,PopularMoviesApp
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "movies.db";

    /*The version of the database*/
    public static final int DATABASE_VERSION = 1;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */

        final String SQL_CREATE_MOVIE_DATABASE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTENGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT," +
                        MovieContract.MovieEntry.COLUMN_POSTER + " TEXT," +
                        MovieContract.MovieEntry.COLUMN_PUBLISH_DATE + " TEXT," +
                        MovieContract.MovieEntry.COLUMN_VOTE + " TEXT," +
                        MovieContract.MovieEntry.COLUMN_MOVIE_SIZE + " INTEGER," +
                        MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + " BOOLEAN);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
