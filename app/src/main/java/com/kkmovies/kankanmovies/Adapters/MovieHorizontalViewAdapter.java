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
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.R;

import java.util.List;

// adapter to bind movies to view for horizontal scroll
public class MovieHorizontalViewAdapter extends RecyclerView.Adapter<MovieHorizontalViewAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

    public MovieHorizontalViewAdapter(Context mContext, List<Movie> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_horizontal_scrollview, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mMovies.get(position).getPoster())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mItemPoster);

        if(mMovies.get(position).getName()!=null){
            holder.mItemTitle.setText(mMovies.get(position).getName());
        }
        else{
            holder.mItemTitle.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mMovies==null ? 0 : mMovies.size();
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
                    Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                    intent.putExtra("movie_id", mMovies.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }


            });
        }

    }
}
