package AppViewModel;

import AppModel.Activity;
import DataSources.AppRepository;
import AppUtils.AppExecutors;
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
            activity.setValue(newActivity);
        }
        return this.activity;
    }
}
