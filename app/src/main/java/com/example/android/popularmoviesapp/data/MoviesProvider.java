package com.example.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

/**
 * Created by Administrator on 2017/9/4 0004.
 * com.example.android.popularmoviesapp.data,PopularMoviesApp
 */

public class MoviesProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mMoviesDbHelper;

    /**
     * Creates the UriMatcher that will match each URI to the CODE_MOVIE and
     * CODE_MOVIE_WITH_DATE constants defined above.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_MOVIE and CODE_MOVIE_WITH_DATE
     */
    public static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE,CODE_MOVIE);

        uriMatcher.addURI(authority,MovieContract.PATH_MOVIE + "/#",CODE_MOVIE_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {

        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case CODE_MOVIE:
                db.beginTransaction();
                int rowInserted = 0;
                try{

                    for(ContentValues value : values){
                        //如果数据已存在就进行更新操作，并保留 COLUMN_MOVIE_FAVORITE 的值
                        String movieId = value.getAsString(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                                null,
                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                                new String[]{movieId},
                                null,
                                null,
                                null);
                        long _id;
                        if(cursor != null && cursor.getCount() > 0){
                            cursor.moveToFirst();
                            value.remove(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE);
                            value.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE,cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE)));
                            _id = db.update(MovieContract.MovieEntry.TABLE_NAME,
                                    value,
                                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                                    new String[]{movieId});
                        }else {
                            _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        }
                        if(_id != -1){
                            rowInserted ++;
                        }
                    }
                    db.setTransactionSuccessful();

                }finally {
                    db.endTransaction();
                }

                if(rowInserted > 0 ){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowInserted;
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match){
            case CODE_MOVIE:
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                break;
            case CODE_MOVIE_WITH_ID:
                String[] selectionArg = new String[]{uri.getLastPathSegment().toString()};
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        selectionArg,
                        null,
                        null,
                        s1);
                break;
            default:
                throw new UnsupportedOperationException("Unkown Uri:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int numRowsDelete;
        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if(null == s){
            s = "1";
        }

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                numRowsDelete = mMoviesDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        s,
                        strings
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDelete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDelete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int numRowsUpdate = 0;
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        switch (match){
            case CODE_MOVIE:
            case CODE_MOVIE_WITH_ID:
                numRowsUpdate = db.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        s,
                        strings
                );
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri:"+uri);
        }
        if(numRowsUpdate != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numRowsUpdate;
    }
}
