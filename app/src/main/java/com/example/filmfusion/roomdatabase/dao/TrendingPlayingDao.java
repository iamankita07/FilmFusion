package com.example.filmfusion.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.TrendingMovieModel;

import java.util.List;

@Dao
public interface TrendingPlayingDao {
    @Insert
    void insertMovies(List<TrendingMovieModel> movies);

    @Query("SELECT * FROM trending_movies")
    LiveData<List<TrendingMovieModel>> getAllMovies();

    @Query("UPDATE trending_movies SET isBookmarked = :isBookmarked WHERE id = :movieId")
    void updateBookmarkStatus(int movieId, boolean isBookmarked);

    @Query("SELECT * FROM trending_movies WHERE isBookmarked = 1")
    LiveData<List<TrendingMovieModel>> getBookmarkedMovies();

    @Query("SELECT isBookmarked FROM trending_movies WHERE id = :movieId")
    boolean isMovieBookmarked(int movieId);

    @Query("UPDATE trending_movies SET isBookmarked = 0 WHERE id = :movieId")
    void removeBookmark(int movieId);

    @Query("SELECT * FROM trending_movies WHERE title LIKE '%' || :query || '%'")
    List<TrendingMovieModel> searchTrendingMovies(String query);
}
