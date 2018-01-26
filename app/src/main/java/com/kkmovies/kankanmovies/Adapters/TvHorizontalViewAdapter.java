package com.kkmovies.kankanmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kkmovies.kankanmovies.Activities.TvDetailsActivity;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Models.TvShow;
import com.kkmovies.kankanmovies.R;

import java.util.List;

public class TvHorizontalViewAdapter extends RecyclerView.Adapter<TvHorizontalViewAdapter.ItemViewHolder> {

    private Context mContext;
    private List<TvShow> mTvShows;

    public TvHorizontalViewAdapter(Context mContext, List<TvShow> mTvShows) {
        this.mContext = mContext;
        this.mTvShows = mTvShows;
    }


    @Override
    public TvHorizontalViewAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvHorizontalViewAdapter.ItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_horizontal_scrollview, parent, false));
    }

    @Override
    public void onBindViewHolder(TvHorizontalViewAdapter.ItemViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mTvShows.get(position).getPoster())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mItemPoster);

        if(mTvShows.get(position).getName()!=null){
            holder.mItemTitle.setText(mTvShows.get(position).getName());
        }
        else{
            holder.mItemTitle.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mTvShows==null ? 0 : mTvShows.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mItemPoster;
        private TextView mItemTitle;

        private ItemViewHolder(View itemView) {
            super(itemView);
            mItemPoster = (ImageView)itemView.findViewById(R.id.item_poster);
            mItemTitle = (TextView)itemView.findViewById(R.id.item_name);

            mItemPoster.getLayoutParams().width = (int)(mContext.getResources().getDisplayMetrics().widthPixels * 0.30);
            mItemPoster.getLayoutParams().height = (int)((mContext.getResources().getDisplayMetrics().widthPixels * 0.35)/0.66);

            mItemPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TvDetailsActivity.class);
                    intent.putExtra("tv_id", mTvShows.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }


            });
        }

    }
}
