package AppView;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import java.util.Date;

import AppModel.Activity;
import AppUtils.InjectorUtils;
import AppViewModel.ActivityListViewModel;
import AppViewModel.CreateActivityViewModel;
import AppViewModel.CustomViewModelFactory;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateActivityFragment extends Fragment {

    private FragmentCreateActivityBinding mFragmentCreateActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private CreateActivityViewModel mActivityListViewModel;

    public CreateActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentCreateActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_activity, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CreateActivityViewModel.class);

        bindData();

        return mFragmentCreateActivityBinding.getRoot();
    }

    //Bind this fragment and viewmodel to xml using databinding
    private void bindData(){
        mFragmentCreateActivityBinding.setHandler(this);
        mFragmentCreateActivityBinding.setViewModel(mActivityListViewModel);
        mFragmentCreateActivityBinding.setLifecycleOwner(this);
    }

    //Create activity button clicked
    public void addActivityBtn(View view)
    {
        //Insert new activity using repository with a method of the ViewModel
        mActivityListViewModel.insertActivity(mActivityListViewModel.getActivity().getValue());

        //Move back to activities list Fragment
        Fragment listActivities = new ActivityListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.activities_fragment_container, listActivities );
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
