package com.project.hobme.UI;

import android.util.Log;

import com.project.hobme.Data.AppRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppRepository mRepository;
    private final int mId;

    public DetailActivityViewModelFactory(AppRepository repository, int id) {
        Log.d("Check", "VM Factory - DetailActivityViewModelFactory");
        this.mRepository = repository;
        this.mId = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Log.d("Check", "VM Factory - create");
        //noinspection unchecked
        return (T) new DetailActivityViewModel(mRepository, mId);
    }
}
