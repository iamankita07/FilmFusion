package com.example.filmfusion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmfusion.BookMarkMovies;
import com.example.filmfusion.R;
import com.example.filmfusion.repository.MovieRepository;
import com.example.filmfusion.roomdatabase.DatabaseClient;
import com.example.filmfusion.roomdatabase.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MovieViewHolder> {

    private List<BookMarkMovies> movieList;
    Context context;
    private MovieRepository movieRepository;
    private AppDatabase db;

    public WishListAdapter( Context context,List<BookMarkMovies> movieList) {
        this.movieList = movieList;
        this.context=context;
        db = DatabaseClient.getInstance(context).getAppDatabase();  // Initialize the database

    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new MovieViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        BookMarkMovies movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.posterImageView);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMovieFromWishlist(movie, position);

            }
        });
    }

    private void removeMovieFromWishlist(BookMarkMovies movie, int position) {
        // Check if the movie is from NowPlaying or Trending
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (movie.getType().equalsIgnoreCase("Now_Playing")) {
                db.nowPlayingDao().removeBookmark(movie.getId()); // Remove from NowPlaying database
            } else if (movie.getType().equalsIgnoreCase("Trending_Movie")) {
                db.trendingMovieDao().removeBookmark(movie.getId());// Remove from Trending database
            }

            // Remove from the local list (UI update)
            movieList.remove(position);
            ((Activity) context).runOnUiThread(() -> notifyItemRemoved(position));
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;

        Button removeButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            posterImageView = itemView.findViewById(R.id.movie_poster);
            removeButton= itemView.findViewById(R.id.removeButton);
        }
    }
}
