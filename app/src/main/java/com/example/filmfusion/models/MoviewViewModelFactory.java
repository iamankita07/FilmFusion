package com.example.filmfusion.models;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.filmfusion.repository.MovieRepository;

public class MoviewViewModelFactory implements ViewModelProvider.Factory {
    private final MovieRepository repository;

    // Constructor to pass MovieRepository
    public MoviewViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviewViewModel.class)) {
            return (T) new MoviewViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
