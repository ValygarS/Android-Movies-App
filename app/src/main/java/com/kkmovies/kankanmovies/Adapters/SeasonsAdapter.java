package com.kkmovies.kankanmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.Models.Season;
import com.kkmovies.kankanmovies.R;

import java.util.List;

// to bind seasons data to recycle view
public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder> {

    private final String TAG = "SeasonsAdapter";

    private Context mContext;
    private List<Season> mSeasons;

    public SeasonsAdapter(Context mContext, List<Season> mSeasons) {
        this.mContext = mContext;
        this.mSeasons = mSeasons;
    }

    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"Entered onCreateViewHolder");
        return new SeasonViewHolder(LayoutInflater.from(mContext).inflate(R.layout.season_item,parent, false));
    }

    @Override
    public void onBindViewHolder(SeasonViewHolder holder, int position) {
        //Log.d(TAG, "Entered onBindViewHolder. Season number:" + String.valueOf(mSeasons.get(position).getSeasonNumber()));
        if(mSeasons.get(position).getSeasonNumber()!=0){
            holder.seasonName.setText("Season " + String.valueOf(mSeasons.get(position).getSeasonNumber()));
        }
        if(mSeasons.get(position).getEpisodeCount()!=0){
            holder.seasonNumberEpisodes.setText(String.valueOf(mSeasons.get(position).getEpisodeCount()));
        }
        if(mSeasons.get(position).getAirDate()!=null){
            holder.seasonDate.setText(DateHelper.parseDate(mSeasons.get(position).getAirDate()));
        }

        //holder.seasonWatched.setHint("Not Watched");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"Entered getItemCount " + mSeasons.size());
        if (mSeasons==null){
            return 0;
        }
        else{
            return mSeasons.size();
        }
    }

    public class SeasonViewHolder extends RecyclerView.ViewHolder {
        private TextView seasonLabel;
        private TextView seasonName;
        private TextView seasonNumberEpisodes;
        private TextView seasonDate;
        private ImageView seasonClick;
        private CheckBox seasonWatched;

        public SeasonViewHolder(View itemView) {
            super(itemView);

            seasonLabel = (TextView)itemView.findViewById(R.id.item_season_episodes_lbl);
            seasonName = (TextView)itemView.findViewById(R.id.item_season_title);
            seasonNumberEpisodes = (TextView)itemView.findViewById(R.id.item_season_episodes_number);
            seasonDate = (TextView)itemView.findViewById(R.id.item_season_air_date);
            seasonClick = (ImageView)itemView.findViewById(R.id.item_season_chevron);
            seasonWatched = (CheckBox)itemView.findViewById(R.id.item_season_watched);
        }
    }
}
