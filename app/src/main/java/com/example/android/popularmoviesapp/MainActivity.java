package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.ConnectivityManager;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;



import com.example.android.popularmoviesapp.data.*;
import com.example.android.popularmoviesapp.sync.MovieSyncUtil;
import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;


/***
 * @author geeve
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /***
     * 初始化各类显示
     */
    private void initView(){

        FragmentManager fm = getSupportFragmentManager();
        MovieListFragment movieListFragment = MovieListFragment.newInstance();

        fm.beginTransaction()
                .add(R.id.activity_main,movieListFragment)
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}
