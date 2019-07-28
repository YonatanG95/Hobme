package AppViewModel;

import android.util.Log;
import android.view.View;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CreateActivityViewModel extends ViewModel {

    private AppRepository repository;
    //private AppExecutors mExecutor;
    private MutableLiveData<Activity> activity;
    private User currUser;

    public CreateActivityViewModel(AppRepository repository)//, AppExecutors mExecutor)
    {
        this.repository = repository;
//        activity = new MutableLiveData<>();
//            Activity newActivity = new Activity();
        //this.mExecutor = mExecutor;
    }

    public void insertActivity(View view) {
        //mExecutor.diskIO().execute(() ->
        repository.insertActivity(this.activity.getValue(), currUser, view);
    }

    public LiveData<Activity> getActivity(){
        if (activity == null){
            activity = new MutableLiveData<>();
            Activity newActivity = new Activity();
//            Log.d("CHECK", "New act id: " + newActivity.getId());
            activity.setValue(newActivity);
        }
//        Log.d("Act", "Act ID: " + this.activity.getValue().getId());
        return this.activity;
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public List<String> getCategories(){
        return repository.getCategoriesNames();
    }
}
