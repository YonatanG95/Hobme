package AppViewModel;

import android.util.Log;

import AppModel.Entity.Activity;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateActivityViewModel extends ViewModel {

    private AppRepository repository;
    //private AppExecutors mExecutor;
    private MutableLiveData<Activity> activity;

    public CreateActivityViewModel(AppRepository repository)//, AppExecutors mExecutor)
    {
        this.repository = repository;
//        activity = new MutableLiveData<>();
//            Activity newActivity = new Activity();
        //this.mExecutor = mExecutor;
    }

    public void insertActivity(Activity activity) {
        //mExecutor.diskIO().execute(() ->
        repository.insertActivity(activity);
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
}
