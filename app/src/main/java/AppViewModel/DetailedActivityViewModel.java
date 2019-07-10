package AppViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import DataSources.AppRepository;

public class DetailedActivityViewModel extends ViewModel {

    private MutableLiveData<Activity> activity;
    private AppRepository repository;
    private User currUser;

    public DetailedActivityViewModel(AppRepository repository) {
        this.repository = repository;
        if (activity == null){
            activity = new MutableLiveData<>();
        }
    }

    public LiveData<Activity> getActivity(){
//        if (activity == null){
//            activity = new MutableLiveData<>();
//            Activity newActivity = new Activity();
////            Log.d("CHECK", "New act id: " + newActivity.getId());
//            activity.setValue(newActivity);
//        }
////        Log.d("Act", "Act ID: " + this.activity.getValue().getId());
//        return this.activity;
        return this.activity;
    }

    public void setActivity(Activity activity){
        this.activity.setValue(activity);
    }

    public void updateActivity(){
        repository.updateActivity(activity.getValue());
    }

    public User getCurrUser() {
        return currUser;
    }

    //TODO consider add user to factory
    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }
}
