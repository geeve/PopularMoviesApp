package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

/**
 * Created by Administrator on 2017/10/22 0022.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private Context mContext;

    private ContentValues[] mContentValues;

    public ReviewsAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reviews_item,parent,false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.tvAuthorView.setText(mContentValues[position].getAsString(OpenMovieJsonUtils.JSON_REVIEW_AUTHOR));
        holder.tvReviewContentView.setText(mContentValues[position].getAsString(OpenMovieJsonUtils.JSON_REVIEW_CONTENT));
    }

    public void swapReviews(ContentValues[] contentValues){
        this.mContentValues = contentValues;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        final TextView tvAuthorView;
        final TextView tvReviewContentView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            tvAuthorView = (TextView)itemView.findViewById(R.id.review_author);
            tvReviewContentView = (TextView)itemView.findViewById(R.id.review_content);

        }
    }
}
