package com.example.android.popularmoviesapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.example.android.popularmoviesapp.Movie;
import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.data.MoviePreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Administrator on 2017/10/20 0020.
 * com.example.android.popularmoviesapp.utilities,PopularMoviesApp
 */

public final class OpenMovieJsonUtils {

    public static final String JSON_RESULTS = "results";

    public static final String JSON_ID = "id";

    public static final String JSON_POSTER = "poster_path";

    public static final String JSON_TITLE = "title";

    public static final String JSON_VOTE = "vote_average";

    public static final String JSON_RELEASE_DATE = "release_date";

    public static final String JSON_OVERVIEW = "overview";

    public static final String JSON_RUNTIME = "runtime";

    public static final String JSON_REVIEW_AUTHOR = "author";

    public static final String JSON_REVIEW_CONTENT = "content";

    public static final String JSON_VIDEO_NAME = "name";

    public static final String JSON_VIDEO_KEY = "key";

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movies.
     *
     * @param movieJsonStr JSON response from server
     *
     * @return Array of Strings describing movie data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ContentValues[] getMoiveContentValuesFromJson(Context context, String movieJsonStr) throws JSONException{

        if(TextUtils.isEmpty(movieJsonStr)){
            return null;
        }

        JSONObject baseMovieJson = new JSONObject(movieJsonStr);

        JSONArray itemJsonArray = baseMovieJson.getJSONArray(JSON_RESULTS);

        ContentValues[] movieContentValues = new ContentValues[itemJsonArray.length()];

        if(itemJsonArray.length() > 0) {

            for (int i = 0; i < itemJsonArray.length(); i++) {
                JSONObject item = itemJsonArray.getJSONObject(i);

                String movieId = item.getString(JSON_ID);
                String moviePoster = item.getString(JSON_POSTER);
                String movieName = item.getString(JSON_TITLE);
                String movieDate = item.getString(JSON_RELEASE_DATE);
                String movieOverview = item.getString(JSON_OVERVIEW);
                String movieVote = item.getString(JSON_VOTE);

                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieId);
                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER,moviePoster);
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME,movieName);
                contentValues.put(MovieContract.MovieEntry.COLUMN_PUBLISH_DATE,movieDate);
                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movieOverview);
                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE,movieVote);
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE,0);
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SORT_TYPE, MoviePreferences.getPrefOrderBy(context));

                movieContentValues[i] = contentValues;
            }
        }

        return movieContentValues;
    }

    public static ContentValues[] getMovieVideosFromJson(String jsonResponse) throws JSONException{
        if(TextUtils.isEmpty(jsonResponse)){
            return  null;
        }

        JSONObject baseVideoJson = new JSONObject(jsonResponse);
        JSONArray itemJsonArray = baseVideoJson.getJSONArray(JSON_RESULTS);

        ContentValues[] videosValues = new ContentValues[itemJsonArray.length()];
        if(itemJsonArray.length()>0){
            for(int i = 0;i<itemJsonArray.length();i++){
                JSONObject item = itemJsonArray.getJSONObject(i);

                String videoName = item.getString(JSON_VIDEO_NAME);
                String videoKey = item.getString(JSON_VIDEO_KEY);

                ContentValues contentValues = new ContentValues();

                contentValues.put(JSON_VIDEO_NAME,videoName);
                contentValues.put(JSON_VIDEO_KEY,videoKey);

                videosValues[i] = contentValues;
            }
        }

        return videosValues;
    }

    public static ContentValues[] getMovieReviewsFromJson(String jsonResponse) throws JSONException{
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        JSONObject baseReviewJson = new JSONObject(jsonResponse);

        JSONArray itemJsonArray = baseReviewJson.getJSONArray(JSON_RESULTS);

        ContentValues[] reviewsValues = new ContentValues[itemJsonArray.length()];

        if(itemJsonArray.length() > 0){
            for (int i = 0;i<itemJsonArray.length();i++){
                JSONObject item = itemJsonArray.getJSONObject(i);

                String review_author = item.getString(JSON_REVIEW_AUTHOR);
                String review_content = item.getString(JSON_REVIEW_CONTENT);

                ContentValues contentValues = new ContentValues();
                contentValues.put(JSON_REVIEW_AUTHOR,review_author);
                contentValues.put(JSON_REVIEW_CONTENT,review_content);

                reviewsValues[i] = contentValues;
            }
        }

        return reviewsValues;
    }

    public static Movie extractMovieFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        try {

            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            String movieId = baseJsonResponse.getString(JSON_ID);
            String moviePoster = baseJsonResponse.getString(JSON_POSTER);
            String movieName = baseJsonResponse.getString(JSON_TITLE);
            String movieDate = baseJsonResponse.getString(JSON_RELEASE_DATE);
            String movieOverview = baseJsonResponse.getString(JSON_OVERVIEW);
            String movieVote = baseJsonResponse.getString(JSON_VOTE);
            String movieRuntime = baseJsonResponse.getString(JSON_RUNTIME);

            Movie movie = new Movie(movieName,movieId);
            movie.setmMoviePoster(moviePoster);
            movie.setmMovieDate(movieDate);
            movie.setmMovieVote(movieVote);
            movie.setmOverView(movieOverview);
            movie.setmMovieRuntime(movieRuntime);

            return movie;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
