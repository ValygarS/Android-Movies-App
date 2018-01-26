package com.kkmovies.kankanmovies.Models;

import com.google.gson.annotations.SerializedName;
import com.kkmovies.kankanmovies.Models.Video;

import java.util.List;


public class ResponseVideos {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Video> videos;

    public ResponseVideos(int id, List<Video> videos) {
        this.id = id;
        this.videos = videos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
