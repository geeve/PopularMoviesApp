package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private GridView mGridView;

    private String mRequestUrl;

    private MovieAdapter mMovieAdapter;

    private String mCurrentSort;

    private int REQUEST_CODE = 100;

    private LinearLayout mNetWorkError;

    private boolean mNetWorkStateOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mCurrentSort = Contract.ORDER_POPULAR;
        createRequestUrl(mCurrentSort);

        initView();

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
                if(!mCurrentSort.equals(Contract.ORDER_POPULAR)){
                    createRequestUrl(Contract.ORDER_POPULAR);
                    mCurrentSort = Contract.ORDER_POPULAR;
                    Toast.makeText(MainActivity.this, R.string.sort_popular_tips,Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().restartLoader(1,null,this).forceLoad();
                }
                break;
            case R.id.sort_rate:
                if(!mCurrentSort.equals(Contract.ORDER_TOP_RATE)){
                    createRequestUrl(Contract.ORDER_TOP_RATE);
                    mCurrentSort = Contract.ORDER_TOP_RATE;
                    Toast.makeText(MainActivity.this, R.string.sort_rate_tips,Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().restartLoader(1,null,this).forceLoad();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * 根据参数确定请求URL
     * @param order 按照热度排序或者评分排序
     */
    private void createRequestUrl(String order){
        mRequestUrl = Contract.BASE_REQUEST_URL;
        if(order.equals(Contract.ORDER_POPULAR)) {
            mRequestUrl += Contract.ORDER_POPULAR;
        }else if(order.equals(Contract.ORDER_TOP_RATE)){
            mRequestUrl += Contract.ORDER_TOP_RATE;
        }

        mRequestUrl = mRequestUrl+"?"+Contract.API_KEY_PARM+"="+Contract.API_KEY_VALUE;
        Log.e("RequestUrl","url:"+mRequestUrl);
    }

    /***
     * 初始化各类显示
     */
    private void initView(){
        mMovieAdapter = new MovieAdapter(this,new ArrayList<Movie>());
        mGridView = (GridView)findViewById(R.id.gv_movie_grid);
        mGridView.setAdapter(mMovieAdapter);

        mNetWorkError = (LinearLayout)findViewById(R.id.view_network_error);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,MovieDetailActivity.class);

                String movieId = mMovieAdapter.getItem(i).getmMovieId();

                intent.putExtra(Contract.JsonKey.movieId,movieId);

                startActivityForResult(intent,REQUEST_CODE);
            }
        });

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieListAsyncTaskLoader(MainActivity.this,mRequestUrl);

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMovieAdapter.setMovie(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovieAdapter.setMovie(new ArrayList<Movie>());
    }
}
