package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;
import com.kkmovies.kankanmovies.Models.SearchResult;

import java.util.List;

public class ResponseMultiSearch {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalRes;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<SearchResult> results;

    public ResponseMultiSearch(int page, int totalRes, int totalPages, List<SearchResult> results) {
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

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

}
