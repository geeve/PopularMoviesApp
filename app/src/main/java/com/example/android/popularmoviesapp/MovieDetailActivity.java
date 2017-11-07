package com.example.android.popularmoviesapp;

import android.content.Intent;


import android.support.v4.app.FragmentManager;



import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;



import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;


/***
 * @author geeve@126.com
 */
public class MovieDetailActivity extends AppCompatActivity{

    private String mMovieId;

    private String mUrl;

    private MoviePrimaryInforFragment mMoviePrimaryInforFragment;

    private MovieReviewsFragment mMovieReviewsFragment;

    private MovieVideosFragment mMovieVideosFrgment;

    private ShareActionProvider mShareActionProvider;

    private String mVideoUrlShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getIntentParam();

        initView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(!TextUtils.isEmpty(mVideoUrlShare)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, mVideoUrlShare);
            intent.setType("text/plain");
            Intent.createChooser(intent, "Share Links");
            setShareIntent(intent);
        }
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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

        String urlReview = NetWorkUtils.BASE_REQUEST_URL + "/" + mMovieId + "/reviews?" +NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
        mMovieReviewsFragment = MovieReviewsFragment.newInstance(urlReview);
        fragmentManager.beginTransaction()
                .add(R.id.frgment_reviews,mMovieReviewsFragment)
                .commit();

        String urlVideo = NetWorkUtils.BASE_REQUEST_URL + "/" + mMovieId + "/videos?" + NetWorkUtils.API_KEY_PARM + "="+NetWorkUtils.API_KEY_VALUE;
        mMovieVideosFrgment = MovieVideosFragment.newInstance(urlVideo);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_videos,mMovieVideosFrgment)
                .commit();

        mVideoUrlShare = mMovieVideosFrgment.getVideoUrl();
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
