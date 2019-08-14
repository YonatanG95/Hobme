package AppViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import DataSources.AppRepository;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;
    private User currUser;

    public ActivityListViewModel(AppRepository repository){
        this.repository = repository;
    }

    public LiveData<PagedList<Activity>> getActivities()
    {
        return repository.getActivities();
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public User getCurrUser(){
        return this.currUser;
    }

}
