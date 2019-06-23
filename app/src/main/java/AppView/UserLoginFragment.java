package AppView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.UserLoginFragmentBinding;

import AppUtils.InjectorUtils;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.DetailedActivityViewModel;
import AppViewModel.UserLoginViewModel;

public class UserLoginFragment extends Fragment {

    private UserLoginViewModel mViewModel;
    private UserLoginFragmentBinding binding;
    private CustomViewModelFactory viewModelFactory;

//
//    public static UserLoginFragment newInstance() {
//        return new UserLoginFragment();
//    }
//
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_login_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserLoginViewModel.class);

        bindData();

        return binding.getRoot();
    }

    private void bindData() {


    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(UserLoginViewModel.class);
//        // TODO: Use the ViewModel
//    }

}
