package com.example.filmfusion;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filmfusion.adapter.MovieAdapter;
import com.example.filmfusion.adapter.SearchAdapter;
import com.example.filmfusion.adapter.TrendingMovieAdapter;
import com.example.filmfusion.adapter.WishListAdapter;
import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.CombineMovies;
import com.example.filmfusion.models.MoviewViewModel;
import com.example.filmfusion.models.MoviewViewModelFactory;
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.TrendingMovieModel;
import com.example.filmfusion.repository.MovieRepository;
import com.example.filmfusion.roomdatabase.DatabaseClient;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<CombineMovies> combinedMoviesList = new ArrayList<>();
    private SearchAdapter movieAdapter;
    ImageView wishList;
    private EditText searchBar;
    LinearLayout search_layout, main_layout;
    RecyclerView searchRecycler_view;
    TextView noResultsTextView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        assignId();
        setupSearchBar();

    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void filterMovies(String query) {
        if (query.isEmpty()) {
            main_layout.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
        }else {
            List<CombineMovies> filteredMovies = new ArrayList<>();
            for (CombineMovies movie : combinedMoviesList) {
                if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredMovies.add(movie);
                }
            }
            if (filteredMovies.isEmpty()) {
                search_layout.setVisibility(View.VISIBLE);
                searchRecycler_view.setVisibility(View.GONE);
                noResultsTextView.setVisibility(View.VISIBLE);
            } else {
                search_layout.setVisibility(View.VISIBLE);
                searchRecycler_view.setVisibility(View.VISIBLE);
                noResultsTextView.setVisibility(View.GONE);
            }

            main_layout.setVisibility(View.GONE);
            search_layout.setVisibility(View.VISIBLE);
            searchRecycler_view.setAdapter(movieAdapter);
            movieAdapter.setMovies(filteredMovies);
        }


    }
    private void assignId() {
        context=this;
        recycler_now_playing=findViewById(R.id.recycler_now_playing);
        recycler_trending=findViewById(R.id.recycler_trending);
        now_playing_title=findViewById(R.id.now_playing_title);
        trending_title=findViewById(R.id.trending_title);
        searchRecycler_view=findViewById(R.id.searchRecycler_view);
        wishList=findViewById(R.id.wishList);
        main_layout=findViewById(R.id.main_layout);
        main_layout.setVisibility(View.VISIBLE);
        search_layout=findViewById(R.id.search_layout);
        search_layout.setVisibility(View.GONE);
        searchBar = findViewById(R.id.searchBar);
        noResultsTextView=findViewById(R.id.noResultsTextView);
        recycler_now_playing.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_trending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 items per row
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecycler_view.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(null);
        trendingMoviewAdapter = new TrendingMovieAdapter();
        movieAdapter=new SearchAdapter(context,combinedMoviesList);
        recycler_now_playing.setAdapter(adapter);
        recycler_trending.setAdapter(trendingMoviewAdapter);
        searchRecycler_view.setAdapter(movieAdapter);
        getNowPlayingMovie();
        getTrendingVideos();
        onClickListener();
        setupViewModel();
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
                intent.putExtra("backdropPath",movies.getBackdropPath());
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
                intent.putExtra("backdropPath",trendingMovieModel.getBackdropPath());

                startActivity(intent);
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
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

        MovieRepository repository = new MovieRepository(getApplicationContext(),apiService);
        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);


        viewModel.getNowPlayingMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                adapter.setMovies(movies);
                nowPlayingMovies = movies;
            } else {
                fetchNowPlayingFromApi(repository);
            }
        });
    }

    private void setupViewModel() {
        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);

        MovieRepository repository = new MovieRepository(getApplicationContext(), apiService);
        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);

        viewModel.getNowPlayingMovies().observe(this, nowPlayingMovies -> {
            if (nowPlayingMovies != null) {
                for (NowPlayingModel movie : nowPlayingMovies) {
                    combinedMoviesList.add(new CombineMovies(movie.getId(), movie.getTitle(), "Now Playing", movie.getPosterPath(), movie.getOverview(),movie.getReleaseDate(),movie.getPopularity(),movie.getVoteAverage()));
                }
                movieAdapter.setMovies(combinedMoviesList);
            }
        });

        viewModel.getTrendingMovies().observe(this, trendingMovies -> {
            if (trendingMovies != null) {
                for (TrendingMovieModel movie : trendingMovies) {
                    combinedMoviesList.add(new CombineMovies(movie.getId(), movie.getTitle(), "Trending", movie.getPosterPath(), movie.getOverview(),movie.getReleaseDate(),movie.getPopularity(),movie.getVoteAverage()));
                }
                movieAdapter.setMovies(combinedMoviesList);
            }
        });
    }
    private void fetchNowPlayingFromApi(MovieRepository repository) {
        repository.fetchNowPlayingMoviesFromApi();
    }

    private void getTrendingVideos() {
        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);

        MovieRepository repository = new MovieRepository(getApplicationContext(),apiService);
        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);


        viewModel.getTrendingMovies().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                trendingMoviewAdapter.setMovies(movies);
                trendingMovies = movies;
            } else {
                fetchTrendingFromApi(repository);
            }
        });
    }

    private void fetchTrendingFromApi(MovieRepository repository) {
        repository.fetchTrendingMoviesFromApi();
    }

}