package AppView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentDetailedActivityBinding;

import AppModel.Entity.User;
import AppUtils.InjectorUtils;
import AppUtils.InputValidator;
import AppViewModel.CustomViewModelFactory;
import AppViewModel.DetailedActivityViewModel;

/**
 * Presents full activity information
 */
public class DetailedActivityFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDetailedActivityBinding mDetailedActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private DetailedActivityViewModel detailedActivityViewModel;
    private boolean userIsCreator = false;
    private boolean userIsMember = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDetailedActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_activity, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        detailedActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DetailedActivityViewModel.class);

        bindData();
        passUser();
        initializeUI();

        return mDetailedActivityBinding.getRoot();
    }

    /**
     * Inflate the menu. this adds items to the action bar.
     * @param menu
     * @param inflater
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Show action bar options only if user is the owner (creator) of the activity
        if(userIsCreator) {
            inflater.inflate(R.menu.top_bar_detailed, menu);
        }
    }

    /**
     * Handles action bar selections
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //Edit activity option - enable text fields
            case R.id.action_edit:
                mDetailedActivityBinding.joinActivityBtn.setVisibility(View.GONE);
                mDetailedActivityBinding.saveActivityBtn.setVisibility(View.VISIBLE);
                mDetailedActivityBinding.inputActNameLayout.getEditText().setEnabled(true);
                mDetailedActivityBinding.inputActInfoLayout.getEditText().setEnabled(true);
                break;

            //Delete activity
            case R.id.action_delete:
                deleteActivity(getView());
                break;
        }
        return true;
    }

    /**
     * Handles UI modifications
     */
    private void initializeUI() {
        //Sets map callback on map fragment
        SupportMapFragment mapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        //Present dates
        mDetailedActivityBinding.startDate.setText(detailedActivityViewModel.getActivity().getValue().startDateString());
        mDetailedActivityBinding.endDate.setText(detailedActivityViewModel.getActivity().getValue().endDateString());

        setHasOptionsMenu(true);

        //If user already joined or activity is full - join button grayed
        if(userIsCreator || userIsMember ||
                detailedActivityViewModel.getActivity().getValue().getCurrMembers() ==
                        detailedActivityViewModel.getActivity().getValue().getMaxMembers()){
            mDetailedActivityBinding.joinActivityBtn.setEnabled(false);
        }
    }

    /**
     * Gets the currently logged user (passed by activity list fragment)
     */
    private void passUser(){
        DetailedActivityFragmentArgs args = DetailedActivityFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        detailedActivityViewModel.setCurrUser(user);

        //Checks if user either creator or member
        if(user.getMyActivitiesIds().contains(detailedActivityViewModel.getActivity().getValue().getId())){
            userIsCreator = true;
        }
        else {
            userIsCreator = false;
        }
        if(user.getActivitiesMemberIds().contains(detailedActivityViewModel.getActivity().getValue().getId())){
            userIsMember = true;
        }
        else
        {
            userIsMember = false;
        }
    }

    /**
     * Gets the current (clicked) activity (passed by activity list fragment)
     */
    private void setActivity() {
        DetailedActivityFragmentArgs args = DetailedActivityFragmentArgs.fromBundle(getArguments());
        detailedActivityViewModel.setActivity(args.getActivity());
    }


    /**
     * Set databinding parameters
     */
    private void bindData(){
        setActivity();
        mDetailedActivityBinding.setViewModel(detailedActivityViewModel);
        mDetailedActivityBinding.setHandler(this);
        mDetailedActivityBinding.setLifecycleOwner(this);
    }

    /**
     * Handles activity update option click - UI changes
     * @param view
     */
    public void updateActivity(View view){
        detailedActivityViewModel.updateActivity();
        mDetailedActivityBinding.saveActivityBtn.setVisibility(View.GONE);
        mDetailedActivityBinding.joinActivityBtn.setVisibility(View.VISIBLE);
        mDetailedActivityBinding.inputActNameLayout.getEditText().setEnabled(false);
        mDetailedActivityBinding.inputActInfoLayout.getEditText().setEnabled(false);
    }

    /**
     * Handles activity delete option click
     * @param view
     */
    public void deleteActivity(View view){
        detailedActivityViewModel.deleteActivity();
        DetailedActivityFragmentDirections.ActDetailsToList action = DetailedActivityFragmentDirections.actDetailsToList(detailedActivityViewModel.getCurrUser());
        Navigation.findNavController(view).navigate(action);
    }

    /**
     * Handles activity join button click
     * @param view
     */
    public void joinActivity(View view){
        detailedActivityViewModel.joinActivity();
        mDetailedActivityBinding.joinActivityBtn.setEnabled(false);
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
        if(InputValidator.isValidField(mDetailedActivityBinding.inputActNameLayout)
                & InputValidator.isValidField(mDetailedActivityBinding.inputActInfoLayout)){
            mDetailedActivityBinding.saveActivityBtn.setEnabled(true);
        }
        else {
            mDetailedActivityBinding.saveActivityBtn.setEnabled(false);
        }
    }

    /**
     * Handles the map callback - show place on map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng placeCenter = new LatLng(detailedActivityViewModel.getActivity().getValue().getSimplePlace().getLatitude(),
                detailedActivityViewModel.getActivity().getValue().getSimplePlace().getLongitude());
        googleMap.addMarker(new MarkerOptions()
            .position(placeCenter).title(detailedActivityViewModel.getActivity().getValue().getSimplePlace().getName()));

        //Move camera to position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(placeCenter).zoom(16).tilt(30).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
