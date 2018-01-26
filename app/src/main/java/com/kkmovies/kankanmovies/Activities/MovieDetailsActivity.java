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
import com.kkmovies.kankanmovies.Adapters.MovieHorizontalViewAdapter;
import com.kkmovies.kankanmovies.Helpers.Constants;
import com.kkmovies.kankanmovies.Helpers.DateHelper;
import com.kkmovies.kankanmovies.Models.Genre;
import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Database.MovieFirebase;
import com.kkmovies.kankanmovies.Models.ResponsePopularMovies;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.ResponseVideos;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApi;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApiClient;
import com.kkmovies.kankanmovies.Models.Video;
import com.kkmovies.kankanmovies.Adapters.VideosAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView moviePoster;
    private ImageView movieBackground;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieGenre;
    private TextView movieRuntime;
    private TextView movieDetails;
    private TextView movieVideosTitle;
    private TextView movieRecommendedTitle;

    private RecyclerView mRecyclerViewVideos;
    private VideosAdapter mVideosAdapter;

    private RecyclerView mRecyclerViewRecommended;
    private MovieHorizontalViewAdapter mRecommendedMoviesAdapter;

    private List<Video> mVideos;
    private List<Movie> mMovies;

    // buttons - save to/remove from Watchlist
    private Button saveToDb;
    private Button mRemoveFromWatchlist;

    private int movieId;
    private Movie movie;

    private final String TAG = "MovieDetails-Activity";

    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference rootRef, moviesRef, movieRef;

    Call<Movie> movieDetailsCall;
    Call<ResponseVideos> mVideosCall;
    Call<ResponsePopularMovies> mRecommendedMoviesCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setTitle("");

        // getting movieID from intent
        Intent intent=getIntent();
        movieId = intent.getIntExtra("movie_id", 0);

        moviePoster = findViewById(R.id.movie_poster);
        movieBackground = findViewById(R.id.movie_backgrd);
        movieTitle = findViewById(R.id.movie_title);
        movieYear = findViewById(R.id.movie_year);
        movieDetails = findViewById(R.id.movie_details);
        movieGenre = findViewById(R.id.movie_genre);
        movieRuntime = findViewById(R.id.movie_runtime);

        // binding videos
        movieVideosTitle = findViewById(R.id.movie_videos_lbl);
        mRecyclerViewVideos = findViewById(R.id.recycler_view_movie_videos);
        mVideos = new ArrayList<>();
        mVideosAdapter = new VideosAdapter(MovieDetailsActivity.this, mVideos);
        mRecyclerViewVideos.setAdapter(mVideosAdapter);
        mRecyclerViewVideos.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        // view for recommended movies
        movieRecommendedTitle = (TextView) findViewById(R.id.movie_recommended_title);
        mRecyclerViewRecommended = (RecyclerView)findViewById(R.id.recycler_view_movie_recommended);
        mMovies = new ArrayList<>();
        mRecommendedMoviesAdapter = new MovieHorizontalViewAdapter(MovieDetailsActivity.this, mMovies);
        mRecyclerViewRecommended.setAdapter(mRecommendedMoviesAdapter);
        mRecyclerViewRecommended.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        // button to save movie data
        saveToDb = (Button) findViewById(R.id.movie_add_wishlist);

        // button to delete from watchlist - hidden by default
        mRemoveFromWatchlist = (Button) findViewById(R.id.movie_remove_wishlist);

        //saving movie to Firebase database
        saveToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMovie();
            }
        });

        //remove from Wishlist with Alert confirmation
        mRemoveFromWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Alert Dialog to confirm deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieDetailsActivity.this);
                builder.setMessage("Delete from Wishlist?")
                        // confirmed deletion
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteMovie();

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(MovieDetailsActivity.this, "Cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // if movie saved to Wishlist - hide save button and display Delete button;
        isMovieWishlist(movieId);

        loadMovieDetails();

        loadRecommendedMovies();

    }

    // set runtime
    private void setRuntime(Integer runtime){
        if(runtime!=null){
            String mRuntime = String.valueOf(runtime) + " mins";
            movieRuntime.setText(mRuntime);
        }
    }

    // set Genres values
    private void setGenre(List<Genre> genres){
        if(genres!=null){
            String genre = "";
            if(genres.size()==1){
                genre = genres.get(0).getGenreName();
            }
            else{
                for (int i=0; i<genres.size(); i++){
                    genre = genre.concat(genres.get(i).getGenreName() + ", ");
                }
                // remove last comma + space
                genre = genre.replaceAll(", $", "");
            }
            movieGenre.setText(genre);
        }


    }
    // add movie to wishlist
    protected void saveMovie(){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        moviesRef = rootRef.child(uid).child("movies");
        movieRef = rootRef.child(uid).child("movies").child(String.valueOf(movie.getId()));

        final MovieFirebase mf = new MovieFirebase(String.valueOf(movie.getId()), movie.getName(), movie.getPoster(),
                movie.getReleaseDate(), true, false);

        movieRef.setValue(mf, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError!=null){
                    Log.d(TAG, "saveMovieToDatabase:fail, Error: " + databaseError.getMessage());
                    Toast.makeText(MovieDetailsActivity.this, "Saving to database failed.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MovieDetailsActivity.this, "Saved successfully.",
                            Toast.LENGTH_SHORT).show();
                    saveToDb.setVisibility(View.GONE);
                    mRemoveFromWatchlist.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void loadMovieDetails(){
        // Getting movie data from movie API and assigning it to views
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);

        movieDetailsCall = tmdbService.getMovieDetails(movieId, getString(R.string.tmdb_api_key));
        movieDetailsCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()){
                    movieDetailsCall = call.clone();
                    movieDetailsCall.enqueue(this);
                    return;
                }
                if (response.body()==null){
                    return;
                }
                movie = response.body();
                // load images - poster and backdrop
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(Constants.POSTER_BASE_URL_500 + response.body().getPoster())
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(moviePoster);
                // TODO check backdrop 500
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .load("https://image.tmdb.org/t/p/w500/" + response.body().getBackdrop())
                        .into(movieBackground);

                if (response.body().getName() != null)
                    movieTitle.setText(response.body().getName());
                else
                    movieTitle.setText("");

                movieYear.setText(DateHelper.concatToYear(response.body().getReleaseDate()));

                movieDetails.setText(response.body().getDescription());

                setGenre(response.body().getGenres());

                setRuntime(response.body().getRuntime());

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });

        loadMovieVideos();

    }

    // load trailers for selected movie, if available
    private void loadMovieVideos() {
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mVideosCall = tmdbService.getMovieVideos(movieId, getString(R.string.tmdb_api_key));
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
                    movieVideosTitle.setVisibility(View.VISIBLE);
                }

                mVideosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseVideos> call, Throwable t) {

            }
        });
    }

    private void loadRecommendedMovies(){
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mRecommendedMoviesCall = tmdbService.getRecommendedMovies(movieId, getString(R.string.tmdb_api_key), 1);
        mRecommendedMoviesCall.enqueue(new Callback<ResponsePopularMovies>() {
            @Override
            public void onResponse(Call<ResponsePopularMovies> call, Response<ResponsePopularMovies> response) {
                if (!response.isSuccessful()){
                    mRecommendedMoviesCall = call.clone();
                    mRecommendedMoviesCall.enqueue(this);
                    return;
                }
                if (response.body()==null){
                    Toast.makeText(getApplicationContext(), "No data loaded", Toast.LENGTH_SHORT).show();
                }
                else{

                    for(Movie movie: response.body().getResults()){
                        if (movie != null && movie.getPoster() != null){
                            mMovies.add(movie);
                        }
                    }
                    if(!mMovies.isEmpty()){
                        movieRecommendedTitle.setVisibility(View.VISIBLE);
                    }

                    mRecommendedMoviesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponsePopularMovies> call, Throwable t) {

            }
        });

    }


    // check if movie saved already
    private void isMovieWishlist(final int movieId){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        moviesRef = rootRef.child(uid).child("movies");
        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(movieId)).exists()) {
                    Log.d(TAG, "movie id exists");
                    if (dataSnapshot.child(String.valueOf(movieId)).child("wishlist").getValue().toString().equals("true")){
                        saveToDb.setVisibility(View.GONE);
                        mRemoveFromWatchlist.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteMovie(){
        rootRef = mDatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        moviesRef = rootRef.child(uid).child("movies");
        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // check if movie also saved in the watchlist: if saved, than just set wishlist to false
                if (dataSnapshot.child(String.valueOf(movieId)).child("watchlist").getValue().toString().equals("true")){
                    dataSnapshot.child(String.valueOf(movieId)).child("wishlist").getRef().setValue("false",
                            new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            // some error happened
                            if(databaseError!=null){
                                Toast.makeText(MovieDetailsActivity.this, "Movie not deleted!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                // if watchlist == false, than deleting from DB completely
                else{
                    moviesRef.child(String.valueOf(movieId)).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            // some error happened
                            if(databaseError!=null){
                                Toast.makeText(MovieDetailsActivity.this, "Movie not deleted!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MovieDetailsActivity.this, "Deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                                mRemoveFromWatchlist.setVisibility(View.GONE);
                                saveToDb.setVisibility(View.VISIBLE);
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


}
