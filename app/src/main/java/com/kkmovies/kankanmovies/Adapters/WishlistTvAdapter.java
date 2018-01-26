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
import com.kkmovies.kankanmovies.Activities.MovieDetailsActivity;
import com.kkmovies.kankanmovies.Activities.TvDetailsActivity;
import com.kkmovies.kankanmovies.Database.TvFirebase;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.Models.TvShow;
import com.kkmovies.kankanmovies.R;

import java.util.List;

import retrofit2.Call;

public class WishlistTvAdapter extends RecyclerView.Adapter<WishlistTvAdapter.TvViewHolder>{

    private Context mContext;
    private List<TvFirebase> mTvShows;

    private Call<TvShow> mCallTvDetails;

    public WishlistTvAdapter(Context mContext, List<TvFirebase> mTvShows) {
        this.mContext = mContext;
        this.mTvShows = mTvShows;
    }

    @Override
    public TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.wishlist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TvViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mTvShows.get(position).posterPath)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mItemPoster);

        holder.mItemTitle.setText(mTvShows.get(position).name);
        holder.mItemYear.setText(DateHelper.parseDate(mTvShows.get(position).firstAirDate));
        holder.mItemType.setText(R.string.item_type_tv);

    }

    @Override
    public int getItemCount() {
        if (mTvShows!=null){
            return mTvShows.size();
        }
        else{
            return 0;
        }
    }

    public class TvViewHolder extends RecyclerView.ViewHolder {

        private ImageView mItemPoster;
        private TextView mItemTitle;
        private TextView mItemYear;
        private TextView mItemType;

        public TvViewHolder(View itemView) {
            super(itemView);

            mItemPoster = (ImageView) itemView.findViewById(R.id.wishlist_poster);
            mItemTitle = (TextView) itemView.findViewById(R.id.wishlist_title);
            mItemYear = (TextView) itemView.findViewById(R.id.wishlist_year);
            mItemType = (TextView) itemView.findViewById(R.id.wishlist_type);

            mItemPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TvDetailsActivity.class);
                    intent.putExtra("tv_id", Integer.parseInt(mTvShows.get(getAdapterPosition()).id));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
