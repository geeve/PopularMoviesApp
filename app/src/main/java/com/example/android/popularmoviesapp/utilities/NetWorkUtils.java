package com.example.android.popularmoviesapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.popularmoviesapp.data.MoviePreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/10/20 0020.
 * com.example.android.popularmoviesapp.utilities,PopularMoviesApp
 */

public final class NetWorkUtils {
    private static final String LOG_TAG = NetWorkUtils.class.getSimpleName();

    public static final String BASE_REQUEST_URL="https://api.themoviedb.org/3/movie";

    //The movieDb API KEY,You must use your key!
    public static final String API_KEY_VALUE = "e7c9406aaf1b4404ff405ad51f971cba";

    public static final String API_KEY_PARM = "api_key";

    public static final String ORDER_POPULAR = "/popular";

    public static final String ORDER_TOP_RATE = "/top_rated";

    //there have “w92”、“w154”、“w185”、“w342”、“w500”、“w780” 或“original” size of picture
    public static final String IMAG_REQUEST_URL="https://image.tmdb.org/t/p/w500";

    public static final String VIDEO_PATH = "https://www.youtube.com/watch?v=";


    /***
     * 获得网站返回的JSON格式数据
     * @param url 请求的URL
     * @return JSON格式的String
     */
    public static String makeHttpRequest(URL url) throws IOException {
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

    public static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Create URL error!",e);
        }

        return url;
    }


    /***
     * 根据偏好设定值获取请求URL
     * @param context
     * @return
     */
    public static URL getRequestUrl(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String requestUrl = BASE_REQUEST_URL;

        int orderBy = Integer.valueOf(sp.getString(MoviePreferences.PREF_ORDER_BY,"0"));
        switch (orderBy){
            case MoviePreferences.PREF_ORDER_BY_POP:
                requestUrl += ORDER_POPULAR;
                break;
            case MoviePreferences.PREF_ORDER_BY_VOTE:
                requestUrl += ORDER_TOP_RATE;
                break;
            default:
                requestUrl += ORDER_POPULAR;
        }

        requestUrl = requestUrl+"?"+ API_KEY_PARM+"="+API_KEY_VALUE;

        return createUrl(requestUrl);
    }
}
