package AppView;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import AppModel.Entity.SimplePlace;
import AppModel.Entity.User;
import AppUtils.DataConverters;
import AppUtils.InjectorUtils;
import AppUtils.InputValidator;
import AppViewModel.CreateActivityViewModel;
import AppViewModel.CustomViewModelFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class CreateActivityFragment extends Fragment {

    private FragmentCreateActivityBinding mFragmentCreateActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private CreateActivityViewModel mCreateActivityViewModel;
    private Calendar date;
    private Uri capturedImageUri;
    private String selectedImagePath;
    private Bitmap bitmap;
    private ExifInterface exifObject;
    private static final int TAKE_PICTURE = 100;
    private static final int REQUEST_READ_PERMISSION = 100;
    private static final int REQUEST_WRITE_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_CHOOSE_IMAGE = 1;
    private final int REQUEST_READ_EXTERNAL_STORAGE = 111;
//    private final String MIN_MEMBERS_HINT = "Minimum Members";
//    private final String MAX_MEMBERS_HINT = "Maximum Members";
    private final String NO_LIMIT_OPTION = "100+";
    private final String TAG = "CreateActivityFragment";

    public CreateActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        date = Calendar.getInstance();

        mFragmentCreateActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_activity, container, false);

        viewModelFactory = InjectorUtils.provideViewModelFactory(getActivity());
        mCreateActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CreateActivityViewModel.class);

        bindData();
        passUser();
        initializeUI();

        return mFragmentCreateActivityBinding.getRoot();
    }

    private void initializeUI() {
        initializeRangeBar();
        initializeLocation();
        initializeSpinners();
        mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
    }

    private void initializeSpinners() {
        LiveData<List<String>> categories = mCreateActivityViewModel.getCategories();
        categories.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories.getValue());
                mFragmentCreateActivityBinding.categorySpinner.setAdapter(categoryAdapter);
                mFragmentCreateActivityBinding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String categoryName = parent.getItemAtPosition(position).toString();
                        mCreateActivityViewModel.getCategoryIdByName(categoryName).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String categoryId) {
                                LiveData<List<String>> types = mCreateActivityViewModel.getTypeNamesByCategory(categoryId);
                                types.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                                    @Override
                                    public void onChanged(List<String> strings) {
                                        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, types.getValue());
                                        mFragmentCreateActivityBinding.activityTypesSpinner.setAdapter(typesAdapter);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void initializeLocation() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(UserLoginFragment.placeFields);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//                mCreateActivityViewModel.getActivity().getValue().setActivityLocation(place);
                mCreateActivityViewModel.getActivity().getValue().setSimplePlace(DataConverters.placeToSimplePlace(place));
                validation("", 0,0,0);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void initializeRangeBar() {
        mFragmentCreateActivityBinding.membersSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mFragmentCreateActivityBinding.minMembers.setText(minValue.toString());
                mFragmentCreateActivityBinding.maxMembers.setText(maxValue.toString());
            }
        });
    }

    //Bind this fragment and viewmodel to xml using databinding
    private void bindData(){
        mFragmentCreateActivityBinding.setHandler(this);
        mFragmentCreateActivityBinding.setViewModel(mCreateActivityViewModel);
        mFragmentCreateActivityBinding.setLifecycleOwner(this);
    }

    private void passUser(){
        CreateActivityFragmentArgs args = CreateActivityFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        mCreateActivityViewModel.setCurrUser(user);
    }

    //Create activity button clicked
    public void addActivityBtn(View view)
    {
        //Insert new activity using repository with a method of the ViewModel
        getSpinnersData();
        setActivityDatesTimes();
        setActivityMembersRange();
        mCreateActivityViewModel.insertActivity(view);
    }

    private void getSpinnersData(){
        mCreateActivityViewModel.getActivity().getValue()
                .setActivityCategory(mFragmentCreateActivityBinding.categorySpinner.getSelectedItem().toString());
        mCreateActivityViewModel.getActivity().getValue()
                .setActivityType(mFragmentCreateActivityBinding.activityTypesSpinner.getSelectedItem().toString());
    }

    private void setActivityMembersRange() {
        mCreateActivityViewModel.getActivity().getValue().setMinMembers(mFragmentCreateActivityBinding.
                membersSeekbar.getSelectedMinValue().intValue());
        mCreateActivityViewModel.getActivity().getValue().setMaxMembers(mFragmentCreateActivityBinding.
                membersSeekbar.getSelectedMaxValue().intValue());
    }

    //TODO replace place with simplePlace
    public void validation(CharSequence s, int start, int before, int count){
        if(InputValidator.isValidField(mFragmentCreateActivityBinding.inputActNameLayout)
                & InputValidator.isValidField(mFragmentCreateActivityBinding.inputActInfoLayout)
                & InputValidator.datesRangeValid(mFragmentCreateActivityBinding.inputStartDateLayout,
                mFragmentCreateActivityBinding.inputStartTimeLayout, mFragmentCreateActivityBinding.inputEndDateLayout,
                mFragmentCreateActivityBinding.inputEndTimeLayout )
                & InputValidator.locationValid(mFragmentCreateActivityBinding.location,
                mCreateActivityViewModel.getActivity().getValue().getSimplePlace().getId() != null)
                & mCreateActivityViewModel.getActivity().getValue().getDisplayedImage() != null){ /* passes - place null or not */
            mFragmentCreateActivityBinding.addActivityBtn.setEnabled(true);
        }
        else {
            mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
        }
    }


    private void setActivityDatesTimes(){
        mCreateActivityViewModel.getActivity().getValue().setCreationTime(date.getTime());
        String startDate = mFragmentCreateActivityBinding.startDate.getText() + " " +
                mFragmentCreateActivityBinding.startTime.getText();
        String endDate = mFragmentCreateActivityBinding.endDate.getText() + " " +
                mFragmentCreateActivityBinding.endTime.getText();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date start = format.parse(startDate);
            mCreateActivityViewModel.getActivity().getValue().setActivityStartDateTime(start);
            Date end = format.parse(endDate);
            mCreateActivityViewModel.getActivity().getValue().setActivityEndDateTime(end);
            mCreateActivityViewModel.getActivity().getValue().setActivityDurationMin((int)(end.getTime() - start.getTime()) / (60 * 1000));
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    @TargetApi(24)
    public void dateBtn(View genView){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONDAY), date.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(genView.getId()==R.id.startDate){
                    mFragmentCreateActivityBinding.startDate.setText("" + String.format("%02d/%02d/%04d",dayOfMonth, month + 1, year));
                }
                if(genView.getId()==R.id.endDate){
                    mFragmentCreateActivityBinding.endDate.setText("" + String.format("%02d/%02d/%04d",dayOfMonth, month + 1, year));
                }
            }
        });
        datePickerDialog.show();
    }

    public void timeBtn(View genView){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(genView.getId()==R.id.startTime){
                    mFragmentCreateActivityBinding.startTime.setText("" + String.format("%02d:%02d",hourOfDay, minute));
                }
                if(genView.getId()==R.id.endTime){
                    mFragmentCreateActivityBinding.endTime.setText("" + String.format("%02d:%02d",hourOfDay, minute));
                }
            }}, Calendar.HOUR, Calendar.MINUTE, true);
        timePickerDialog.show();
    }

    public void selectImage(View view) {
        final CharSequence[] PHOTO_OPTIONS = getResources().getStringArray(R.array.choose_photo_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //builder.setTitle("Choose your profile picture");

        builder.setItems(PHOTO_OPTIONS, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);}
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);}
                if (PHOTO_OPTIONS[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else if (PHOTO_OPTIONS[item].equals("Choose from Gallery")) {
//                    if (ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
//                    }
//                    else{
//                        requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
//                                REQUEST_READ_EXTERNAL_STORAGE);
//                        Log.d("Login", "No permissions");
//                    }

                } else if (PHOTO_OPTIONS[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto, 1);
//                } else {
//                    return;
//                }
//                return;
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        mFragmentCreateActivityBinding.imageView.setImageBitmap(imageBitmap);
                        mCreateActivityViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(imageBitmap));
                    }

                    break;
                case REQUEST_CHOOSE_IMAGE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Log.d(TAG, "imageView set");
                                mFragmentCreateActivityBinding.imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                mCreateActivityViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(BitmapFactory.decodeFile(picturePath)));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
            validation("", 0,0,0);
        }
    }



}
