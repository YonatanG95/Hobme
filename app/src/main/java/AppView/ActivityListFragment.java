package AppView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.google.android.material.navigation.NavigationView;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentActivityListBinding;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CustomViewModelFactory;

/**
 * Presents a recyclerview of activities
 */
public class ActivityListFragment extends Fragment{

    private FragmentActivityListBinding mActivityListBinding;
    private ActivityListViewModel mActivityListViewModel;
    private CustomViewModelFactory viewModelFactory;
    private ActivityAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivityListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ActivityListViewModel.class);

        bindData();
        passUser();
        initializeUI();

        //Observes the activity list live data for changes - passes data to adapter
        mActivityListViewModel.getActivities().observe(getViewLifecycleOwner(), new Observer<PagedList<Activity>>() {
            @Override
            public void onChanged(PagedList<Activity> activities) {
                adapter.submitList(activities);
            }
        });

        return mActivityListBinding.getRoot();
    }

    /**
     * Handles UI modifications
     */
    private void initializeUI(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        TextView title = navigationView.getHeaderView(0).findViewById(R.id.nav_title);
        title.setText(mActivityListViewModel.getCurrUser().getFullName());
        TextView sub = navigationView.getHeaderView(0).findViewById(R.id.nav_sub);
        sub.setText(mActivityListViewModel.getCurrUser().getEmail());
    }

    /**
     * Set databinding parameters
     */
    private void bindData()
    {
        adapter = new ActivityAdapter();
        mActivityListBinding.setAdapter(adapter);
        mActivityListBinding.setHandler(this);
    }

    /**
     * Handle floating button (create activity) press - navigate to CreateActivityFragment
     * @param view
     */
    public void createActivityBtnClick(View view)
    {
        ActivityListFragmentDirections.ActListToCreate action = ActivityListFragmentDirections.actListToCreate(mActivityListViewModel.getCurrUser());
        Navigation.findNavController(view).navigate(action);
    }

    /**
     * Gets the currently logged user (passed by login fragment)
     */
    private void passUser(){
        ActivityListFragmentArgs args = ActivityListFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        mActivityListViewModel.setCurrUser(user);
        adapter.setCurrUser(user);
    }
}
