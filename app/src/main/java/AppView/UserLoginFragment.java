package AppView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.hobme.R;
import com.project.hobme.databinding.UserLoginFragmentBinding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import AppModel.Entity.SimplePlace;
import AppUtils.DataConverters;
import AppUtils.InputValidator;
import AppUtils.InjectorUtils;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.UserLoginViewModel;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class UserLoginFragment extends Fragment {

    private UserLoginViewModel mViewModel;
    private UserLoginFragmentBinding binding;
    private CustomViewModelFactory viewModelFactory;
    private final String API_KEY = "AIzaSyBKjSLWH58p7jfzEOoysCLnnCQ2rgFkEWI";
    public static PlacesClient placesClient;
    public static List<Place.Field> placeFields;
    public SimplePlace currLocation;
    private final int REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_login_fragment, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserLoginViewModel.class);

        initializeLocationSDK();
        getCurrLocation();

        bindData();

        initializeUI();

        return binding.getRoot();
    }

    /**
     * Handles UI modifications
     */
    private void initializeUI() {
        binding.btnLogin.setEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//        ((ActivitiesFragmentsContainer)getActivity()).hideBottomNav();
    }

    /**
     * Using places API key to initialize places API
     */
    private void initializeLocationSDK() {
        // Initialize the SDK
        Places.initialize(getContext(), API_KEY);

        // Create a new Places client instance
        placesClient = Places.createClient(getContext());

        // Use fields to define the data types to return.
        placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
    }


    public void getCurrLocation() {

        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.newInstance(placeFields);

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    Place estimated = response.getPlaceLikelihoods().get(0).getPlace();
                    currLocation = DataConverters.placeToSimplePlace(estimated);
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.d("Login", "Place not found: " + apiException.getStatusCode());
                    }
                    currLocation = new SimplePlace();
                }
                //TODO move to onCreate & create a pre-login page with data initialization
                checkUserStatus();
            });
        }
        else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
            Log.d("Login", "No permissions");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                                  String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrLocation();
                } else {
                    currLocation = new SimplePlace();
                }
                return;
            }
        }
    }

    /**
     * If user session exists - navigate directly to main page (ActivityListFragment)
     */
    private void checkUserStatus() {
        mViewModel.isAlreadyLoggedIn(binding.getRoot(), currLocation);
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
        mViewModel.userLogInEmail(this, view, currLocation);
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



