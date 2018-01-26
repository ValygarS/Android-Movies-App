package com.kkmovies.kankanmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.Video;

import java.util.List;

// adapter for binding videos - trailers;
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private Context mContext;
    private List<Video> mVideos;

    public VideosAdapter(Context mContext, List<Video> mVideos) {
        this.mContext = mContext;
        this.mVideos = mVideos;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideosViewHolder(LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        // load video thumbnail
        Glide.with(mContext.getApplicationContext())
                .asBitmap()
                .load(Constants.YOUTUBE_THUMBNAIL_BASE_URL + mVideos.get(position).getKey() + "/hqdefault.jpg")
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.centerCropTransform())
                .into(holder.mVideoImage);

        if (mVideos.get(position).getName()!= null){
            holder.mVideoTitle.setText(mVideos.get(position).getName());
        }
        else{
            holder.mVideoTitle.setText("");
        }



    }

    @Override
    public int getItemCount() {
        if(mVideos!=null){
            return mVideos.size();
        }
        else{
            return 0;
        }
    }

    protected class VideosViewHolder extends RecyclerView.ViewHolder{

        private CardView mVideoCard;
        private ImageView mVideoImage;
        private TextView mVideoTitle;

        private VideosViewHolder(View view){
            super(view);
            mVideoCard = (CardView)view.findViewById(R.id.video_card);
            mVideoImage = (ImageView) view.findViewById(R.id.video_image);
            mVideoTitle = (TextView) view.findViewById(R.id.video_name_tv);

            // open external youtube video
            mVideoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.YOUTUBE_BASE_URL + mVideos.get(getAdapterPosition()).getKey()));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
