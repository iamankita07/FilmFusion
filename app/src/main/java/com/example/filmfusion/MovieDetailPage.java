package com.example.filmfusion;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class MovieDetailPage extends AppCompatActivity {
    private TextView titleTextView, overviewTextView, releaseDateTextView, voteAverageTextView;
    private ImageView posterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_page);
        assignId();

    }

    private void assignId() {
        titleTextView = findViewById(R.id.movieTitle);
        overviewTextView = findViewById(R.id.movieOverview);
        releaseDateTextView = findViewById(R.id.movieReleaseDate);
        voteAverageTextView = findViewById(R.id.movieVoteAverage);
        posterImageView = findViewById(R.id.moviePoster);

        // Retrieve data from Intent
        String originalTitle = getIntent().getStringExtra("originalTitle");
        String overview = getIntent().getStringExtra("overview");
        String posterPath = getIntent().getStringExtra("posterPath");
        String releaseDate = getIntent().getStringExtra("releaseDate");
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
    }
}