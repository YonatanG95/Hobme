package AppViewModel;

import androidx.lifecycle.ViewModel;

import DataSources.AppRepository;

public class ContainerViewModel extends ViewModel {

    private AppRepository repository;

    public ContainerViewModel(AppRepository repository){

        this.repository = repository;
    }

    public void logOut(){
        repository.logOutUser();
    }
}
