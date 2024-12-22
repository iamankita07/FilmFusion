package com.example.filmfusion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filmfusion.adapter.MovieAdapter;
import com.example.filmfusion.adapter.TrendingMovieAdapter;
import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.MoviewViewModel;
import com.example.filmfusion.models.MoviewViewModelFactory;
import com.example.filmfusion.repository.MovieRepository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    TextView now_playing_title, trending_title;
    RecyclerView recycler_now_playing, recycler_trending;
    private MoviewViewModel viewModel;
    private MovieAdapter adapter;

    private TrendingMovieAdapter trendingMoviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        assignId();

    }

    private void assignId() {
        recycler_now_playing=findViewById(R.id.recycler_now_playing);
        recycler_trending=findViewById(R.id.recycler_trending);
        now_playing_title=findViewById(R.id.now_playing_title);
        trending_title=findViewById(R.id.trending_title);

        recycler_now_playing.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_trending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MovieAdapter();
        trendingMoviewAdapter = new TrendingMovieAdapter();
        recycler_now_playing.setAdapter(adapter);
        recycler_trending.setAdapter(trendingMoviewAdapter);
        getNowPlayingMovie();
        getTrendingVideos();
        onClickListener();
    }

    private void onClickListener() {
        adapter.SetOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this, MovieDetailPage.class);
                startActivity(intent);
            }
        });

        trendingMoviewAdapter.SetOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this, MovieDetailPage.class);
                startActivity(intent);
            }
        });
    }

    private void getNowPlayingMovie() {

        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);

        // Assuming you have a MovieDao setup for Room DB
        MovieRepository repository = new MovieRepository(apiService);


        // Create an instance of MoviewViewModel using the custom factory
        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);



        viewModel.getNowPlayingMovies().observe(this, movies -> {
            if (movies != null) {
                adapter.setMovies(movies);
            }
        });
    }

    private void getTrendingVideos() {
        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);

        // Assuming you have a MovieDao setup for Room DB
        MovieRepository repository = new MovieRepository(apiService);


        // Create an instance of MoviewViewModel using the custom factory
        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);
        viewModel.getTrendingMovies().observe(this, movies -> {
            if (movies != null) {
                trendingMoviewAdapter.setMovies(movies);
            }
        });
    }
}