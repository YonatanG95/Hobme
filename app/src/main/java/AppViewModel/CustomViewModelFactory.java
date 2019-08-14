package AppViewModel;

import DataSources.AppRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Basic class for all viewModels (a template)
 */
public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final AppRepository repository;

    /**
     * Constructor - gives the viewModel an access to the repository
     * @param repository
     */
    public CustomViewModelFactory(AppRepository repository){
        this.repository = repository;
    }

    /**
     * Creates a viewModel by request with basic parameters
     * @param modelClass
     * @param <T>
     * @return - a viewModel suitable to the requesting class
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActivityListViewModel.class))
            return (T) new ActivityListViewModel(repository);//, executors);
        else if (modelClass.isAssignableFrom(CreateActivityViewModel.class))
            return (T) new CreateActivityViewModel(repository);//, executors);
        else if (modelClass.isAssignableFrom(DetailedActivityViewModel.class))
            return (T) new DetailedActivityViewModel(repository);
        else if (modelClass.isAssignableFrom(UserLoginViewModel.class))
            return (T) new UserLoginViewModel(repository);
        else if (modelClass.isAssignableFrom(UserRegisterViewModel.class))
            return (T) new UserRegisterViewModel(repository);
        else if (modelClass.isAssignableFrom(ContainerViewModel.class))
            return (T) new ContainerViewModel(repository);
        else
            throw new IllegalArgumentException("AppViewModel Not Found");
    }
}
