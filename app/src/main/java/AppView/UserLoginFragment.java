package AppView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.hobme.R;
import com.project.hobme.databinding.UserLoginFragmentBinding;

import AppUtils.InputValidator;
import AppUtils.InjectorUtils;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.UserLoginViewModel;


public class UserLoginFragment extends Fragment {

    private UserLoginViewModel mViewModel;
    private UserLoginFragmentBinding binding;
    private CustomViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_login_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserLoginViewModel.class);

        checkUserStatus();

        bindData();

        binding.btnLogin.setEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((ActivitiesFragmentsContainer)getActivity()).hideBottomNav();

        return binding.getRoot();
    }

    private void checkUserStatus() {
        mViewModel.isAlreadyLoggedIn(binding.getRoot());
    }

    private void bindData() {
        binding.setView(mViewModel);
        binding.setHandler(this);
        binding.setLifecycleOwner(this);
    }

    public void loginBtn(View view){
        mViewModel.userLogInEmail(this, view);
    }

    public void registerBtn(View view){
        Navigation.findNavController(view).navigate(R.id.loginToRegister);
    }


    public void validation(CharSequence s, int start, int before, int count){
        if(InputValidator.isPasswordValid(binding.inputPasswordLayout)
                & InputValidator.isEmailValid(binding.inputEmailLayout)){
            binding.btnLogin.setEnabled(true);
        }
        else {
            binding.btnLogin.setEnabled(false);
        }
    }
}



