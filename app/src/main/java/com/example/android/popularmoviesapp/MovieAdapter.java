package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.start;

/**
 * Created by Administrator on 2017/8/13 0013.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    private List<Movie> mMovieList;

    private MoiveViewHolder moiveViewHolder;

    public MovieAdapter(Context context,List<Movie> list){

        this.mContext = context;

        this.mMovieList = list;
    }


    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Movie getItem(int i) {
        return mMovieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setMovie(List<Movie> movie){
        mMovieList.clear();
        mMovieList.addAll(movie);
        notifyDataSetChanged();
    }

    //返回数据List
    public List<Movie> getMovie(){
        return mMovieList;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        moiveViewHolder = new MoiveViewHolder();
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.movie_item,viewGroup,false);

            moiveViewHolder.moivePic = (ImageView) view.findViewById(R.id.item_moive_pic);
            moiveViewHolder.moiveId = (TextView)view.findViewById(R.id.item_moive_id);
            view.setTag(moiveViewHolder);
        }else{
            moiveViewHolder = (MoiveViewHolder)view.getTag();
        }

        moiveViewHolder.moiveId.setText(mMovieList.get(i).getmMovieId());
        Picasso.with(mContext).load(mMovieList.get(i).getmMoviePoster()).into(moiveViewHolder.moivePic);

        return view;
    }


    public class MoiveViewHolder{
        public ImageView moivePic;
        public TextView moiveId;
    }
}
