package com.project.hobme.UI;

import android.util.Log;

import com.project.hobme.Data.AppRepository;
import com.project.hobme.Data.Database.ActivityEntry;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailActivityViewModel extends ViewModel {

    private MutableLiveData<ActivityEntry> mActivity;

    public DetailActivityViewModel(AppRepository repository, int id){
        Log.d("Check", "VM - DetailActivityViewModel");
        mActivity = new MutableLiveData<>();
    }

    public MutableLiveData<ActivityEntry> getActivity()
    {
        Log.d("Check", "VM - getActivity");
        return mActivity;
    }

    public void setActivity(MutableLiveData<ActivityEntry> activityEntry)
    {
        Log.d("Check", "VM - setActivity");
        mActivity = activityEntry;
    }
}
