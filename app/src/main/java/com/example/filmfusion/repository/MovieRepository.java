package com.example.filmfusion.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.NowPlayingMovieResponse;
import com.example.filmfusion.models.TrendingMovieModel;
import com.example.filmfusion.models.TrendingMovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private final ApiServices apiService;
    private final String apiKey = "df56fa6390cbcc1f8d5bfedd2ad667c3";

    public MovieRepository(ApiServices apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<NowPlayingModel>> getNowPlayingMovies() {
        MutableLiveData<List<NowPlayingModel>> liveData = new MutableLiveData<>();

        apiService.getNowPlayingMovies(apiKey, 1).enqueue(new Callback<NowPlayingMovieResponse>() {
            @Override
            public void onResponse(Call<NowPlayingMovieResponse> call, Response<NowPlayingMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<NowPlayingMovieResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<TrendingMovieModel>> getTrendingMovies() {
        MutableLiveData<List<TrendingMovieModel>> liveData = new MutableLiveData<>();

        apiService.getTrendingMovies(apiKey, 1).enqueue(new Callback<TrendingMovieResponse>() {
            @Override
            public void onResponse(Call<TrendingMovieResponse> call, Response<TrendingMovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<TrendingMovieResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }
}
