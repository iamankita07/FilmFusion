package com.example.filmfusion.models;

import java.util.List;

public class TrendingMovieResponse {
    private List<TrendingMovieModel> results;

    public List<TrendingMovieModel> getResults() {
        return results;
    }

    public void setResults(List<TrendingMovieModel> results) {
        this.results = results;
    }
}
