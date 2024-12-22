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

    // Expose Local Database LiveData
    public LiveData<List<NowPlayingModel>> getNowPlayingMovies() {
        return repository.getNowPlayingMovies();
    }

    public LiveData<List<TrendingMovieModel>> getTrendingMovies() {
        return repository.getTrendingMovies();
    }

    // Trigger API fetch if needed
    public void fetchMoviesFromApi() {
        repository.fetchNowPlayingMoviesFromApi();
    }


    public void updateBookmarkStatus(int movieId, boolean isBookmarked) {
        repository.updateNowPlayingBookmarkStatus(movieId, isBookmarked);
    }

    public LiveData<List<NowPlayingModel>> getBookmarkedMovies() {
        return repository.getBookmarkedNowPlayingMovies();

    }

    public LiveData<List<NowPlayingModel>> removeBookmarkNowPlaying(int movieId) {
        return repository.removeBookmarkNowPlaying(movieId);

    }

    public void updateTrendingBookmarkStatus(int movieId, boolean isBookmarked) {
        repository.updateTrendingBookmarkStatus(movieId, isBookmarked);
    }

    public LiveData<List<TrendingMovieModel>> getBookmarkedTrendingMovies() {
        return repository.getBookmarkedTrendingMovies();
    }

    public LiveData<List<TrendingMovieModel>> removeBookmarkTrending(int movieId) {
        return repository.removeBookmarkTrending(movieId);

    }

    public LiveData<List<CombineMovies>> searchLocalMovies(String query) {
        return repository.searchLocalMovies(query);
    }





}
