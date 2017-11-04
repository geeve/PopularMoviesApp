package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.MovieContract;
import com.example.android.popularmoviesapp.data.MoviePreferences;
import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.start;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoiveViewHolder> {

    private Context mContext;


    private Cursor mCursor;

    public MovieAdapter(Context context,MovieAdapterOnClickHandler handler){

        this.mContext = context;

        this.mClickHandler = handler;
    }



    @Override
    public MoiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item,parent,false);
        view.setFocusable(true);
        return new MoiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoiveViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        Picasso.with(mContext).load(NetWorkUtils.IMAG_REQUEST_URL + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER))).into(holder.moivePic);
        holder.moiveId.setText(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public void setMovie(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    //返回数据List
    public Cursor getMovie(){
        return mCursor;
    }

    final private MovieAdapterOnClickHandler mClickHandler;
    public interface MovieAdapterOnClickHandler{
        void onClick(String date);
    }

    class MoiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView moivePic;
        final TextView moiveId;

        public MoiveViewHolder(View itemView) {
            super(itemView);

            moivePic = (ImageView) itemView.findViewById(R.id.item_moive_pic);
            moiveId = (TextView) itemView.findViewById(R.id.item_moive_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String movieId = moiveId.getText().toString();
            Log.v("movieId",movieId);
            mClickHandler.onClick(movieId);
        }
    }
}
