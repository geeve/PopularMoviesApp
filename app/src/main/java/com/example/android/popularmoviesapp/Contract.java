package com.example.android.popularmoviesapp;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class Contract {

    public static final String BASE_REQUEST_URL="https://api.themoviedb.org/3/movie";

    //The movieDb API KEY,You must use your key!
    public static final String API_KEY_VALUE = "";

    public static final String API_KEY_PARM = "api_key";

    public static final String ORDER_POPULAR = "/popular";

    public static final String ORDER_TOP_RATE = "/top_rated";

    //there have “w92”、“w154”、“w185”、“w342”、“w500”、“w780” 或“original” size of picture
    public static final String IMAG_REQUEST_URL="https://image.tmdb.org/t/p/w500";

    public class JsonKey{
        public static final String results = "results";

        public static final String movieId = "id";

        public static final String moviePoster = "poster_path";

        public static final String movieName = "title";

        public static final String movieVote = "vote_average";

        public static final String movieDate = "release_date";

        public static final String movieOverView = "overview";
    }
}
