package AppView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentDetailedActivityBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import AppModel.Entity.Activity;
import AppModel.Entity.User;
import AppUtils.InjectorUtils;
import AppUtils.InputValidator;
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
public class DetailedActivityFragment extends Fragment implements OnMapReadyCallback {

    private FragmentDetailedActivityBinding mDetailedActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private DetailedActivityViewModel detailedActivityViewModel;
    private boolean userIsCreator = false;
    private boolean userIsMember = false;

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
        passUser();
        initializeUI();

        return mDetailedActivityBinding.getRoot();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(userIsCreator) {
            inflater.inflate(R.menu.top_bar_detailed, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mDetailedActivityBinding.joinActivityBtn.setVisibility(View.GONE);
                mDetailedActivityBinding.saveActivityBtn.setVisibility(View.VISIBLE);
                mDetailedActivityBinding.inputActNameLayout.getEditText().setEnabled(true);
                mDetailedActivityBinding.inputActInfoLayout.getEditText().setEnabled(true);
                break;

            case R.id.action_delete:
                deleteActivity(getView());
        }
        return true;
    }

    private void initializeUI() {
        SupportMapFragment mapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mDetailedActivityBinding.startDate.setText(format.format(detailedActivityViewModel.getActivity().getValue()
                .getActivityStartDateTime()));
        mDetailedActivityBinding.endDate.setText(format.format(detailedActivityViewModel.getActivity().getValue()
                .getActivityEndDateTime()));
        setHasOptionsMenu(true);
        if(userIsCreator || userIsMember ||
                detailedActivityViewModel.getActivity().getValue().getCurrMembers() ==
                        detailedActivityViewModel.getActivity().getValue().getMaxMembers()){
            mDetailedActivityBinding.joinActivityBtn.setEnabled(false);
        }
    }

    private void passUser(){
        DetailedActivityFragmentArgs args = DetailedActivityFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        detailedActivityViewModel.setCurrUser(user);
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

    private void setActivity() {
        DetailedActivityFragmentArgs args = DetailedActivityFragmentArgs.fromBundle(getArguments());
        detailedActivityViewModel.setActivity(args.getActivity());
    }

    private void bindData(){
        setActivity();
        mDetailedActivityBinding.setViewModel(detailedActivityViewModel);
        mDetailedActivityBinding.setHandler(this);
        mDetailedActivityBinding.setLifecycleOwner(this);
    }

    public void updateActivity(View view){
        detailedActivityViewModel.updateActivity();
        mDetailedActivityBinding.saveActivityBtn.setVisibility(View.GONE);
        mDetailedActivityBinding.joinActivityBtn.setVisibility(View.VISIBLE);
        mDetailedActivityBinding.inputActNameLayout.getEditText().setEnabled(false);
        mDetailedActivityBinding.inputActInfoLayout.getEditText().setEnabled(false);
//        DetailedActivityFragmentDirections.ActDetailsToList action = DetailedActivityFragmentDirections.actDetailsToList(detailedActivityViewModel.getCurrUser());
//        Navigation.findNavController(view).navigate(action);
    }

    public void deleteActivity(View view){
        detailedActivityViewModel.deleteActivity();
        DetailedActivityFragmentDirections.ActDetailsToList action = DetailedActivityFragmentDirections.actDetailsToList(detailedActivityViewModel.getCurrUser());
        Navigation.findNavController(view).navigate(action);
    }

    public void joinActivity(View view){
        detailedActivityViewModel.joinActivity();
        mDetailedActivityBinding.joinActivityBtn.setEnabled(false);
    }

    public void validation(CharSequence s, int start, int before, int count){
        if(InputValidator.isValidField(mDetailedActivityBinding.inputActNameLayout)
                & InputValidator.isValidField(mDetailedActivityBinding.inputActInfoLayout)){
            mDetailedActivityBinding.saveActivityBtn.setEnabled(true);
        }
        else {
            mDetailedActivityBinding.saveActivityBtn.setEnabled(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng placeCenter = new LatLng(detailedActivityViewModel.getActivity().getValue().getSimplePlace().getLatitude(),
                detailedActivityViewModel.getActivity().getValue().getSimplePlace().getLongitude());
        googleMap.addMarker(new MarkerOptions()
            .position(placeCenter).title(detailedActivityViewModel.getActivity().getValue().getSimplePlace().getName()));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(placeCenter).zoom(16).tilt(30).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
