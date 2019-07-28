package AppView;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import AppModel.Entity.User;
import AppUtils.DataConverters;
import AppUtils.InjectorUtils;
import AppUtils.InputValidator;
import AppViewModel.CreateActivityViewModel;
import AppViewModel.CustomViewModelFactory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
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
        initializeRangeBar();
        initializeLocation();
//        initializeSpinners();
        mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return mFragmentCreateActivityBinding.getRoot();
    }

//    private void initializeSpinners() {
//        List<String> categories = mCreateActivityViewModel.getCategories();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories);
//        mFragmentCreateActivityBinding.categorySpinner.setAdapter(adapter);
//    }

    private void initializeLocation() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mCreateActivityViewModel.getActivity().getValue().setActivityLocation(place);
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
        setActivityDatesTimes();
        mCreateActivityViewModel.insertActivity(view);
//        Navigation.findNavController(view).navigate(R.id.actCreateToList);
    }

//    private void initiateSpinners(){
//        String[] members = new String[101];
//        members[0] = NO_LIMIT_OPTION;
//        for(int i=2; i < 101; i++){
//            members[i-1] = Integer.toString(i);
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, members);
//        mFragmentCreateActivityBinding.spinnerMinMembers.setAdapter(adapter);
//        mFragmentCreateActivityBinding.spinnerMaxMembers.setAdapter(adapter);
//    }

    public void validation(CharSequence s, int start, int before, int count){
        if(InputValidator.isValidField(mFragmentCreateActivityBinding.inputActNameLayout)
                & InputValidator.isValidField(mFragmentCreateActivityBinding.inputActInfoLayout)
                & InputValidator.datesRangeValid(mFragmentCreateActivityBinding.inputStartDateLayout, mFragmentCreateActivityBinding.inputStartTimeLayout,
                mFragmentCreateActivityBinding.inputEndDateLayout, mFragmentCreateActivityBinding.inputEndTimeLayout)){
            mFragmentCreateActivityBinding.addActivityBtn.setEnabled(true);
        }
        else {
            mFragmentCreateActivityBinding.addActivityBtn.setEnabled(false);
        }
    }

    //TODO implement as data converters for databinding
    private void setActivityDatesTimes(){
        mCreateActivityViewModel.getActivity().getValue().setCreationTime(date.getTime());
        String startDate = mFragmentCreateActivityBinding.startDate.getText() + " " +
                mFragmentCreateActivityBinding.startTime.getText();
        String endDate = mFragmentCreateActivityBinding.endDate.getText() + " " +
                mFragmentCreateActivityBinding.endTime.getText();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date dateToInsert = format.parse(startDate);
            mCreateActivityViewModel.getActivity().getValue().setActivityStartDateTime(dateToInsert);
            dateToInsert = format.parse(endDate);
            mCreateActivityViewModel.getActivity().getValue().setActivityEndDateTime(dateToInsert);
        } catch (ParseException e){
            //TODO check form on button clicked
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

                if (PHOTO_OPTIONS[item].equals("Take Photo")) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);}
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);}
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                    StrictMode.setVmPolicy(builder.build());
//                    File filePhoto = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
//                    capturedImageUri = Uri.fromFile(filePhoto);
//                    selectedImagePath = capturedImageUri.getPath();
//                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, selectedImagePath);
//                    startActivityForResult(takePicture, 0);
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else if (PHOTO_OPTIONS[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (PHOTO_OPTIONS[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
//                        if (data != null) {
//                            Bundle extras = data.getExtras();
//                            if (extras.containsKey("data")) {
//                                bitmap = (Bitmap) extras.get("data");
//                            }
//                            else {
//                                bitmap = getBitmapFromUri();
//                            }
//                        }
//                        else {
//                            bitmap = getBitmapFromUri();
//                        }
//
//                        ExifInterface ei = null;
//                        try {
//                            ei = new ExifInterface(selectedImagePath);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_UNDEFINED);
//
//                        Log.d(TAG, "" +orientation);
//
//                        Bitmap rotatedBitmap = null;
//                        switch(orientation) {
//
//                            case ExifInterface.ORIENTATION_ROTATE_90:
//                                rotatedBitmap = rotateImage(bitmap, 90);
//                                break;
//
//                            case ExifInterface.ORIENTATION_ROTATE_180:
//                                rotatedBitmap = rotateImage(bitmap, 180);
//                                break;
//
//                            case ExifInterface.ORIENTATION_ROTATE_270:
//                                rotatedBitmap = rotateImage(bitmap, 270);
//                                break;
//
//                            case ExifInterface.ORIENTATION_NORMAL:
//                            default:
//                                rotatedBitmap = bitmap;
//                        }
//                        mFragmentCreateActivityBinding.imageView.setImageBitmap(rotatedBitmap);
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
        }
    }

//    private Bitmap getBitmapFromUri() {
//
//        getContext().getContentResolver().notifyChange(capturedImageUri, null);
//        ContentResolver cr = getContext().getContentResolver();
//        Bitmap bitmap;
//        try {
//            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, capturedImageUri);
//            return bitmap;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }

}
