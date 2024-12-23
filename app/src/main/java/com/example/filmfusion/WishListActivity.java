package com.example.filmfusion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmfusion.adapter.MovieAdapter;
import com.example.filmfusion.adapter.WishListAdapter;
import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.CombineMovies;
import com.example.filmfusion.models.MoviewViewModel;
import com.example.filmfusion.models.MoviewViewModelFactory;
import com.example.filmfusion.models.NowPlayingModel;
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
    ImageView back_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wish_list);
        assignId();

    }

    private void assignId() {
        context= this;
        recyclerView = findViewById(R.id.recyclerView);
        back_button= findViewById(R.id.back_button);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 items per row
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(WishListActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        movieAdapter.SetOnItemClickListener(new WishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookMarkMovies movies = combinedMovies.get(position);
                Intent intent = new Intent(WishListActivity.this, MovieDetailPage.class);
                intent.putExtra("originalTitle",movies.getOriginalTitle());
                intent.putExtra("overview", movies.getOverview());
                intent.putExtra("posterPath", movies.getPosterPath());
                intent.putExtra("releaseDate", movies.getReleaseDate());
                intent.putExtra("voteAverage", movies.getVoteAverage());
                intent.putExtra("backdropPath",movies.getBackdropPath());
                intent.putExtra("id", movies.getId());
                startActivity(intent);
            }
        });




    }


}