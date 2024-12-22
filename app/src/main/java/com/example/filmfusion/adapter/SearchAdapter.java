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
import com.example.filmfusion.models.CombineMovies;
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.repository.MovieRepository;
import com.example.filmfusion.roomdatabase.DatabaseClient;
import com.example.filmfusion.roomdatabase.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MovieViewHolder> {

    private List<CombineMovies> movieList;
    Context context;
    private AppDatabase db;

    public SearchAdapter( Context context,List<CombineMovies> movieList) {
        this.movieList = movieList;
        this.context=context;
        db = DatabaseClient.getInstance(context).getAppDatabase();  // Initialize the database

    }
    public void setMovies(List<CombineMovies> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new MovieViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        CombineMovies movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());

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
