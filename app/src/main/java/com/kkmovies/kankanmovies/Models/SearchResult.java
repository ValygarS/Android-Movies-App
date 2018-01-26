package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("id")
    private int id;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("name")
    private String tvName;

    @SerializedName("media_type")
    private String mediaType;

    // for movie
    @SerializedName("release_date")
    private String releaseDate;

    // for tv
    @SerializedName("first_air_date")
    private String firstAirDate;


    public SearchResult(int id, String poster, String movieTitle, String tvName, String mediaType, String releaseDate,
                        String firstAirDate) {
        this.id = id;
        this.poster = poster;
        this.movieTitle = movieTitle;
        this.tvName = tvName;
        this.mediaType = mediaType;
        this.releaseDate = releaseDate;
        this.firstAirDate = firstAirDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }
}
