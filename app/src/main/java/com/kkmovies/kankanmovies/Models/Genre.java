package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrew on 06/01/2018.
 */

public class Genre {

    @SerializedName("id")
    private int genreId;
    @SerializedName("name")
    private String genreName;

    public Genre(Integer genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
