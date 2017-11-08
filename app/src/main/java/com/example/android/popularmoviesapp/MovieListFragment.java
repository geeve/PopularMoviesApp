package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.icu.util.GregorianCalendar;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.data.MoviePreferences;
import com.example.android.popularmoviesapp.sync.MovieSyncUtil;
import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

/**
 * Created by Administrator on 2017/11/3 0003.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,MovieAdapter.MovieAdapterOnClickHandler  {
    private RecyclerView mRecyclerView;


    private MovieAdapter mMovieAdapter;

    private static int REQUEST_CODE = 100;

    private LinearLayout mNetWorkError;

    private boolean mNetWorkStateOk = false;

    private static String INI_STATE = "ini_state";

    private boolean mTwoPanl = false;

    private static int mPrePosition;
    private static final String PRE_POSITION = "pre_position";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieAdapter = new MovieAdapter(getContext(),this);
        setHasOptionsMenu(true);
    }

    public MovieListFragment() {
        super();
    }

    public static MovieListFragment newInstance(){
        MovieListFragment movieListFragment = new MovieListFragment();
        return movieListFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_movie_list,container,false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.gv_movie_grid);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        mNetWorkError = (LinearLayout) viewRoot.findViewById(R.id.view_network_error);


        /**判断是横屏还是竖屏*/
        Configuration configuration = getActivity().getResources().getConfiguration();
        int ori = configuration.orientation;
        if(ori == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            Log.v("Device is ","LANDDSCAPE");
        }else if(ori == Configuration.ORIENTATION_PORTRAIT){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }


        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null) {
            MovieSyncUtil.initialize(getContext());
            getActivity().getSupportLoaderManager().initLoader(REQUEST_CODE, null, this).forceLoad();
        }else {
            mPrePosition = savedInstanceState.getInt(PRE_POSITION,1);
            Log.v("restore position",":"+mPrePosition);
            Bundle b = new Bundle();
            b.putInt(PRE_POSITION,mPrePosition);
            MovieSyncUtil.startImmediateSync(getContext());
            getActivity().getSupportLoaderManager().restartLoader(REQUEST_CODE,b,this);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mNetWorkStateOk = isOnline();
        if(!mNetWorkStateOk){
            mNetWorkError.setVisibility(View.VISIBLE);

        }else{
            mNetWorkError.setVisibility(View.GONE);
//            getActivity().getSupportLoaderManager().restartLoader(REQUEST_CODE,null,this).forceLoad();
        }

        if(getActivity().findViewById(R.id.two_pane_divide) != null){
            mTwoPanl = true;
        }else {
            mTwoPanl = false;
        }

    }
    /**
     * 判断网络连接是否存在
     * @return boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isOnline()){
            return true;
        }
        switch (item.getItemId()){
            case R.id.sort_pop:
                MoviePreferences.setPrefOrderBy(getContext(),MoviePreferences.PREF_ORDER_BY_POP);
                Toast.makeText(getContext(), R.string.sort_popular_tips,Toast.LENGTH_SHORT).show();

                break;
            case R.id.sort_rate:
                MoviePreferences.setPrefOrderBy(getContext(),MoviePreferences.PREF_ORDER_BY_VOTE);
                Toast.makeText(getContext(), R.string.sort_rate_tips,Toast.LENGTH_SHORT).show();

                break;
            case R.id.sort_favorite:
                MoviePreferences.setPrefOrderBy(getContext(),MoviePreferences.PREF_ORDER_BY_FARI);
                break;
            case R.id.refresh:
                break;
            case R.id.setting:
                startActivity(new Intent(getContext(),SettingActivity.class));
                break;
            default:

        }
        if(MoviePreferences.getPrefOrderBy(getContext()) != MoviePreferences.PREF_ORDER_BY_FARI) {
            MovieSyncUtil.startImmediateSync(getContext());
        }

        getActivity().getSupportLoaderManager().restartLoader(REQUEST_CODE,null,this).forceLoad();
        mNetWorkError.setVisibility(View.GONE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INI_STATE,true);
        super.onSaveInstanceState(outState);
        GridLayoutManager gm = (GridLayoutManager)mRecyclerView.getLayoutManager();
        mPrePosition = gm.findFirstVisibleItemPosition();
        Log.v("save postion",":"+mPrePosition);
        outState.putInt(PRE_POSITION,mPrePosition);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;

        if(args != null){
            mPrePosition = args.getInt(PRE_POSITION,0);
            Log.v("Loader Create:"," "+mPrePosition);
        }
        if(MoviePreferences.getPrefOrderBy(getContext()) == MoviePreferences.PREF_ORDER_BY_FARI){
            Log.v("OrderBy:","favorite");
            selection = MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + "=1 and "+ MovieContract.MovieEntry.COLUMN_MOVIE_SORT_TYPE + "=2";
        }else if(MoviePreferences.getPrefOrderBy(getContext()) == MoviePreferences.PREF_ORDER_BY_POP){
            selection = MovieContract.MovieEntry.COLUMN_MOVIE_SORT_TYPE + "=0";
        }else {
            selection = MovieContract.MovieEntry.COLUMN_MOVIE_SORT_TYPE + "=1";
        }
        return new CursorLoader(getContext(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mMovieAdapter.setMovie(data);
        Log.v("Load position:"," "+mPrePosition);
        if(mPrePosition >= 0) {
            mRecyclerView.smoothScrollToPosition(mPrePosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.setMovie(null);
    }

    @Override
    public void onClick(String date) {
        if(!mTwoPanl) {
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);

            intent.putExtra(OpenMovieJsonUtils.JSON_ID, date);

            startActivityForResult(intent, REQUEST_CODE);

        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
