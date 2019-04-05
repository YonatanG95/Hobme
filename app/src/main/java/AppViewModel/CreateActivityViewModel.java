package AppViewModel;

import android.util.Log;
import android.widget.Toast;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import java.util.Date;
import java.util.Observable;
import java.util.Random;

import AppModel.Activity;
import AppModel.AppRepository;
import AppUtils.AppExecutors;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateActivityViewModel extends ViewModel {

    private AppRepository repository;
    private AppExecutors mExecutor;
    private MutableLiveData<Activity> activity;

    public CreateActivityViewModel(AppRepository repository, AppExecutors mExecutor)
    {
        this.repository = repository;
        this.mExecutor = mExecutor;
    }

    public void insertActivity(Activity activity){
        mExecutor.diskIO().execute(() -> repository.insertActivity(activity));
    }

//    public void createActivity()
//    {
//        insertActivity(activity.getValue());
//    }

    public LiveData<Activity> getActivity(){
        if (activity == null){
            activity = new MutableLiveData<>();
            Activity newActivity = new Activity();
            activity.setValue(newActivity);
        }
        return this.activity;
    }
}
