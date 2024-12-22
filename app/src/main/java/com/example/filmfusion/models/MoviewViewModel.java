package com.example.filmfusion.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmfusion.repository.MovieRepository;

import java.util.List;

public class MoviewViewModel extends ViewModel {
    private final MovieRepository repository;
    private LiveData<List<NowPlayingModel>> nowPlayingMovies;

    private LiveData<List<TrendingMovieModel>> trendingMovies;



    public MoviewViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<NowPlayingModel>> getNowPlayingMovies() {
        if (nowPlayingMovies == null) {
            nowPlayingMovies = repository.getNowPlayingMovies();
        }
        return nowPlayingMovies;
    }

    public LiveData<List<TrendingMovieModel>> getTrendingMovies() {
        if (trendingMovies == null) {
            trendingMovies = repository.getTrendingMovies();
        }
        return trendingMovies;
    }
}
