package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.utilities.NetWorkUtils;
import com.example.android.popularmoviesapp.utilities.OpenMovieJsonUtils;

/**
 * Created by Administrator on 2017/10/26 0026.
 * com.example.android.popularmoviesapp,PopularMoviesApp
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder>{

    private Context mContext;

    private ContentValues[] mContentValues;

    public VideosAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.videos_item,parent,false);

        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        holder.imVideoPlayIcon.setTag(mContentValues[position].getAsString(OpenMovieJsonUtils.JSON_VIDEO_KEY));
        holder.tvTrailer.setText(mContentValues[position].getAsString(OpenMovieJsonUtils.JSON_VIDEO_NAME));
    }

    public void swapData(ContentValues[] c){
        this.mContentValues = c;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(mContentValues == null){
            return 0;
        }
        return mContentValues.length;
    }

    class VideosViewHolder extends RecyclerView.ViewHolder{

        private ImageView imVideoPlayIcon;

        private TextView tvTrailer;

        public VideosViewHolder(View itemView) {
            super(itemView);

            imVideoPlayIcon = (ImageView) itemView.findViewById(R.id.videos_play_icon);
            tvTrailer = (TextView) itemView.findViewById(R.id.video_item_label);

            imVideoPlayIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String videoKey = (String)imVideoPlayIcon.getTag();
                    String url = NetWorkUtils.VIDEO_PATH + videoKey;
                    Uri uri = Uri.parse(url);
                    Log.v("Video Url:",url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    if(intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}

