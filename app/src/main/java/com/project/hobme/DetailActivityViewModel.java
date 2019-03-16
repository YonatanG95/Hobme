package com.project.hobme;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailActivityViewModel extends ViewModel {

    private MutableLiveData<ActivityEntry> mActivity;

    public DetailActivityViewModel(AppRepository repository, int id){
        mActivity = new MutableLiveData<>();
    }

    public MutableLiveData<ActivityEntry> getActivity()
    {
        return mActivity;
    }

    public void setActivity(MutableLiveData<ActivityEntry> activityEntry)
    {
        mActivity = activityEntry;
    }
}
