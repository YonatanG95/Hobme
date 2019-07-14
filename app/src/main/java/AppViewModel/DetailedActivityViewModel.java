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

    public void deleteActivity(){
        currUser.getActivitiesMemberIds().remove(activity.getValue().getId());
        currUser.getMyActivitiesIds().remove(activity.getValue().getId());
        repository.deleteActivity(activity.getValue());
        repository.updateUser(currUser);
    }

    public void joinActivity(){
        activity.getValue().setCurrMembers(activity.getValue().getCurrMembers()+1);
        activity.getValue().getMembersIds().add(currUser.getId());
        currUser.getActivitiesMemberIds().add(activity.getValue().getId());
        repository.updateActivity(activity.getValue());
        repository.updateUser(currUser);
    }

    public void leaveActivity(){
        activity.getValue().setCurrMembers(activity.getValue().getCurrMembers()-1);
        activity.getValue().getMembersIds().remove(currUser.getId());
        currUser.getActivitiesMemberIds().remove(activity.getValue().getId());
        repository.updateActivity(activity.getValue());
        repository.updateUser(currUser);
    }

    public User getCurrUser() {
        return currUser;
    }

    //TODO consider add user to factory
    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }
}
