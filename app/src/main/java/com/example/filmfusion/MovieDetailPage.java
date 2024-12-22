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
    ImageView share;

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
        // Extract movie ID from the deep link (if it exists)
        Uri data = getIntent().getData();
        if (data != null && data.getScheme().equals("filmfusion") && data.getHost().equals("movie")) {
            String movieIdString = data.getLastPathSegment();
            int movieIdFromLink = Integer.parseInt(movieIdString);

            // Use the extracted movie ID to fetch the movie details (or show it if it's already available)
            loadMovieDetails(movieIdFromLink);
        } else {
            // Normal data retrieval (intent from other activities)
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

        // Retrieve data from Intent
        originalTitle = getIntent().getStringExtra("originalTitle");
        String overview = getIntent().getStringExtra("overview");
        String posterPath = getIntent().getStringExtra("posterPath");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        movieId= getIntent().getIntExtra("id", -1);
        double voteAverage = getIntent().getDoubleExtra("voteAverage", 0.0);

        // Set data to UI elements
        titleTextView.setText(originalTitle);
        overviewTextView.setText(overview);
        releaseDateTextView.setText("Release Date: " + releaseDate);
        voteAverageTextView.setText("Rating: " + voteAverage);

        // Load poster image using Glide
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/" + posterPath) // Full URL for poster image
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
    }

    private void shareMovie() {
//        String shareMessage = "Check out this movie: " + originalTitle + "\n"
//                 + "\n" +
//                "Watch it here: filmfusion://movie/" + movieId;
//
//        // Create the intent to share the movie details
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//
//        // Start the sharing activity
//        startActivity(Intent.createChooser(shareIntent, "Share movie via"));

        String webUrl = "http://www.filmfusion.com/movie/" + movieId; // Web URL (if you have a web version)

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, webUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Movie"));
    }



    private void updateBookmarkUI(boolean isBookmarked) {

        if (isBookmarked) {
            bookmarkButton.setText("Remove Bookmark"); // Example text
            bookmarkButton.setBackgroundColor(Color.RED); // Example styling
        } else {
            bookmarkButton.setText("Add Bookmark");
            bookmarkButton.setBackgroundColor(Color.GREEN);
        }
    }

    private void loadMovieDetails(int movieId) {
        if (movieId == -1) {
            // Handle the case where the movie ID is invalid or not passed
            Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            finish(); // Optionally finish the activity
        } else {
            Toast.makeText(this, "movie found", Toast.LENGTH_SHORT).show();
            // Continue with loading movie details
        }
    }

}


