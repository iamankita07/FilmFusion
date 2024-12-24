package com.example.filmfusion.roomdatabase.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.filmfusion.models.NowPlayingModel;
import com.example.filmfusion.models.TrendingMovieModel;
import com.example.filmfusion.roomdatabase.dao.NowPlayingDao;
import com.example.filmfusion.roomdatabase.dao.TrendingPlayingDao;

@Database(entities = {TrendingMovieModel.class, NowPlayingModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrendingPlayingDao trendingMovieDao();
    public abstract NowPlayingDao nowPlayingDao();
}
