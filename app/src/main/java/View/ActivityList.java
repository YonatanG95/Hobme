package View;

import Model.Activity;
import Utils.InjectorUtils;
import ViewModel.ActivityListViewModel;
import ViewModel.CustomViewModelFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.project.hobme.R;
import com.project.hobme.databinding.ActivityListBinding;

import java.util.List;

public class ActivityList extends AppCompatActivity {

    private ActivityListBinding mActivityListBinding;
    private ActivityListViewModel mActivityListViewModel;
    private CustomViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mActivityListBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        viewModelFactory = InjectorUtils.provideViewModelFactory(ActivityList.this);
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ActivityListViewModel.class);

//        mActivityListViewModel.getActivitiesByType().observe(this, new Observer<List<Activity>>() {
//             @Override
//             public void onChanged(List<Activity> activities) {
//             }
//        });
    }
}
