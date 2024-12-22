package com.example.filmfusion;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmfusion.adapter.MovieAdapter;
import com.example.filmfusion.adapter.WishListAdapter;
import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.MoviewViewModel;
import com.example.filmfusion.models.MoviewViewModelFactory;
import com.example.filmfusion.repository.MovieRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WishListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private WishListAdapter movieAdapter;
    private MoviewViewModel viewModel;
    private List<BookMarkMovies> combinedMovies = new ArrayList<>();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wish_list);
        assignId();

    }

    private void assignId() {
        context= this;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new WishListAdapter(this,combinedMovies);
        recyclerView.setAdapter(movieAdapter);

        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
        MovieRepository repository = new MovieRepository(getApplicationContext(),apiService);

        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);

        // Combine the bookmarked NowPlaying and Trending movies
        viewModel.getBookmarkedMovies().observe(this, nowPlayingMovies -> {
            if (nowPlayingMovies != null) {
                combinedMovies.clear();
                combinedMovies.addAll(nowPlayingMovies);
                movieAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getBookmarkedTrendingMovies().observe(this, trendingMovies -> {
            if (trendingMovies != null) {
                combinedMovies.addAll(trendingMovies);
                movieAdapter.notifyDataSetChanged();
            }
        });

    }
}