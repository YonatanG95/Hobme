package AppView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentActivityListBinding;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppModel.LocalData;
import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CustomViewModelFactory;

public class ActivityListFragment extends Fragment{

    private FragmentActivityListBinding mActivityListBinding;
    private ActivityListViewModel mActivityListViewModel;
    private CustomViewModelFactory viewModelFactory;
    private ActivityAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mActivityListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ActivityListViewModel.class);


        bindData();
        passUser();
        initializeUI();

//        mActivityListViewModel.setSortText("");
        mActivityListViewModel.getActivities().observe(getViewLifecycleOwner(), new Observer<PagedList<Activity>>() {
            @Override
            public void onChanged(PagedList<Activity> activities) {
                adapter.submitList(activities);
            }
        });

        return mActivityListBinding.getRoot();
    }

    private void initializeUI(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//        ((ActivitiesFragmentsContainer)getActivity()).showBottomNav();
        setHasOptionsMenu(true);
    }

//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.top_bar_list, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.option_duration:
//                mActivityListViewModel.setSortText(LocalData.SORT_DURATION);
//                break;
//
//            case R.id.option_start:
//                mActivityListViewModel.setSortText(LocalData.SORT_START);
//                break;
//        }
//        return true;
//    }

    private void bindData()
    {
        adapter = new ActivityAdapter();
        mActivityListBinding.setAdapter(adapter);
        mActivityListBinding.setHandler(this);
    }

    public void createActivityBtnClick(View view)
    {
//        Navigation.findNavController(view).navigate(R.id.actListToCreate);
        ActivityListFragmentDirections.ActListToCreate action = ActivityListFragmentDirections.actListToCreate(mActivityListViewModel.getCurrUser());
        Navigation.findNavController(view).navigate(action);
    }

    private void passUser(){
        ActivityListFragmentArgs args = ActivityListFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        mActivityListViewModel.setCurrUser(user);
        adapter.setCurrUser(user);
    }



}
