package com.kkmovies.kankanmovies.Helpers;

import com.kkmovies.kankanmovies.Models.Genre;

import java.util.List;

// class to parse List of Genre items
public class GenresHelper {

    public static String getGenres(List<Genre> genres){
        String genre = "";
        if(genres!=null){

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

        }
        return genre;
    }
}
