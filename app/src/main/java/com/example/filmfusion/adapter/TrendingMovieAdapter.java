package com.example.filmfusion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmfusion.R;
import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.TrendingMovieModel;

import java.util.ArrayList;
import java.util.List;

public class TrendingMovieAdapter extends RecyclerView.Adapter<TrendingMovieAdapter.MovieViewHolder> {
    private List<TrendingMovieModel> movies = new ArrayList<>();
    static MovieAdapter.OnItemClickListener clickListener;

    public void setMovies(List<TrendingMovieModel> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        TrendingMovieModel movie = movies.get(position);
        holder.bind(movie);
    }

    public void SetOnItemClickListener(MovieAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView title;
        private final ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            poster = itemView.findViewById(R.id.movie_poster);
        }

        public void bind(TrendingMovieModel movie) {
            title.setText(movie.getTitle());
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .into(poster);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}

