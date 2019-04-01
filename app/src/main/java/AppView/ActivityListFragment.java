package AppView;

import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CustomViewModelFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentActivityListBinding;

public class ActivityListFragment extends AppCompatActivity {

    private FragmentActivityListBinding mActivityListBinding;
    private ActivityListViewModel mActivityListViewModel;
    private CustomViewModelFactory viewModelFactory;
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_list);

        mActivityListBinding = DataBindingUtil.setContentView(this, R.layout.fragment_activity_list);

        viewModelFactory = InjectorUtils.provideViewModelFactory(this);
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ActivityListViewModel.class);

//        mActivityListViewModel.getActivitiesByType().observe(this, new Observer<List<Activity>>() {
//             @Override
//             public void onChanged(List<Activity> activities) {
//                 adapter.setActivities(activities);
//             }
//        });

        adapter = new ActivityAdapter();
        mActivityListBinding.setAdapter(adapter);

    }
}
