package AppViewModel;

import AppModel.Entity.Activity;
import AppUtils.DataConverters;
import DataSources.AppRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import java.util.Date;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;

    public ActivityListViewModel(AppRepository repository){
        this.repository = repository;
        //Date d = new Date();
        //repository.fetchMoreActivities(DataConverters.toDate(d.getTime()));
    }

    public LiveData<PagedList<Activity>> getActivities()
    {
        return repository.getActivities();
    }


}
