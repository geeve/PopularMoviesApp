package com.example.android.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Movie database. This class is not necessary, but keeps
 * the code organized.
 */

public class MovieContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesapp";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Movies
     * can handle. For instance,
     *
     *     content://com.example.android.popularmoviesapp/movies/
     *     [           BASE_CONTENT_URI         ][ PATH_WEATHER ]
     *
     * is a valid path for looking at weather data.
     *
     *      content://com.example.android.popularmoviesapp/givemeroot/
     *
     * will fail, as the ContentProvider hasn't been given any information on what to do with
     * "givemeroot". At least, let's hope not. Don't be that dev, reader. Don't be that dev.
     */
    public static final String PATH_MOVIE = "movies";
    /*
         * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Movies
         * can handle. For instance,
         *
         *     content://com.example.android.popularmoviesapp/trailer/
         *     [           BASE_CONTENT_URI         ][ PATH_WEATHER ]
         *
         */
    public static final String PATH_TRAILER = "trailer";
    /*
             * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Movies
             * can handle. For instance,
             *
             *     content://com.example.android.popularmoviesapp/reviews/
             *     [           BASE_CONTENT_URI         ][ PATH_WEATHER ]
             *
             */
    public static final String PATH_REVIEW = "reviews";

    /* Inner class that defines the table contents of the movies table */
    public static final class MovieEntry implements BaseColumns{

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "movies";

        /*The name of the movie*/
        public static final String COLUMN_NAME = "movie_name";

        /*The poster of the movie*/
        public static final String COLUMN_POSTER = "movie_poster";

        /*The OverView of the movie*/
        public static final String COLUMN_OVERVIEW = "movie_overview";

        /*The Publish date of the movie*/
        public static final String COLUMN_PUBLISH_DATE = "movie_publish_date";

        /*The Vote of the movie*/
        public static final String COLUMN_VOTE = "movie_vote";

        /*The size of the movie*/
        public static final String COLUMN_MOVIE_SIZE = "movie_size";

        /*Is or Not favorite movie*/
        public static final String COLUMN_MOVIE_FAVORITE = "movie_favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
