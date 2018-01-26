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
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kkmovies.kankanmovies.Activities.MovieDetailsActivity;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Database.MovieFirebase;
import com.kkmovies.kankanmovies.R;

import java.util.List;

import retrofit2.Call;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishListViewHolder>{

    private Context mContext;
    private List<MovieFirebase> mMovies;


    public WishlistAdapter(Context mContext, List<MovieFirebase> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
    }


    protected class WishListViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemPoster;
        private TextView mItemTitle;
        private TextView mItemYear;
        private TextView mItemType;

        private WishListViewHolder(View view){
            super(view);
            mItemPoster = (ImageView) view.findViewById(R.id.wishlist_poster);
            mItemTitle = (TextView) view.findViewById(R.id.wishlist_title);
            mItemYear = (TextView) view.findViewById(R.id.wishlist_year);
            mItemType = (TextView) view.findViewById(R.id.wishlist_type);

            mItemPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                    intent.putExtra("movie_id", Integer.parseInt(mMovies.get(getAdapterPosition()).id));
                    view.getContext().startActivity(intent);
                }
            });


        }

    }

    protected class WishListTvShowViewHolder extends RecyclerView.ViewHolder{

        public WishListTvShowViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public WishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WishListViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.wishlist_item, parent, false));

    }

    @Override
    public void onBindViewHolder(WishListViewHolder holder, int position) {

        // load poster

        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mMovies.get(position).posterPath)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mItemPoster);

        // set movie data to TextViews;
        String releaseDate = mMovies.get(position).year;

        holder.mItemTitle.setText(mMovies.get(position).name);
        holder.mItemYear.setText(DateHelper.concatToYear(releaseDate));
        holder.mItemType.setText(R.string.item_type_movie);

    }

    @Override
    public int getItemCount() {
        if (mMovies!=null){
            return mMovies.size();
        }
        else{
            return 0;
        }
    }

}
