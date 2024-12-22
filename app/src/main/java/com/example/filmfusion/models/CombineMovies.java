package com.example.filmfusion.models;


public class CombineMovies {
    private int id;
    private String title;
    private String source; // Indicates whether the movie is from "Now Playing" or "Trending"

    public CombineMovies(int id, String title, String source) {
        this.id = id;
        this.title = title;
        this.source = source;
    }

    public int getId() {
        return id;
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
}

