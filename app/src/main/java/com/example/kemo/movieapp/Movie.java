package com.example.kemo.movieapp;

import io.realm.RealmObject;

/**
 * Created by kemo on 17/10/2016.
 */
public class Movie extends RealmObject{
    private String  title;
    private String overView;
    private double voteAverage;
    private int movieId;
    private String releaseData;
    private String posterPath;
    private double popularity;
    private String originalTitle;
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = "http://image.tmdb.org/t/p/w185/" + posterPath;
    }


    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }



    public String getOverView() {
        return overView;
    }

    public void setOverView(String overview) {
        this.overView = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
