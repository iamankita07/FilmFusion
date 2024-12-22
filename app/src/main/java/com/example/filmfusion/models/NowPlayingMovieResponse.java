package com.example.filmfusion.models;

import java.util.List;

public class NowPlayingMovieResponse {

    private List<NowPlayingModel> results;

    public List<NowPlayingModel> getResults() {
        return results;
    }

    public void setResults(List<NowPlayingModel> results) {
        this.results = results;
    }
}
