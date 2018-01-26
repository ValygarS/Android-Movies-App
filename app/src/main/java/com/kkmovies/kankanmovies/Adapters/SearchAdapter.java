package com.kkmovies.kankanmovies.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.kkmovies.kankanmovies.Activities.MovieDetailsActivity;
import com.kkmovies.kankanmovies.Activities.TvDetailsActivity;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.SearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchResultViewHolder> {

    private final String TAG = "SearchAdapter";

    private Context mContext;
    private List<SearchResult> mSearchResults;

    public SearchAdapter(Context mContext, List<SearchResult> mSearchResults) {
        this.mContext = mContext;
        this.mSearchResults = mSearchResults;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        // load posters
        Glide.with(mContext.getApplicationContext())
                .asBitmap()
                .load(Constants.POSTER_BASE_URL_342 + mSearchResults.get(position).getPoster())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.mItemPoster);

        if (mSearchResults.get(position).getMediaType().equals("tv")){
            holder.mItemName.setText(mSearchResults.get(position).getTvName());
            holder.mItemTypeYear.setText(R.string.item_type_tv);

            // if first air date available, add it
            if(mSearchResults.get(position).getFirstAirDate()!=null){
                // set tv air date to TextViews;
                String firstAirDate = mSearchResults.get(position).getFirstAirDate();

                String dateRequired = DateHelper.parseDate(firstAirDate);


                holder.mItemTypeYear.setText(holder.mItemTypeYear.getText()+" ("+dateRequired+")");
            }

        }
        else if (mSearchResults.get(position).getMediaType().equals("movie")){
            holder.mItemName.setText(mSearchResults.get(position).getMovieTitle());
            holder.mItemTypeYear.setText(R.string.item_type_movie);

            // if release date available, add year
            if(mSearchResults.get(position).getReleaseDate()!=null || !mSearchResults.get(position).getReleaseDate().equals("")){
                // set movie date to TextViews;
                String releaseDate = mSearchResults.get(position).getReleaseDate();
                // get only year from data (yyyy-mm-dd)
                String year = DateHelper.concatToYear(releaseDate);

                holder.mItemTypeYear.setText(holder.mItemTypeYear.getText()+" ("+year+")");
            }

        }




    }

    @Override
    public int getItemCount() {
        if (mSearchResults!=null){
            return mSearchResults.size();
        }
        else {
            return 0;
        }
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        private ImageView mItemPoster;
        private TextView mItemName;
        private TextView mItemTypeYear;


        public SearchResultViewHolder(View view){
            super(view);
            mCardView = (CardView) view.findViewById(R.id.search_card);
            mItemPoster = (ImageView) view.findViewById(R.id.search_item_poster);
            mItemName = (TextView) view.findViewById(R.id.search_item_name);
            mItemTypeYear = (TextView) view.findViewById(R.id.search_item_type_year);


            // setting width to display 2 rows on the screen
            mItemPoster.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.45);
            mItemPoster.getLayoutParams().height = (int) ((mContext.getResources().getDisplayMetrics().widthPixels * 0.45) / 0.66);

            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSearchResults.get(getAdapterPosition()).getMediaType().equals("movie")){
                        Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
                        intent.putExtra("movie_id", mSearchResults.get(getAdapterPosition()).getId());
                        view.getContext().startActivity(intent);
                    }
                    else if (mSearchResults.get(getAdapterPosition()).getMediaType().equals("tv")){
                        Intent intent = new Intent(view.getContext(), TvDetailsActivity.class);
                        intent.putExtra("tv_id", mSearchResults.get(getAdapterPosition()).getId());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

    }
}
