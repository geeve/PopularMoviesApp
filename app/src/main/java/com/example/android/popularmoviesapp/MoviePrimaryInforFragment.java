package com.example.android.popularmoviesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/10/23 0023.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MoviePrimaryInforFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movie>{


    private ImageView ivMoviePoster;

    private TextView tvMovieDate;

    private TextView tvMovieVote;

    private TextView tvMovieOverview;

    private TextView tvMovieRuntime;

    private static String mUrl;

    private Movie mMovie;

    public MoviePrimaryInforFragment() {
        super();

    }

    public static MoviePrimaryInforFragment newInstance(String url){
        MoviePrimaryInforFragment moviePrimaryInforFragment = new MoviePrimaryInforFragment();
        Bundle b =  new Bundle();
        b.putString("url",url);
        moviePrimaryInforFragment.setArguments(b);
        mUrl = url;
        return moviePrimaryInforFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_primary_info,container,false);
        tvMovieDate = (TextView)rootView.findViewById(R.id.detail_date);
        tvMovieOverview = (TextView) rootView.findViewById(R.id.detail_movie_overview);
        tvMovieVote = (TextView)rootView.findViewById(R.id.detail_vote_averge);
        ivMoviePoster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        tvMovieRuntime = (TextView) rootView.findViewById(R.id.detail_movie_runtime);
//        this.mUrl = getArguments().getString("url");
//        Bundle b = getArguments();
//        this.mUrl = b.getString("url");
        //this.mUrl = getActivity().getIntent().getStringExtra("url");
        return rootView;
    }

    private void setViews(Movie movie){
        tvMovieVote.setText(movie.getmMovieVote());
        tvMovieOverview.setText(movie.getmOverView());
        tvMovieDate.setText(movie.getmMovieDate());
        tvMovieRuntime.setText(movie.getmMovieRuntime()+ "min");
        Picasso.with(getContext()).load(movie.getmMoviePoster()).into(ivMoviePoster);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(4,null,this).forceLoad();
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        return new MovieDetailAsyncTaskLoader(getContext(),mUrl);
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
