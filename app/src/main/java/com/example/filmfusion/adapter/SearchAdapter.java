package com.example.filmfusion.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.filmfusion.HomeActivity;
import com.example.filmfusion.MovieDetailPage;
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
    static OnItemClickListener clickListener;
    private AppDatabase db;

    public SearchAdapter( Context context,List<CombineMovies> movieList) {
        this.movieList = movieList;
        this.context=context;
        db = DatabaseClient.getInstance(context).getAppDatabase();

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
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.posterImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailPage.class);
                intent.putExtra("originalTitle",movie.getTitle());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("posterPath", movie.getPosterPath());
                intent.putExtra("releaseDate", movie.getReleaseDate());
                intent.putExtra("voteAverage", movie.getVoteAverage());
                intent.putExtra("id", movie.getId());
                intent.putExtra("backdropPath",movie.getPosterPath());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void SetOnItemClickListener(SearchAdapter.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        ImageView posterImageView;

        Button removeButton;


        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            posterImageView = itemView.findViewById(R.id.movie_poster);
            removeButton= itemView.findViewById(R.id.removeButton);
            removeButton.setVisibility(View.GONE);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }
    }
}
