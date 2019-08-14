package AppViewModel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import DataSources.AppRepository;

public class CreateActivityViewModel extends ViewModel {

    private AppRepository repository;
    private MutableLiveData<Activity> activity;
    private User currUser;

    public CreateActivityViewModel(AppRepository repository)//, AppExecutors mExecutor)
    {
        this.repository = repository;
    }

    public void insertActivity(View view) {
        repository.insertActivity(this.activity.getValue(), currUser, view);
    }

    public LiveData<Activity> getActivity(){
        if (activity == null){
            activity = new MutableLiveData<>();
            Activity newActivity = new Activity();
            activity.setValue(newActivity);
        }
        return this.activity;
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public LiveData<List<String>> getCategories(){
        return repository.getCategoriesNames();
    }

    public LiveData<List<String>> getTypeNamesByCategory(String categoryId){
        return repository.getTypeNamesByCategory(categoryId);
    }

    public LiveData<String> getCategoryIdByName(String categoryName){
        return repository.getCategoryIdByName(categoryName);
    }
}
