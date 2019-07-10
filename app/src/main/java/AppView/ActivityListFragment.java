package AppView;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CustomViewModelFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentActivityListBinding;

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


        //TODO delete when type ready
        mActivityListViewModel.getActivities().observe(getViewLifecycleOwner(), new Observer<PagedList<Activity>>() {
            @Override
            public void onChanged(PagedList<Activity> activities) {
                adapter.submitList(activities);
            }
        });

        bindData();
        passUser();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return mActivityListBinding.getRoot();
    }

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
