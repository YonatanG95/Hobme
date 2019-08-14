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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import AppModel.Entity.User;
import AppUtils.DataConverters;
import AppUtils.InjectorUtils;
import AppUtils.InputValidator;
import AppViewModel.CreateActivityViewModel;
import AppViewModel.CustomViewModelFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Presents a form to create new activity
 */
public class CreateActivityFragment extends Fragment {

    private FragmentCreateActivityBinding mFragmentCreateActivityBinding;
    private CustomViewModelFactory viewModelFactory;
    private CreateActivityViewModel mCreateActivityViewModel;
    private Calendar date;
    private static final int REQUEST_READ_PERMISSION = 100;
    private static final int REQUEST_WRITE_PERMISSION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_CHOOSE_IMAGE = 1;
    private final String TAG = "CreateActivityFragment";


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

    /**
     * Handles UI modifications
     */
    private void initializeUI() {
        initializeRangeBar();
        initializeLocation();
        initializeSpinners();
        mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
    }

    /**
     * Initializes category and type spinners with options from DB
     * Reacts to user selection
     */
    private void initializeSpinners() {
        LiveData<List<String>> categories = mCreateActivityViewModel.getCategories();
        categories.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories.getValue());
                mFragmentCreateActivityBinding.categorySpinner.setAdapter(categoryAdapter);

                //A listener for category selection - request activity types of the specific category
                mFragmentCreateActivityBinding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String categoryName = parent.getItemAtPosition(position).toString();

                        //First, convert the category name to its ID
                        mCreateActivityViewModel.getCategoryIdByName(categoryName).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String categoryId) {

                                //Second, request activity types of chosen category
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

    /**
     * Initializes the location autocomplete. Reacts to user search and selection
     */
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

                //Converts to more simple representation of the place
                mCreateActivityViewModel.getActivity().getValue().setSimplePlace(DataConverters.placeToSimplePlace(place));

                //Validate all inputs outside of databinding context
                validation("", 0,0,0);
            }

            @Override
            public void onError(Status status) {
            }
        });
    }

    /**
     * Initialize range seekbar with user selection listener
     */
    private void initializeRangeBar() {
        mFragmentCreateActivityBinding.membersSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mFragmentCreateActivityBinding.minMembers.setText(minValue.toString());
                mFragmentCreateActivityBinding.maxMembers.setText(maxValue.toString());
            }
        });
    }

    /**
     * Set databinding parameters
     */
    private void bindData(){
        mFragmentCreateActivityBinding.setHandler(this);
        mFragmentCreateActivityBinding.setViewModel(mCreateActivityViewModel);
        mFragmentCreateActivityBinding.setLifecycleOwner(this);
    }

    /**
     * Gets the currently logged user (passed by activity list fragment)
     */
    private void passUser(){
        CreateActivityFragmentArgs args = CreateActivityFragmentArgs.fromBundle(getArguments());
        User user = args.getUser();
        mCreateActivityViewModel.setCurrUser(user);
    }

    /**
     * Handles activity creation button press - sets all activity fields which are outside of
     * databinding scope
     * @param view
     */
    public void addActivityBtn(View view)
    {
        getSpinnersData();
        setActivityDatesTimes();
        mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
        mCreateActivityViewModel.insertActivity(view);
    }

    /**
     * Gets spinner's values and sets the corresponding activity fields
     */
    private void getSpinnersData(){
        mCreateActivityViewModel.getActivity().getValue()
                .setActivityCategory(mFragmentCreateActivityBinding.categorySpinner.getSelectedItem().toString());
        mCreateActivityViewModel.getActivity().getValue()
                .setActivityType(mFragmentCreateActivityBinding.activityTypesSpinner.getSelectedItem().toString());
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

    /**
     * Gets date and time pickers values and sets the corresponding activity fields
     */
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

    /**
     * Handles date text input pressed - inflates date picker with current date
     * @param genView
     */
    @TargetApi(24)
    public void dateBtn(View genView){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONDAY), date.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Determines whether its the start or end date
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

    /**
     * Handles time text input pressed - inflates time picker
     * @param genView
     */
    public void timeBtn(View genView){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Determines whether its the start or end time
                if(genView.getId()==R.id.startTime){
                    mFragmentCreateActivityBinding.startTime.setText("" + String.format("%02d:%02d",hourOfDay, minute));
                }
                if(genView.getId()==R.id.endTime){
                    mFragmentCreateActivityBinding.endTime.setText("" + String.format("%02d:%02d",hourOfDay, minute));
                }
            }}, Calendar.HOUR, Calendar.MINUTE, true);
        timePickerDialog.show();
    }

    /**
     * Handles image selection button
     * @param view
     */
    public void selectImage(View view) {
        final CharSequence[] photoOptions = getResources().getStringArray(R.array.choose_photo_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(photoOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //Ask for permissions if don't exist
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);}
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);}

                //Takes photo - start camera intent
                if (photoOptions[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                //Chooses file from gallery - external storage intent
                } else if (photoOptions[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                //Cancel - exit menu
                } else if (photoOptions[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * Handles the intents result for choosing or taking photo
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                //Capture image result - get response data
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        mFragmentCreateActivityBinding.imageView.setImageBitmap(imageBitmap);
                        mCreateActivityViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(imageBitmap));
                    }
                    break;

                //Choose image result - get picture from path
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
                                mFragmentCreateActivityBinding.imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                mCreateActivityViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(BitmapFactory.decodeFile(picturePath)));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
            //Validate all inputs outside of databinding context
            validation("", 0,0,0);
        }
    }



}
