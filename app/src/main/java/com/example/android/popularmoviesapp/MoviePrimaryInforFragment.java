package com.example.android.popularmoviesapp;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.MovieContract;
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

    private Button btnFavorite;

    //保存本电影是否被收藏了
    private boolean mIsFavorite;

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
        btnFavorite = (Button) rootView.findViewById(R.id.btn_make_favorite);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMovie == null){
                    return;
                }
                if(!mIsFavorite){
                    setMovieFavorite(mMovie.getmMovieId(),true);

                }else {
                    setMovieFavorite(mMovie.getmMovieId(),false);

                }
            }
        });
        getActivity().getSupportLoaderManager().initLoader(4,null,this).forceLoad();
    }

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


    private void setViews(Movie movie){
        tvMovieVote.setText(movie.getmMovieVote());
        tvMovieOverview.setText(movie.getmOverView());
        tvMovieDate.setText(movie.getmMovieDate());
        tvMovieRuntime.setText(movie.getmMovieRuntime()+ "min");
        Picasso.with(getContext()).load(movie.getmMoviePoster()).into(ivMoviePoster);
        if(mIsFavorite){
            btnFavorite.setText("HAS FAVORITE");
            btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorDeli));
        }else{
            btnFavorite.setText("MAKE AS FAVORITE");
            btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    /***
     * 根据电影ID 查询是否被收藏
     * @param movieId
     * @return true：被收藏，false：未收藏
     */
    private boolean isMovieFavorite(String movieId){
        String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID, MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE};
        String[] selectionArgs = {movieId};
        Uri uri = Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI,movieId);
        Cursor cursor = getContext().getContentResolver().query(
                uri,
                projection,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                selectionArgs,
                null
        );

        cursor.moveToFirst();
        int i = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE));
        cursor.close();
        if(i == 0){
            return false;
        }else {
            return true;
        }
    }

    /***
     * 设置电影IP为movieId的收藏为true
     * @param movieId
     * @param b
     */
    private void setMovieFavorite(String movieId,final boolean b){
        ContentValues contentValues = new ContentValues();
        int v = 0;
        if(b){
            v = 1;
        }else {
            v = 0;
        }
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE,v);
        AsyncQueryHandler asyncQueryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {
            @Override
            protected void onUpdateComplete(int token, Object cookie, int result) {
                super.onUpdateComplete(token, cookie, result);
                mIsFavorite = b;
                setViews(mMovie);
            }
        };
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {movieId};

        asyncQueryHandler.startUpdate(200,
                null,
                MovieContract.MovieEntry.CONTENT_URI,
                contentValues,
                selection,
                selectionArgs);
//        int num = getContext().getContentResolver().update(
//                MovieContract.MovieEntry.CONTENT_URI,
//                contentValues,
//                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
//                new String[] {movieId}
//        );

    }
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        return new MovieDetailAsyncTaskLoader(getContext(),mUrl);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        mMovie = data;
        if(isMovieFavorite(mMovie.getmMovieId())){
            mIsFavorite = true;
        }else{
            mIsFavorite = false;
        }
        setViews(mMovie);

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        mMovie = null;
    }

}
