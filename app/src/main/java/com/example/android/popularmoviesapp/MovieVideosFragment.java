package com.example.android.popularmoviesapp;

import android.content.ContentValues;
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

import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

/**
 * Created by Administrator on 2017/10/26 0026.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieVideosFragment extends Fragment implements LoaderManager.LoaderCallbacks<ContentValues[]>{
    private static String mUrl;

    public static String videoUrl;
    private RecyclerView mRecyclerView;

    private VideosAdapter mAdapter;

    private static final int VIDEO_ASYNC_CODE = 104;
    public MovieVideosFragment() {
        super();
    }

    public static MovieVideosFragment newInstance(String url){
        MovieVideosFragment movieVideosFragment = new MovieVideosFragment();
        mUrl = url;
        return movieVideosFragment;
    }

    public String getVideoUrl(){
        return videoUrl;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_videos,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.videos_list);
        mAdapter = new VideosAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(VIDEO_ASYNC_CODE,null,this).forceLoad();
    }

    @Override
    public Loader<ContentValues[]> onCreateLoader(int id, Bundle args) {
        return new VideosAsyncTaskLoader(getContext(),mUrl);
    }

    @Override
    public void onLoadFinished(Loader<ContentValues[]> loader, ContentValues[] data) {
        if(data.length > 0) {
            videoUrl = NetWorkUtils.VIDEO_PATH + data[0].getAsString(OpenMovieJsonUtils.JSON_VIDEO_KEY);
        }
        mAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<ContentValues[]> loader) {
        mAdapter.swapData(null);
    }
}
