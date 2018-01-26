package com.kkmovies.kankanmovies.MoviesApi;

import com.kkmovies.kankanmovies.Models.Movie;
import com.kkmovies.kankanmovies.Models.ResponseMultiSearch;
import com.kkmovies.kankanmovies.Models.ResponsePopularMovies;
import com.kkmovies.kankanmovies.Models.ResponsePopularTv;
import com.kkmovies.kankanmovies.Models.ResponseVideos;
import com.kkmovies.kankanmovies.Models.TvShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Retrofit interface with methods to call TMDB Api service

public interface TmdbApi {
    // movie
    @GET("movie/popular")
    Call<ResponsePopularMovies> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ResponseVideos> getMovieVideos(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<ResponsePopularMovies> getSimilarMovies(@Path("id") int movieId, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}/recommendations")
    Call<ResponsePopularMovies> getRecommendedMovies(@Path("id") int movieId, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<ResponsePopularMovies> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") int page);

    // search

    @GET("search/multi")
    Call<ResponseMultiSearch> getSearchResults(@Query("api_key") String apiKey, @Query("query") String query, @Query("page") int page);

    // Tv Show

    @GET("tv/popular")
    Call<ResponsePopularTv> getPopularTvShows(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/{tv_id}")
    Call<TvShow> getTvShowDetails(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/videos")
    Call<ResponseVideos> getTvShowVideos(@Path("tv_id") int tvId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/recommendations")
    Call<ResponsePopularTv> getRecommendedTvShows(@Path("tv_id") int tvId, @Query("api_key") String apiKey, @Query("page") int page );


}
