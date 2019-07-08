package AppView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.project.hobme.databinding.UserRegisterFragmentBinding;

import AppUtils.InjectorUtils;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.UserLoginViewModel;
import AppViewModel.UserRegisterViewModel;

public class UserRegisterFragment extends Fragment {

    private UserRegisterViewModel mViewModel;
    private UserRegisterFragmentBinding binding;
    private CustomViewModelFactory viewModelFactory;

//    public static UserRegisterFragment newInstance() {
//        return new UserRegisterFragment();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_register_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserRegisterViewModel.class);

        bindData();

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        return binding.getRoot();
    }

    private void bindData() {
        binding.setLifecycleOwner(this);
        binding.setView(mViewModel);
        binding.setHandler(this);
    }

    public void createUserEmail(View view){
        mViewModel.createUserEmail(this, view);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(UserRegisterViewModel.class);
//    }

}
