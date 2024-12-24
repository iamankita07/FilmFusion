package com.example.filmfusion.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("movie/now_playing")
    Call<NowPlayingMovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<TrendingMovieResponse> getTrendingMovies(@Query("api_key") String apiKey, @Query("page") int page);
}
