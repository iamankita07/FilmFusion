package com.example.filmfusion.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.CombineMovies;
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.NowPlayingMovieResponse;
import com.example.filmfusion.models.TrendingMovieModel;
import com.example.filmfusion.models.TrendingMovieResponse;
import com.example.filmfusion.roomdatabase.DatabaseClient;
import com.example.filmfusion.roomdatabase.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiServices apiService;
    private final String apiKey = "df56fa6390cbcc1f8d5bfedd2ad667c3";
    Context context;
    AppDatabase db;

    public MovieRepository(Context context,ApiServices apiService) {
        this.apiService = apiService;
        this.context=context;
        this.db = DatabaseClient.getInstance(context).getAppDatabase();

    }


    public LiveData<List<NowPlayingModel>> getNowPlayingMovies() {
        return db.nowPlayingDao().getAllMovies();
    }

    // Fetch Now Playing Movies from API and save to database
    public void fetchNowPlayingMoviesFromApi() {
        apiService.getNowPlayingMovies(apiKey, 1).enqueue(new Callback<NowPlayingMovieResponse>() {
            @Override
            public void onResponse(Call<NowPlayingMovieResponse> call, Response<NowPlayingMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NowPlayingModel> nowPlayingMovies = response.body().getResults();

                    // Save data to Room in a background thread
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> db.nowPlayingDao().insertMovies(nowPlayingMovies));
                }
            }

            @Override
            public void onFailure(Call<NowPlayingMovieResponse> call, Throwable t) {
                // Handle API fetch failure (optional)
            }
        });
    }


    public LiveData<List<TrendingMovieModel>> getTrendingMovies() {
        return db.trendingMovieDao().getAllMovies();
    }

    // Fetch Now Playing Movies from API and save to database
    public void fetchTrendingMoviesFromApi() {
        apiService.getTrendingMovies(apiKey, 1).enqueue(new Callback<TrendingMovieResponse>() {
            @Override
            public void onResponse(Call<TrendingMovieResponse> call, Response<TrendingMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TrendingMovieModel> trendingMovieModels = response.body().getResults();

                    // Save data to Room in a background thread
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> db.trendingMovieDao().insertMovies(trendingMovieModels));
                }
            }

            @Override
            public void onFailure(Call<TrendingMovieResponse> call, Throwable t) {
                // Handle API fetch failure (optional)
            }
        });
    }


    public void updateNowPlayingBookmarkStatus(int movieId, boolean isBookmarked) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> db.nowPlayingDao().updateBookmarkStatus(movieId, isBookmarked));
    }

    public LiveData<List<NowPlayingModel>> getBookmarkedNowPlayingMovies() {
        return db.nowPlayingDao().getBookmarkedMovies();
    }

    public void updateTrendingBookmarkStatus(int movieId, boolean isBookmarked) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> db.trendingMovieDao().updateBookmarkStatus(movieId, isBookmarked));
    }

    public LiveData<List<TrendingMovieModel>> getBookmarkedTrendingMovies() {
        return db.trendingMovieDao().getBookmarkedMovies();
    }

    public LiveData<List<NowPlayingModel>> removeBookmarkNowPlaying(int movieId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.nowPlayingDao().removeBookmark(movieId);


        });

        return null;
    }
    public LiveData<List<TrendingMovieModel>> removeBookmarkTrending(int movieId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            db.trendingMovieDao().removeBookmark(movieId);

        });

        return null;
    }


    public LiveData<List<CombineMovies>> searchLocalMovies(String query) {
        MutableLiveData<List<CombineMovies>> liveData = new MutableLiveData<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            List<NowPlayingModel> nowPlayingMovies = db.nowPlayingDao().searchNowPlayingMovies(query);
            List<TrendingMovieModel> trendingMovies = db.trendingMovieDao().searchTrendingMovies(query);

            List<CombineMovies> combinedMovies = new ArrayList<>();

            for (NowPlayingModel movie : nowPlayingMovies) {
                combinedMovies.add(new CombineMovies(movie.getId(), movie.getTitle(), "Now Playing"));
            }

            for (TrendingMovieModel movie : trendingMovies) {
                combinedMovies.add(new CombineMovies(movie.getId(), movie.getTitle(), "Trending"));
            }

            liveData.postValue(combinedMovies);
        });

        return liveData;
    }



}
