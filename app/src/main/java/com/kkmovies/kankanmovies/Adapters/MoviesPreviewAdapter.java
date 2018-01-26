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

/**
 * Created by Andrew on 13/01/2018.
 */

public class MoviesPreviewAdapter extends RecyclerView.Adapter<MoviesPreviewAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

    public MoviesPreviewAdapter(Context mContext, List<Movie> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;
        private TextView mMovieTitle;

        private ItemViewHolder(View view){
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.item_poster);
            mMovieTitle = (TextView) view.findViewById(R.id.item_name);

            mMoviePoster.getLayoutParams().width = (int)(mContext.getResources().getDisplayMetrics().widthPixels * 0.45);
            mMoviePoster.getLayoutParams().height = (int)((mContext.getResources().getDisplayMetrics().widthPixels * 0.45)/0.66);

            mMoviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                    intent.putExtra("movie_id", mMovies.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_horizontal_scrollview,
                parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mMovies.get(position).getPoster())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mMoviePoster);

        holder.mMovieTitle.setText(mMovies.get(position).getName());
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
