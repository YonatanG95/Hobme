package AppViewModel;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppUtils.DataConverters;
import AppView.ActivityListFragment;
import AppView.DetailedActivityFragmentArgs;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.Date;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;
    private User currUser;

    public ActivityListViewModel(AppRepository repository){
        this.repository = repository;
        //Date d = new Date();
        //repository.fetchMoreActivities(DataConverters.toDate(d.getTime()));
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
