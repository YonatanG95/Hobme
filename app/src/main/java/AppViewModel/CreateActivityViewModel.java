package AppViewModel;

import java.util.Observable;

import AppModel.Activity;
import AppModel.AppRepository;
import AppUtils.AppExecutors;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class CreateActivityViewModel extends ViewModel {

    private AppRepository repository;
    private AppExecutors mExecutor;

    public CreateActivityViewModel(AppRepository repository, AppExecutors mExecutor)
    {
        this.repository = repository;
        this.mExecutor = mExecutor;
    }

    public void insertActivity(Activity activity){
        mExecutor.diskIO().execute(() -> repository.insertActivity(activity));
    }
}
