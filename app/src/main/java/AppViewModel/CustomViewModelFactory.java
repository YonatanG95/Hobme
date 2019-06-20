package AppViewModel;

import DataSources.AppRepository;
import AppUtils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final AppRepository repository;
    //private final AppExecutors executors;

    public CustomViewModelFactory(AppRepository repository){//, AppExecutors executors) {
        this.repository = repository;
        //this.executors = executors;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActivityListViewModel.class))
            return (T) new ActivityListViewModel(repository);//, executors);
        else if (modelClass.isAssignableFrom(CreateActivityViewModel.class))
            return (T) new CreateActivityViewModel(repository);//, executors);
        else if (modelClass.isAssignableFrom(DetailedActivityViewModel.class))
            return (T) new DetailedActivityViewModel(repository);
        else
            throw new IllegalArgumentException("AppViewModel Not Found");
    }
}
