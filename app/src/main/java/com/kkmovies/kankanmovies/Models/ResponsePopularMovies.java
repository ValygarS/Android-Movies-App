package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andrew on 02/01/2018.
 */

public class ResponsePopularMovies {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalRes;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<Movie> results;

    public ResponsePopularMovies(int page, int totalRes, int totalPages, List<Movie> results) {
        this.page = page;
        this.totalRes = totalRes;
        this.totalPages = totalPages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalRes() {
        return totalRes;
    }

    public void setTotalRes(int totalRes) {
        this.totalRes = totalRes;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
