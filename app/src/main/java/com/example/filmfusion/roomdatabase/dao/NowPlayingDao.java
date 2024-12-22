package com.example.filmfusion.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.filmfusion.models.NowPlayingModel;

import java.util.List;
@Dao
public interface NowPlayingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<NowPlayingModel> movies);

    @Query("SELECT * FROM now_playing_movies")
    LiveData<List<NowPlayingModel>> getAllMovies();

    @Query("UPDATE now_playing_movies SET isBookmarked = :isBookmarked WHERE id = :movieId")
    void updateBookmarkStatus(int movieId, boolean isBookmarked);

    @Query("SELECT * FROM now_playing_movies WHERE isBookmarked = 1")
    LiveData<List<NowPlayingModel>> getBookmarkedMovies();

    @Query("SELECT isBookmarked FROM now_playing_movies WHERE id = :movieId")
    boolean isMovieBookmarked(int movieId);

    @Query("UPDATE now_playing_movies SET isBookmarked = 0 WHERE id = :movieId")
    void removeBookmark(int movieId);

    @Query("SELECT * FROM now_playing_movies WHERE title LIKE '%' || :query || '%'")
    List<NowPlayingModel> searchNowPlayingMovies(String query);

}
