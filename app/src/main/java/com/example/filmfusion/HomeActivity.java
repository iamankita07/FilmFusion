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
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.TrendingMovieModel;
import com.example.filmfusion.repository.MovieRepository;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    TextView now_playing_title, trending_title;
    RecyclerView recycler_now_playing, recycler_trending;
    private MoviewViewModel viewModel;
    private MovieAdapter adapter;
    private List<NowPlayingModel> nowPlayingMovies;
    private List<TrendingMovieModel> trendingMovies;

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
                NowPlayingModel movies = nowPlayingMovies.get(position);
                Intent intent = new Intent(HomeActivity.this, MovieDetailPage.class);
                intent.putExtra("originalTitle",movies.getOriginalTitle());
                intent.putExtra("overview", movies.getOverview());
                intent.putExtra("posterPath", movies.getPosterPath());
                intent.putExtra("releaseDate", movies.getReleaseDate());
                intent.putExtra("voteAverage", movies.getVoteAverage());
                intent.putExtra("id", movies.getId());
                startActivity(intent);
            }
        });

        trendingMoviewAdapter.SetOnItemClickListener(new TrendingMovieAdapter .OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TrendingMovieModel trendingMovieModel = trendingMovies.get(position);
                Intent intent = new Intent(HomeActivity.this, MovieDetailPage.class);
                intent.putExtra("originalTitle",trendingMovieModel.getOriginalTitle());
                intent.putExtra("overview", trendingMovieModel.getOverview());
                intent.putExtra("posterPath", trendingMovieModel.getPosterPath());
                intent.putExtra("releaseDate", trendingMovieModel.getReleaseDate());
                intent.putExtra("voteAverage", trendingMovieModel.getVoteAverage());
                intent.putExtra("id", trendingMovieModel.getId());
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
                nowPlayingMovies = movies;
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
                trendingMovies = movies;
            }
        });
    }
}