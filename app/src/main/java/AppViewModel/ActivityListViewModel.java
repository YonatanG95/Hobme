package AppViewModel;

import android.util.Log;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppUtils.DataConverters;
import AppView.ActivityListFragment;
import AppView.DetailedActivityFragmentArgs;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.Date;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;
    private User currUser;
//    private MutableLiveData<String> sortText = new MutableLiveData<>();
//    private LiveData<PagedList<Activity>> activities;

    public ActivityListViewModel(AppRepository repository){
        this.repository = repository;
        //Date d = new Date();
        //repository.fetchMoreActivities(DataConverters.toDate(d.getTime()));
//        activities = Transformations.switchMap(sortText, (input) -> {
//            if(input == null || input.equals("")){
//                return repository.getActivities("");
//            }
//            else return repository.getActivities(input);
//        });
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

//    public MutableLiveData<String> getSortText() {
//        return sortText;
//    }
//
//    public void setSortText(String sortText) {
//        this.sortText.setValue(sortText);
//    }
}
