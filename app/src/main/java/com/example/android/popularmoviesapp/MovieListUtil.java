package com.example.android.popularmoviesapp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieListUtil {

    private static final String LOG_TAG = MovieListUtil.class.getSimpleName();


    /***
     * 调用此方法可以获得ArrayList数据
     * @param requestUrl 请求Url，网站的API
     * @param detail 是否查询单个电影信息，是为单个，否为LIst
     * @return  ArrayList<Movie>
     */
    public static ArrayList<Movie> fetchMovieData(String requestUrl,boolean detail){

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error in InputStream",e);
        }

        ArrayList<Movie> movies = new ArrayList<>();
        if(!detail) {
            movies = extractMovieFromJson(jsonResponse);
        }else {

            Movie m = extractMovieFromJson2(jsonResponse);
            movies.add(m);
        }

        return movies;
    }

    private static ArrayList<Movie> extractMovieFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemJsonArray = baseJsonResponse.getJSONArray(Contract.JsonKey.results);

            if(itemJsonArray.length() > 0){

                for(int i = 0;i<itemJsonArray.length();i++){
                    JSONObject item = itemJsonArray.getJSONObject(i);

                    String movieId = item.getString(Contract.JsonKey.movieId);

                    String moviePoster = item.getString(Contract.JsonKey.moviePoster);
                    String movieName = item.getString(Contract.JsonKey.movieName);

                    Movie movie = new Movie(movieName,movieId);
                    movie.setmMoviePoster(moviePoster);

                    movies.add(movie);
                }

                return movies;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * 从JSON中获得电影的详细信息
     * @param jsonResponse
     * @return
     */
    private static Movie extractMovieFromJson2(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        try {

            JSONObject baseJsonResponse = new JSONObject(jsonResponse);


            String movieId = baseJsonResponse.getString(Contract.JsonKey.movieId);
            String moviePoster = baseJsonResponse.getString(Contract.JsonKey.moviePoster);
            String movieName = baseJsonResponse.getString(Contract.JsonKey.movieName);
            String movieDate = baseJsonResponse.getString(Contract.JsonKey.movieDate);
            String movieOverview = baseJsonResponse.getString(Contract.JsonKey.movieOverView);
            String movieVote = baseJsonResponse.getString(Contract.JsonKey.movieVote);

            Movie movie = new Movie(movieName,movieId);
            movie.setmMoviePoster(moviePoster);
            movie.setmMovieDate(movieDate);
            movie.setmMovieVote(movieVote);
            movie.setmOverView(movieOverview);


            return movie;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * 获得网站返回的JSON格式数据
     * @param url 请求的URL
     * @return JSON格式的String
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;

        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(10000);
            httpUrlConnection.setReadTimeout(15000);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            if(httpUrlConnection.getResponseCode() == 200){
                inputStream = httpUrlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Http error:"+httpUrlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Retriew data error!",e);
        }finally {
            if(httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /***
     * 从InputStream中读取数据转化为String
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Create URL error!",e);
        }

        return url;
    }
}
