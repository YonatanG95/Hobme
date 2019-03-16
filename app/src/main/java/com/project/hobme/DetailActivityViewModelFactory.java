package com.project.hobme;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppRepository mRepository;
    private final int mId;

    public DetailActivityViewModelFactory(AppRepository repository, int id) {
        this.mRepository = repository;
        this.mId = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mRepository, mId);
    }
}
