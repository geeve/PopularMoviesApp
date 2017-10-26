package com.example.android.popularmoviesapp;

import android.content.Intent;

import android.support.v4.app.FragmentManager;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionProvider;

import android.util.Log;
import android.view.Menu;

import android.widget.ShareActionProvider;


import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;




public class MovieDetailActivity extends AppCompatActivity{

    private String mMovieId;

    private String mUrl;

    private MoviePrimaryInforFragment mMoviePrimaryInforFragment;

    private MovieReviewsFragment mMovieReviewsFragment;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getIntentParam();

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMoviePrimaryInforFragment = MoviePrimaryInforFragment.newInstance(mUrl);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_primary,mMoviePrimaryInforFragment)
                .commit();

        String url = NetWorkUtils.BASE_REQUEST_URL + "/" + mMovieId + "/reviews" + "?" +NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
        mMovieReviewsFragment = MovieReviewsFragment.newInstance(url);
        fragmentManager.beginTransaction()
                .add(R.id.frgment_reviews,mMovieReviewsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
//        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.menu_item_share).getActionProvider();
//        mShareActionProvider.setShareIntent(getShareIntent());
//        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        return true;
    }
    private Intent getShareIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,mUrl);
        intent.setType("text/plain");
        return intent;
    }
    /**
     * 获得Intent中传过来的参数
     */
    private void getIntentParam(){
        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            mMovieId = null;
            return;
        }

        mMovieId = bundle.getString(OpenMovieJsonUtils.JSON_ID);
        Log.v("res MovieId",mMovieId);
        mUrl = NetWorkUtils.BASE_REQUEST_URL + "/" + mMovieId +"?" +NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
        Log.v("movie_path",mUrl);
    }

}
