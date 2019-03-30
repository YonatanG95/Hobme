package ViewModel;

import Model.AppRepository;
import Utils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final AppRepository repository;
    private final AppExecutors executors;

    public CustomViewModelFactory(AppRepository repository, AppExecutors executors) {
        this.repository = repository;
        this.executors = executors;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActivityListViewModel.class))
            return (T) new ActivityListViewModel(repository, executors);
        else
            throw new IllegalArgumentException("ViewModel Not Found");
    }
}
