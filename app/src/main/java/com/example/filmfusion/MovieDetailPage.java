package com.example.filmfusion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.filmfusion.models.ApiServices;
import com.example.filmfusion.models.MoviewViewModel;
import com.example.filmfusion.models.MoviewViewModelFactory;
import com.example.filmfusion.repository.MovieRepository;
import com.example.filmfusion.roomdatabase.DatabaseClient;
import com.example.filmfusion.roomdatabase.db.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailPage extends AppCompatActivity {
    private TextView titleTextView, overviewTextView, releaseDateTextView, voteAverageTextView;
    private ImageView posterImageView;
    Button bookmarkButton;
    ImageView share,back_button;

    private MoviewViewModel viewModel;

    AppDatabase db;
    Context context;
    int movieId;
    AtomicBoolean isBookmarked;
    boolean newBookmarkStatus;
    String originalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_page);
        assignId();
        Uri data = getIntent().getData();
        if (data != null && data.getScheme().equals("filmfusion") && data.getHost().equals("movie")) {
            String movieIdString = data.getLastPathSegment();
            int movieIdFromLink = Integer.parseInt(movieIdString);
            loadMovieDetails(movieIdFromLink);
        } else {
            movieId = getIntent().getIntExtra("id", -1);
            loadMovieDetails(movieId);
        }

    }

    private void assignId() {
        context=this;
        titleTextView = findViewById(R.id.movieTitle);
        overviewTextView = findViewById(R.id.movieOverview);
        releaseDateTextView = findViewById(R.id.movieReleaseDate);
        voteAverageTextView = findViewById(R.id.movieVoteAverage);
        posterImageView = findViewById(R.id.moviePoster);
        bookmarkButton = findViewById(R.id.bookmarkButton);
        back_button=findViewById(R.id.back_button);
        share= findViewById(R.id.share);
        db= DatabaseClient.getInstance(this).getAppDatabase();
        ApiServices apiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
        MovieRepository repository = new MovieRepository(getApplicationContext(),apiService);

        MoviewViewModelFactory factory = new MoviewViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MoviewViewModel.class);
        originalTitle = getIntent().getStringExtra("originalTitle");
        String overview = getIntent().getStringExtra("overview");
        String posterPath = getIntent().getStringExtra("posterPath");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        String backdropPath= getIntent().getStringExtra("backdropPath");
        movieId= getIntent().getIntExtra("id", -1);
        double voteAverage = getIntent().getDoubleExtra("voteAverage", 0.0);
        titleTextView.setText(originalTitle);
        overviewTextView.setText(overview);
        releaseDateTextView.setText("Release Date: " + releaseDate);
        voteAverageTextView.setText("Rating: " + voteAverage);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/" + backdropPath) // Full URL for poster image
                .into(posterImageView);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean isBookmarkedNowPlaying = db.nowPlayingDao().isMovieBookmarked(movieId);
            boolean isBookmarkedTrending = db.trendingMovieDao().isMovieBookmarked(movieId);
            boolean isBookmarked = isBookmarkedNowPlaying || isBookmarkedTrending; // Combine results

            runOnUiThread(() -> {
                updateBookmarkUI(isBookmarked);
            });
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBookmarked = new AtomicBoolean(true);
                newBookmarkStatus = isBookmarked.get();

                if (bookmarkButton.getText().equals("Remove Bookmark")) {
                    viewModel.removeBookmarkNowPlaying(movieId);
                    viewModel.removeBookmarkTrending(movieId);
                    viewModel.updateBookmarkStatus(movieId, false);
                    viewModel.updateTrendingBookmarkStatus(movieId, false);
                    isBookmarked = new AtomicBoolean(false);
                    newBookmarkStatus = isBookmarked.get();
                } else {
                    viewModel.updateBookmarkStatus(movieId, true);
                    viewModel.updateTrendingBookmarkStatus(movieId, true);

                }

                runOnUiThread(() -> {
                    updateBookmarkUI(newBookmarkStatus);
                    isBookmarked.set(newBookmarkStatus); // Update local variable
                });
            }
        });

       share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               shareMovie();
           }
       });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MovieDetailPage.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void shareMovie() {
        String webUrl = "http://www.filmfusion.com/movie/" + movieId; // Web URL (if you have a web version)
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, webUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Movie"));
    }



    private void updateBookmarkUI(boolean isBookmarked) {

        if (isBookmarked) {
            bookmarkButton.setText("Remove Bookmark");
            bookmarkButton.setBackgroundColor(Color.BLUE);
            bookmarkButton.setBackgroundResource(R.drawable.rounded_button_background);
        } else {
            bookmarkButton.setText("Add Bookmark");
            bookmarkButton.setBackgroundColor(Color.BLUE);
            bookmarkButton.setBackgroundResource(R.drawable.rounded_button_background);
        }
    }

    private void loadMovieDetails(int movieId) {
        if (movieId == -1) {
            //Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //Toast.makeText(this, "movie found", Toast.LENGTH_SHORT).show();
        }
    }

}


