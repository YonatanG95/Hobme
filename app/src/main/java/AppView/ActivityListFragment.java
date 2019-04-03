package AppView;

import AppModel.Activity;
import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CustomViewModelFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentActivityListBinding;

import java.util.List;

public class ActivityListFragment extends Fragment {

    private FragmentActivityListBinding mActivityListBinding;
    private ActivityListViewModel mActivityListViewModel;
    private CustomViewModelFactory viewModelFactory;
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;

    public ActivityListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivityListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container, false);

        bindData();

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ActivityListViewModel.class);


        //TODO delete when type ready
        mActivityListViewModel.getActivities().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                adapter.setActivities(activities);
            }
        });

        //TODO uncomment when type ready
//        mActivityListViewModel.getActivitiesByType().observe(this, new Observer<List<Activity>>() {
//             @Override
//             public void onChanged(List<Activity> activities) {
//                 adapter.setActivities(activities);
//             }
//        });

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
        Log.d("CHECK", "button clicked");
        Fragment createActivity = new CreateActivityFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activities_fragment_container, createActivity );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
