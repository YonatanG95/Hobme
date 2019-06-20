package AppViewModel;

import AppModel.Entity.Activity;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;

    //private AppExecutors mExecutor;

    public ActivityListViewModel(AppRepository repository){//, AppExecutors mExecutor){
        this.repository = repository;
        //this.mExecutor = mExecutor;
    }

    //TODO delete when type ready
    public LiveData<PagedList<Activity>> getActivities()
    {
        return repository.getActivities();
    }

//    public LiveData<List<Activity>> getActivitiesByType(int activityTypeId){
//        return repository.getActivitiesByType(activityTypeId);
//    }

//    //TODO delete this
//    public void deleteAllActivities(){
//        //mExecutor.diskIO().execute(() ->
//        repository.deleteAllActivities();
//    }
}
