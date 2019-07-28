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

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
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
    private final String API_KEY = "AIzaSyBKjSLWH58p7jfzEOoysCLnnCQ2rgFkEWI";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_login_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserLoginViewModel.class);

        checkUserStatus();

        bindData();

        initializeUI();

        initializeLocationSDK();

        return binding.getRoot();
    }

    /**
     * Handles UI modifications
     */
    private void initializeUI() {
        binding.btnLogin.setEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((ActivitiesFragmentsContainer)getActivity()).hideBottomNav();
    }

    /**
     * Using places API key to initialize places API
     */
    private void initializeLocationSDK() {
        // Initialize the SDK
        Places.initialize(getContext(), API_KEY);

        // Create a new Places client instance
        PlacesClient placesClient = Places.createClient(getContext());
    }

    /**
     * If user session exists - navigate directly to main page (ActivityListFragment)
     */
    private void checkUserStatus() {
        mViewModel.isAlreadyLoggedIn(binding.getRoot());
    }

    /**
     * Set databinding parameters
     */
    private void bindData() {
        binding.setView(mViewModel);
        binding.setHandler(this);
        binding.setLifecycleOwner(this);
    }

    /**
     * Handles login button press - Firebase email login. On result - navigation to  ActivityListFragment
     * @param view
     */
    public void loginBtn(View view){
        mViewModel.userLogInEmail(this, view);
    }

    /**
     * Handles registration link press - navigate to UserRegisterFragment
     * @param view
     */
    public void registerBtn(View view){
        Navigation.findNavController(view).navigate(R.id.loginToRegister);
    }


    /**
     * Validates UI text fields. All "onTextChanged" attributes
     * refer this method on every text modification.
     * Parameters are meaningless ("onTextChanged" requirements)
     * @param s
     * @param start
     * @param before
     * @param count
     */
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



