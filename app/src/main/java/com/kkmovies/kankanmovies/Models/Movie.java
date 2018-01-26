package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;
import com.kkmovies.kankanmovies.Models.Genre;

import java.util.List;

/**
 * Created by Andrew on 01/01/2018.
 */

public class Movie {

    @SerializedName("id")
    private int Id;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("title")
    private String name;

    @SerializedName("backdrop_path")
    private String backdrop;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("original_title")
    private String originalName;

    @SerializedName("overview")
    private String description;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private double voteAvg;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("genres")
    List<Genre> genres;

    public Movie(int id, String imdbId, String name, String backdrop,
                 String poster, String originalName, String description, int voteCount,
                 double voteAvg, String releaseDate, String mediaType, int runtime, List<Genre> genres) {
        Id = id;
        this.imdbId = imdbId;
        this.name = name;
        this.backdrop = backdrop;
        this.poster = poster;
        this.originalName = originalName;
        this.description = description;
        this.voteCount = voteCount;
        this.voteAvg = voteAvg;
        this.releaseDate = releaseDate;
        this.mediaType = mediaType;
        this.runtime = runtime;
        this.genres = genres;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(double voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}

