package com.example.android.popularmoviesapp;

import android.net.Uri;

import com.example.android.popularmoviesapp.utilities.NetWorkUtils;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class Movie {

    //电影名称
    private String mMovieName;
    //电影ID
    private String mMovieId;

    //电影海报图
    private String mMoviePoster;
    //电影简介
    private String mOverView;

    //发布日期
    private String mMovieDate;
    //用户评分
    private String mMovieVote;
    //时长
    private String mMovieRuntime;


    public Movie(String name,String movieId){

        this.mMovieId = movieId;

        this.mMovieName = name;

    }

    public String getmMovieId() {
        return mMovieId;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    //设置海报图片Uri
    public void setmMoviePoster(String mMoviePoster) {

        this.mMoviePoster = NetWorkUtils.IMAG_REQUEST_URL+"/"+mMoviePoster;
    }

    public String getmOverView() {
        return mOverView;
    }

    public String getmMovieDate() {
        return mMovieDate;
    }

    public String getmMovieVote() {
        return mMovieVote;
    }

    public void setmOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public void setmMovieDate(String mMovieDate) {
        this.mMovieDate = mMovieDate;
    }

    public void setmMovieVote(String mMovieVote) {
        this.mMovieVote = mMovieVote;
    }

    public String getmMovieRuntime() {
        return mMovieRuntime;
    }

    public void setmMovieRuntime(String mMovieRuntime) {
        this.mMovieRuntime = mMovieRuntime;
    }

}
