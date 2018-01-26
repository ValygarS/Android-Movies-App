package com.kkmovies.kankanmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kkmovies.kankanmovies.Activities.TvDetailsActivity;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Activities.MovieDetailsActivity;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.TvShow;

import java.util.List;

public class MainTvAdapter extends RecyclerView.Adapter<MainTvAdapter.TvListViewHolder> {

    private Context mContext;
    private List<TvShow> mTvShow;

    public MainTvAdapter(Context mContext, List<TvShow> mTvShow) {
        this.mContext = mContext;
        this.mTvShow = mTvShow;
    }


    protected class TvListViewHolder extends RecyclerView.ViewHolder {
        private ImageView mTvPoster;
        private ProgressBar mTvProgressBar;

        private TvListViewHolder(View view){
            super(view);
            mTvPoster = (ImageView) view.findViewById(R.id.movie_poster);

            mTvPoster.getLayoutParams().width = (int)(mContext.getResources().getDisplayMetrics().widthPixels * 0.45);

            mTvProgressBar = (ProgressBar) view.findViewById(R.id.poster_progressBar);

            mTvPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TvDetailsActivity.class);
                    intent.putExtra("tv_id", mTvShow.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }

    }


    @Override
    public TvListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvListViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item, parent, false));
    }

    // get poster image for movie
    @Override
    public void onBindViewHolder(final MainTvAdapter.TvListViewHolder holder, int position) {
        holder.mTvProgressBar.setVisibility(View.VISIBLE);
        // load poster
        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_500 + mTvShow.get(position).getPoster())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.mTvProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mTvProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mTvPoster);


    }

    @Override
    public int getItemCount() {
        return mTvShow==null ? 0 : mTvShow.size();
    }


}
