package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/10/22 0022.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ContentValues[]>{

    private static String mUrl;

    private Context mContext;

    private ContentValues[] mContentValues;

    private ReviewsAdapter mReviewsAdapter;

    private RecyclerView mRecyclerView;

    private static final int REVIEW_ASYNC_CODE = 103;

    public MovieReviewsFragment() {
        super();
    }

    public static MovieReviewsFragment newInstance(String url){
        MovieReviewsFragment movieReviewsFragment = new MovieReviewsFragment();
        mUrl = url;
        return movieReviewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews,container,false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_reviews_list);
        mReviewsAdapter = new ReviewsAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mReviewsAdapter);
        mRecyclerView.setHasFixedSize(true);
        getLoaderManager().initLoader(REVIEW_ASYNC_CODE,null,this).forceLoad();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<ContentValues[]> onCreateLoader(int id, Bundle args) {
        return new ReviewsAsyncTaskLoader(getContext(),mUrl);
    }

    @Override
    public void onLoadFinished(Loader<ContentValues[]> loader, ContentValues[] data) {
        this.mContentValues = data;
        mReviewsAdapter.swapReviews(mContentValues);
    }

    @Override
    public void onLoaderReset(Loader<ContentValues[]> loader) {
        mReviewsAdapter.swapReviews(null);
    }

}
