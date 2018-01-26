package com.kkmovies.kankanmovies.Database;

public class TvFirebase {

    public String id;
    public String name;
    public String posterPath;
    public String firstAirDate;
    public Boolean wishlist;
    public Boolean watchlist;

    public TvFirebase(){

    }

    public TvFirebase(String id, String name, String posterPath, String firstAirDate, Boolean wishlist, Boolean watchlist) {
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.firstAirDate = firstAirDate;
        this.wishlist = wishlist;
        this.watchlist = watchlist;
    }
}
