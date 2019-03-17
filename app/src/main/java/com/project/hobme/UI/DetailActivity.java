package com.project.hobme.UI;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.hobme.Data.Database.ActivityEntry;
import com.project.hobme.Data.Database.UserEntry;
import com.project.hobme.R;
import com.project.hobme.Utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

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
        int id = 1;
        factory = InjectorUtils.provideDetailViewModelFactory(this, id);
        ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
    }

    private void bindActivityToUI(ActivityEntry activityEntry) {


        List<UserEntry> participants = activityEntry.getParticipants();

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, participants);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);

    }
}
