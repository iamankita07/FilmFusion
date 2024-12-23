package com.example.filmfusion.models;


public class CombineMovies {
    private int id;
    private String title;
    private String source;
    private String overview;
    private String releaseDate;
    private double popularity;
    private double voteAverage;


    private String posterPath;

    public CombineMovies(int id, String title, String source, String posterPath, String overview, String releaseDate, Double popularity, Double voteAverage) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.posterPath=posterPath;
        this.overview=overview;
        this.releaseDate=releaseDate;
        this.popularity=popularity;
        this.voteAverage=voteAverage;

    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}

