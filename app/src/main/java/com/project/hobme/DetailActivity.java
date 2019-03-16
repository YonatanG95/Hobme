package com.project.hobme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailActivity extends FragmentActivity{

    private DetailActivityViewModel mViewModel;
    private DetailActivityViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mViewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        mViewModel.getActivity().observe(this, activityEntry -> {
            if (activityEntry != null) bindActivityToUI(activityEntry);
        });
        //factory = InjectorUtils.provideDetailViewModelFactory(this, id);
        ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
}
    private void bindActivityToUI(ActivityEntry activityEntry) {

    }
}
