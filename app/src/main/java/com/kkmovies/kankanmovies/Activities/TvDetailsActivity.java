package com.kkmovies.kankanmovies.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kkmovies.kankanmovies.Adapters.SeasonsAdapter;
import com.kkmovies.kankanmovies.Adapters.TvHorizontalViewAdapter;
import com.kkmovies.kankanmovies.Adapters.VideosAdapter;
import com.kkmovies.kankanmovies.Database.MovieFirebase;
import com.kkmovies.kankanmovies.Database.TvFirebase;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.Helpers.GenresHelper;
import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Models.ResponsePopularMovies;
import com.kkmovies.kankanmovies.Models.ResponsePopularTv;
import com.kkmovies.kankanmovies.Models.ResponseVideos;
import com.kkmovies.kankanmovies.Models.Season;
import com.kkmovies.kankanmovies.Models.TvShow;
import com.kkmovies.kankanmovies.Models.Video;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApi;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApiClient;
import com.kkmovies.kankanmovies.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvDetailsActivity extends AppCompatActivity {

    private final String TAG = "TvDetails-Activity";

    private ImageView mTvPoster;
    private ImageView mTvBackground;

    private TextView mTvTitle;
    private TextView mTvFirstAirDate;
    private TextView mTvGenre;
    private TextView mTvSeasonsCount;
    private TextView mTvEpisodesCount;
    private TextView mTvDetails;
    private TextView mTvVideosTitle;
    private TextView mTvRecommendedLabel;
    private TextView mTvSeasonsTitle;

    private RecyclerView mRecyclerViewVideos;
    private RecyclerView mRecyclerViewSeasons;
    private RecyclerView mRecyclerViewRecommended;

    private VideosAdapter mVideosAdapter;
    private TvHorizontalViewAdapter mTvRecommendedAdapter;

    private List<Video> mVideos;

    // buttons - save to/remove from Watchlist
    private Button mAddToWatchlist;
    private Button mRemoveFromWatchlist;

    private int tvId;
    private TvShow mTvShow;

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference rootRef, tvsRef, tvRef;

    // get tv show details
    Call<TvShow> mTvShowCall;

    // get trailers
    Call<ResponseVideos> mVideosCall;

    // get recommended
    Call<ResponsePopularTv> mRecommendedTvsCall;
    private List<TvShow> mTvShows;

    // seasons
    List<Season> mSeasons;
    private SeasonsAdapter mSeasonsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details);

        setTitle("");

        // getting Tv show Id from intent
        Intent intent=getIntent();
        tvId = intent.getIntExtra("tv_id", 0);

        mTvPoster = (ImageView) findViewById(R.id.tv_poster);
        mTvBackground = (ImageView) findViewById(R.id.tv_backgrd);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvFirstAirDate = (TextView) findViewById(R.id.tv_first_air_date);
        mTvGenre = (TextView) findViewById(R.id.tv_genre);
        mTvSeasonsCount = (TextView) findViewById(R.id.tv_seasons_count);
        mTvEpisodesCount = (TextView) findViewById(R.id.tv_episodes_count);
        mTvDetails = (TextView) findViewById(R.id.tv_details);
        mTvVideosTitle = (TextView) findViewById(R.id.tv_videos_lbl);

        // Videos - trailers
        mRecyclerViewVideos = (RecyclerView)findViewById(R.id.recycler_view_tv_videos);
        mVideos = new ArrayList<>();
        mVideosAdapter = new VideosAdapter(TvDetailsActivity.this, mVideos);
        mRecyclerViewVideos.setAdapter(mVideosAdapter);
        mRecyclerViewVideos.setLayoutManager(new LinearLayoutManager(TvDetailsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        mAddToWatchlist = (Button)findViewById(R.id.tv_add_wishlist);
        mRemoveFromWatchlist = (Button)findViewById(R.id.tv_remove_wishlist);

        // Recommended
        mRecyclerViewRecommended = (RecyclerView)findViewById(R.id.recycler_view_tv_recommended);
        mTvRecommendedLabel = (TextView) findViewById(R.id.tv_recommended_lbl);
        mTvShows = new ArrayList<>();
        mTvRecommendedAdapter = new TvHorizontalViewAdapter(TvDetailsActivity.this, mTvShows);
        mRecyclerViewRecommended.setAdapter(mTvRecommendedAdapter);
        mRecyclerViewRecommended.setLayoutManager(new LinearLayoutManager(TvDetailsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        //Seasons
        mTvSeasonsTitle = (TextView)findViewById(R.id.tv_seasons_title);
        mRecyclerViewSeasons = (RecyclerView)findViewById(R.id.recycler_view_tv_seasons);
        mSeasons = new ArrayList<>();
        mSeasonsAdapter = new SeasonsAdapter(TvDetailsActivity.this, mSeasons);
        mRecyclerViewSeasons.setAdapter(mSeasonsAdapter);
        mRecyclerViewSeasons.setLayoutManager(new LinearLayoutManager(TvDetailsActivity.this,
                LinearLayoutManager.VERTICAL, false));

        mAddToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTvWishlist();
            }
        });

        mRemoveFromWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TvDetailsActivity.this);
                builder.setMessage("Delete from Wishlist?")
                        // confirmed deletion
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteTvShow();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(TvDetailsActivity.this, "Cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        isTvWishlist(tvId);

        loadTvDetails();

        loadRecommendedTvShows();
    }

    private void loadTvDetails(){
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mTvShowCall = tmdbService.getTvShowDetails(tvId, getString(R.string.tmdb_api_key));
        mTvShowCall.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                if (!response.isSuccessful()){
                    mTvShowCall = call.clone();
                    mTvShowCall.enqueue(this);
                    return;
                }
                if (response.body()==null){
                    return;
                }
                mTvShow = response.body();

                Log.d(TAG, "LoadTVDetails");
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(Constants.POSTER_BASE_URL_500 + response.body().getPoster())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(mTvPoster);
                // TODO check backdrop 500
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .load(Constants.BACKDROP_BASE_URL_300 + response.body().getBackdropPath())
                        .into(mTvBackground);

                if (response.body().getName() != null)
                    mTvTitle.setText(response.body().getName());
                else
                    mTvTitle.setText("");

                mTvGenre.setText(GenresHelper.getGenres(response.body().getGenres()));

                if(response.body().getFirstAirDate()!=null){
                    mTvFirstAirDate.setText(DateHelper.parseDate(response.body().getFirstAirDate()));
                }
                if(response.body().getOverview()!=null){
                    mTvDetails.setText(response.body().getOverview());
                }

                //setGenre(response.body().getGenres());

                mTvSeasonsCount.setText(String.valueOf(response.body().getNumberOfSeasons()));
                mTvEpisodesCount.setText(String.valueOf(response.body().getNumberOfEpisodes()));

                loadTvVideos();

                loadSeasonsData();
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {

            }
        });

    }
    // load trailers for Tv show if there any
    private void loadTvVideos() {
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mVideosCall = tmdbService.getTvShowVideos(tvId, getString(R.string.tmdb_api_key));
        mVideosCall.enqueue(new Callback<ResponseVideos>() {
            @Override
            public void onResponse(Call<ResponseVideos> call, Response<ResponseVideos> response) {
                if (!response.isSuccessful()){
                    mVideosCall = call.clone();
                    mVideosCall.enqueue(this);
                    return;
                }
                if (response.body()==null){
                    return;
                }

                if(response.body().getVideos()==null){
                    return;
                }

                for (Video video:response.body().getVideos()) {
                    mVideos.add(video);
                }
                if (!mVideos.isEmpty()){
                    mTvVideosTitle.setVisibility(View.VISIBLE);
                }

                mVideosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseVideos> call, Throwable t) {

            }
        });
    }

    private void loadRecommendedTvShows(){
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mRecommendedTvsCall = tmdbService.getRecommendedTvShows(tvId, getString(R.string.tmdb_api_key), 1);
        mRecommendedTvsCall.enqueue(new Callback<ResponsePopularTv>() {
            @Override
            public void onResponse(Call<ResponsePopularTv> call, Response<ResponsePopularTv> response) {
                if (!response.isSuccessful()){
                    mRecommendedTvsCall = call.clone();
                    mRecommendedTvsCall.enqueue(this);
                    return;
                }
                if (response.body()==null){
                    Toast.makeText(getApplicationContext(), "No data loaded", Toast.LENGTH_SHORT).show();
                }
                else{

                    for(TvShow tvShow: response.body().getResults()){
                        if (tvShow != null && tvShow.getPoster() != null){
                            mTvShows.add(tvShow);
                        }
                    }
                    if(!mTvShows.isEmpty()){
                        mTvRecommendedLabel.setVisibility(View.VISIBLE);
                    }

                    mTvRecommendedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponsePopularTv> call, Throwable t) {

            }
        });
    }

    private void loadSeasonsData(){
        Log.d(TAG, "Entered load seasons data");

        if (mTvShow==null){
            Log.d(TAG, "mTvShow==null");
            return;
        }
        if (mTvShow.getSeasons()==null){
            Log.d(TAG, "mTvShow.getSeasons()==null");
            return;
        }
        for (Season season: mTvShow.getSeasons()){
            if(season.getSeasonNumber()==0){
                continue;
            }
            mSeasons.add(season);
        }

        mTvSeasonsTitle.setVisibility(View.VISIBLE);

        mSeasonsAdapter.notifyDataSetChanged();

    }

    protected void saveTvWishlist(){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tvsRef = rootRef.child(uid).child("tvs");
        tvRef = rootRef.child(uid).child("tv").child(String.valueOf(mTvShow.getId()));

        final TvFirebase tvFirebase = new TvFirebase(String.valueOf(mTvShow.getId()), mTvShow.getName(), mTvShow.getPoster(),
                mTvShow.getFirstAirDate(), true, false);

        tvRef.setValue(tvFirebase, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Log.d(TAG, "saveTvToDatabase:fail, Error: " + databaseError.getMessage());
                    Toast.makeText(TvDetailsActivity.this, "Saving to database failed.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(TvDetailsActivity.this, "Saved successfully.",
                            Toast.LENGTH_SHORT).show();
                    mAddToWatchlist.setVisibility(View.GONE);
                    mRemoveFromWatchlist.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    protected void deleteTvShow(){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tvsRef = rootRef.child(uid).child("tv");
        tvsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // check if tv show also saved in the watchlist: if saved, than just set wishlist to false
                if (dataSnapshot.child(String.valueOf(tvId)).child("watchlist").getValue().toString().equals("true")){
                    dataSnapshot.child(String.valueOf(tvId)).child("wishlist").getRef().setValue("false",
                            new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    // some error happened
                                    if(databaseError!=null){
                                        Toast.makeText(TvDetailsActivity.this, "TV not deleted!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(TvDetailsActivity.this, "Deleted successfully",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                // if watchlist == false, than deleting from DB completely
                else{
                    tvsRef.child(String.valueOf(tvId)).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            // some error happened
                            if(databaseError!=null){
                                Toast.makeText(TvDetailsActivity.this, "Tv Show not deleted!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(TvDetailsActivity.this, "Deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                                mRemoveFromWatchlist.setVisibility(View.GONE);
                                mAddToWatchlist.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void isTvWishlist(final int tvId){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tvsRef = rootRef.child(uid).child("tvs");
        tvsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(tvId)).exists()) {
                    Log.d(TAG, "tv id exists");
                    if (dataSnapshot.child(String.valueOf(tvId)).child("wishlist").getValue().toString().equals("true")){
                        mAddToWatchlist.setVisibility(View.GONE);
                        mRemoveFromWatchlist.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}
