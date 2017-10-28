package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.ConnectivityManager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,MovieAdapter.MovieAdapterOnClickHandler {


    private RecyclerView mRecyclerView;


    private MovieAdapter mMovieAdapter;

    private int REQUEST_CODE = 100;

    private LinearLayout mNetWorkError;

    private boolean mNetWorkStateOk = false;

    private static String INI_STATE = "ini_state";

    private boolean mTwoPanl = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            initView();
        }

        if(findViewById(R.id.two_pane_divide) != null){
            mTwoPanl = true;
        }else {
            mTwoPanl = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNetWorkStateOk = isOnline();
        if(!mNetWorkStateOk){
            mNetWorkError.setVisibility(View.VISIBLE);

        }else{
            mNetWorkError.setVisibility(View.GONE);
            getSupportLoaderManager().initLoader(1,null,this).forceLoad();
        }
    }

    /**
     * 判断网络连接是否存在
     * @return boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isOnline()){
            return true;
        }
        switch (item.getItemId()){
            case R.id.sort_pop:
                MoviePreferences.setPrefOrderBy(this,MoviePreferences.PREF_ORDER_BY_POP);
                Toast.makeText(MainActivity.this, R.string.sort_popular_tips,Toast.LENGTH_SHORT).show();

                break;
            case R.id.sort_rate:
                MoviePreferences.setPrefOrderBy(this,MoviePreferences.PREF_ORDER_BY_VOTE);
                Toast.makeText(MainActivity.this, R.string.sort_rate_tips,Toast.LENGTH_SHORT).show();

                break;
            case R.id.sort_favorite:
                MoviePreferences.setPrefOrderBy(this,MoviePreferences.PREF_ORDER_BY_FARI);
                break;
            case R.id.refresh:

        }
        if(MoviePreferences.getPrefOrderBy(this) != MoviePreferences.PREF_ORDER_BY_FARI) {
            MovieSyncUtil.startImmediateSync(this);
        }
        getSupportLoaderManager().restartLoader(1,null,this).forceLoad();
        mNetWorkError.setVisibility(View.GONE);
        return super.onOptionsItemSelected(item);
    }


    /***
     * 初始化各类显示
     */
    private void initView(){
        mMovieAdapter = new MovieAdapter(this,this);
        mRecyclerView = (RecyclerView) findViewById(R.id.gv_movie_grid);
        mRecyclerView.setAdapter(mMovieAdapter);
        //设置布局为每行2列
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setHasFixedSize(true);
        mNetWorkError = (LinearLayout)findViewById(R.id.view_network_error);

        getSupportLoaderManager().initLoader(1,null,this);
        MovieSyncUtil.initialize(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "";
        String[] selectionArgs = {"0"};

        if(MoviePreferences.getPrefOrderBy(this) == MoviePreferences.PREF_ORDER_BY_FARI){
            Log.v("OrderBy:","favorite");
            selection = MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + "=1";
            selectionArgs[0] = "1";
        }
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        for(int i=0;i<data.getCount();i++){
            Log.v(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID))+",","f:"+data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE)));
            data.moveToNext();
        }
        mMovieAdapter.setMovie(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.setMovie(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INI_STATE,true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(String date) {
        if(!mTwoPanl) {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

            intent.putExtra(OpenMovieJsonUtils.JSON_ID, date);

            startActivityForResult(intent, REQUEST_CODE);

        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            String urlPrimary = NetWorkUtils.BASE_REQUEST_URL + "/" + date +"?" +NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
            MoviePrimaryInforFragment moviePrimaryInforFragment = MoviePrimaryInforFragment.newInstance(urlPrimary);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_primary,moviePrimaryInforFragment)
                    .commit();

            String urlReview = NetWorkUtils.BASE_REQUEST_URL + "/" + date + "/reviews?" +NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
            MovieReviewsFragment movieReviewsFragment = MovieReviewsFragment.newInstance(urlReview);
            fragmentManager.beginTransaction()
                    .replace(R.id.frgment_reviews,movieReviewsFragment)
                    .commit();

            String urlVideo = NetWorkUtils.BASE_REQUEST_URL + "/" + date + "/videos?" + NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
            MovieVideosFragment movieVideosFrgment = MovieVideosFragment.newInstance(urlVideo);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_videos,movieVideosFrgment)
                    .commit();
        }
    }
}
