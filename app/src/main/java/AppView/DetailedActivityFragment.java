package AppView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentDetailedActivityBinding;

import AppModel.Entity.Activity;
import AppUtils.InjectorUtils;
import AppViewModel.CreateActivityViewModel;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.DetailedActivityViewModel;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link DetailedActivityFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link DetailedActivityFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class DetailedActivityFragment extends Fragment {

    private FragmentDetailedActivityBinding mDetailedActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private DetailedActivityViewModel detailedActivityViewModel;
    private Activity activity;

    public DetailedActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDetailedActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_activity, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        detailedActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DetailedActivityViewModel.class);

        bindData();

        return mDetailedActivityBinding.getRoot();
    }

    private void setActivity() {
        DetailedActivityFragmentArgs args = DetailedActivityFragmentArgs.fromBundle(getArguments());
        activity = args.getActivity();
        mDetailedActivityBinding.setActivity(activity);
    }

    private void bindData(){
        setActivity();
        mDetailedActivityBinding.setViewModel(detailedActivityViewModel);
        mDetailedActivityBinding.setLifecycleOwner(this);
    }
}
