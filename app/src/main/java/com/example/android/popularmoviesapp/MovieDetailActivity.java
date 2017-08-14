package com.example.android.popularmoviesapp;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>{

    private String mMovieId;

    private TextView tvMovieName;

    private ImageView ivMoviePoster;

    private TextView tvMovieDate;

    private TextView tvMovieVote;

    private TextView tvMovieOverview;

    private String mUrl;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        setTitle(getString(R.string.movie_detail_title));
        getIntentParam();
        initView();

        getSupportLoaderManager().initLoader(2,null,this).forceLoad();
    }

    private void initView(){
        tvMovieDate = (TextView)findViewById(R.id.detail_date);
        tvMovieName = (TextView) findViewById(R.id.tv_movie_name);
        tvMovieOverview = (TextView) findViewById(R.id.detail_movie_overview);
        tvMovieVote = (TextView)findViewById(R.id.detail_vote_averge);
        ivMoviePoster = (ImageView) findViewById(R.id.detail_movie_poster);
    }

    private void setViews(Movie movie){
        tvMovieName.setText(movie.getmMovieName());
        tvMovieVote.setText(movie.getmMovieVote());
        tvMovieOverview.setText(movie.getmOverView());
        tvMovieDate.setText(movie.getmMovieDate());
        Picasso.with(this).load(movie.getmMoviePoster()).into(ivMoviePoster);
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

        mMovieId = bundle.getString(Contract.JsonKey.movieId);

        mUrl = Contract.BASE_REQUEST_URL + "/" + mMovieId +"?" +Contract.API_KEY_PARM + "="+Contract.API_KEY_VALUE;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        return new MovieDetailAsyncTaskLoader(this,mUrl);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        mMovie = data;
        setViews(mMovie);

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        mMovie = null;
    }
}
