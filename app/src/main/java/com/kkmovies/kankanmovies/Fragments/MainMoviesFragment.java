package com.kkmovies.kankanmovies.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Adapters.MoviesPreviewAdapter;
import com.kkmovies.kankanmovies.R;
import com.kkmovies.kankanmovies.Models.ResponsePopularMovies;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApi;
import com.kkmovies.kankanmovies.MoviesApi.TmdbApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMoviesFragment extends Fragment {

    private final String TAG = "Main-Movies-Fragment";

    private FrameLayout mUpcomingFrameLayout, mPopularFrameLayout;
    private RecyclerView mUpcomingRecyclerView, mPopularRecyclerView;
    private TextView mUpcomingLabel, mPopularLabel;
    private TextView mShowAllUpcoming, mShowAllPopular;
    private MoviesPreviewAdapter mMoviesUpcomingAdapter, mMoviesPopularAdapter;
    private List<Movie> mUpcomingMovies, mPopularMovies;
    private Call<ResponsePopularMovies> mCallUpcomingMovies, mCallPopularMovies;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_main, container, false);

        mUpcomingFrameLayout = (FrameLayout) view.findViewById(R.id.movies_upcoming_frame);
        mUpcomingLabel = (TextView) view.findViewById(R.id.movie_upcoming_label);
        mShowAllUpcoming = (TextView) view.findViewById(R.id.movie_upcoming_all);

        mPopularFrameLayout= (FrameLayout) view.findViewById(R.id.movies_popular_frame);
        mPopularLabel = (TextView) view.findViewById(R.id.movie_popular_label);
        mShowAllPopular = (TextView) view.findViewById(R.id.movie_popular_all);

        mUpcomingMovies = new ArrayList<>();
        mPopularMovies = new ArrayList<>();

        mUpcomingRecyclerView = (RecyclerView) view.findViewById(R.id.movies_upcoming_rv);
        mMoviesUpcomingAdapter = new MoviesPreviewAdapter(getActivity(), mUpcomingMovies);

        mUpcomingRecyclerView.setAdapter(mMoviesUpcomingAdapter);
        mUpcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        mPopularRecyclerView = (RecyclerView) view.findViewById(R.id.movies_popular_rv);
        mMoviesPopularAdapter = new MoviesPreviewAdapter(getActivity(), mPopularMovies);
        mPopularRecyclerView.setAdapter(mMoviesPopularAdapter);
        mPopularRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));


        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadUpcomingMovies();
        loadPopularMovies();
    }

    private void loadUpcomingMovies(){
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mCallUpcomingMovies = tmdbService.getUpcomingMovies(getString(R.string.tmdb_api_key), 1);
        mCallUpcomingMovies.enqueue(new Callback<ResponsePopularMovies>() {
            @Override
            public void onResponse(Call<ResponsePopularMovies> call, Response<ResponsePopularMovies> response) {
                if (!response.isSuccessful()){
                    mCallUpcomingMovies = call.clone();
                    mCallUpcomingMovies.enqueue(this);
                    return;

                }
                if (response.body().equals(null)){
                    Toast.makeText(getActivity().getApplicationContext(), "No data loaded", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (Movie movie: response.body().getResults()){
                        if (movie != null && movie.getPoster() != null){
                            mUpcomingMovies.add(movie);
                        }
                    }
                    mMoviesUpcomingAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResponsePopularMovies> call, Throwable t) {

            }
        });

    }

    private void loadPopularMovies(){
        TmdbApi tmdbService = TmdbApiClient.getRetrofit().create(TmdbApi.class);
        mCallPopularMovies = tmdbService.getPopularMovies(getString(R.string.tmdb_api_key), 1);
        mCallPopularMovies.enqueue(new Callback<ResponsePopularMovies>() {
            @Override
            public void onResponse(Call<ResponsePopularMovies> call, Response<ResponsePopularMovies> response) {
                if (!response.isSuccessful()){
                    mCallPopularMovies = call.clone();
                    mCallPopularMovies.enqueue(this);
                    return;

                }
                if (response.body().equals(null)){
                    Toast.makeText(getActivity().getApplicationContext(), "No data loaded", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (Movie movie: response.body().getResults()){
                        if (movie != null && movie.getPoster() != null){
                            mPopularMovies.add(movie);
                        }
                    }
                    mMoviesPopularAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResponsePopularMovies> call, Throwable t) {

            }
        });
    }
}
