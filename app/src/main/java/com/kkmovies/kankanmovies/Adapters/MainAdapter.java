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
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Activities.MovieDetailsActivity;
import com.kkmovies.kankanmovies.R;

import java.util.List;


// Adapter that binds view to RecyclerView
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MovieListViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

    public MainAdapter(Context mContext, List<Movie> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
    }


    protected class MovieListViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;
        private ProgressBar mMovieProgressBar;

        private MovieListViewHolder(View view){
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);

            mMoviePoster.getLayoutParams().width = (int)(mContext.getResources().getDisplayMetrics().widthPixels * 0.45);

            mMovieProgressBar = (ProgressBar) view.findViewById(R.id.poster_progressBar);

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
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieListViewHolder viewHolder = new MovieListViewHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.movie_item, parent, false));
        return viewHolder;
    }

    // get poster image for movie
    @Override
    public void onBindViewHolder(final MovieListViewHolder holder, int position) {
        holder.mMovieProgressBar.setVisibility(View.VISIBLE);
        // load poster
        Glide.with(mContext)
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_500 + mMovies.get(position).getPoster())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.mMovieProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mMovieProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mMoviePoster);


    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


}
