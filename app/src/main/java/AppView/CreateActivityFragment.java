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
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.project.hobme.R;
import com.project.hobme.databinding.FragmentCreateActivityBinding;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import AppUtils.DataConverters;
import AppUtils.InjectorUtils;
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
    private CreateActivityViewModel mActivityListViewModel;
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
        mActivityListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CreateActivityViewModel.class);

        bindData();

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        return mFragmentCreateActivityBinding.getRoot();
    }

    //Bind this fragment and viewmodel to xml using databinding
    private void bindData(){
        mFragmentCreateActivityBinding.setHandler(this);
        mFragmentCreateActivityBinding.setViewModel(mActivityListViewModel);
        mFragmentCreateActivityBinding.setLifecycleOwner(this);
    }

    //Create activity button clicked
    public void addActivityBtn(View view)
    {
        //Insert new activity using repository with a method of the ViewModel

        setActivityDatesTimes();
        mActivityListViewModel.insertActivity(mActivityListViewModel.getActivity().getValue());

        //Move back to activities list Fragment
//        Fragment listActivities = new ActivityListFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.activities_fragment_container, listActivities );
//        transaction.addToBackStack(null);
//        transaction.commit();
        Navigation.findNavController(view).navigate(R.id.actCreateToList);
    }

    //TODO implement as data converters for databinding
    private void setActivityDatesTimes(){
        mActivityListViewModel.getActivity().getValue().setCreationTime(date.getTime());
        String startDate = mFragmentCreateActivityBinding.dateStartBtn.getText() + " " +
                mFragmentCreateActivityBinding.timeStartBtn.getText();
        String endDate = mFragmentCreateActivityBinding.dateEndBtn.getText() + " " +
                mFragmentCreateActivityBinding.timeEndBtn.getText();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date dateToInsert = format.parse(startDate);
            mActivityListViewModel.getActivity().getValue().setActivityStartDateTime(dateToInsert);
            dateToInsert = format.parse(endDate);
            mActivityListViewModel.getActivity().getValue().setActivityEndDateTime(dateToInsert);
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
                if(genView.getId()==R.id.dateStartBtn){
                    mFragmentCreateActivityBinding.dateStartBtn.setText("" + String.format("%02d/%02d/%04d",dayOfMonth, month + 1, year));
                }
                if(genView.getId()==R.id.dateEndBtn){
                    mFragmentCreateActivityBinding.dateEndBtn.setText("" + String.format("%02d/%02d/%04d",dayOfMonth, month + 1, year));
                }
            }
        });
        datePickerDialog.show();
    }

    public void timeBtn(View genView){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(genView.getId()==R.id.timeStartBtn){
                    mFragmentCreateActivityBinding.timeStartBtn.setText("" + String.format("%02d:%02d",hourOfDay, minute));
                }
                if(genView.getId()==R.id.timeEndBtn){
                    mFragmentCreateActivityBinding.timeEndBtn.setText("" + String.format("%02d:%02d",hourOfDay, minute));
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
                        mActivityListViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(imageBitmap));
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
                                mActivityListViewModel.getActivity().getValue().setDisplayedImage(DataConverters.bitmapToBlob(BitmapFactory.decodeFile(picturePath)));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private Bitmap getBitmapFromUri() {

        getContext().getContentResolver().notifyChange(capturedImageUri, null);
        ContentResolver cr = getContext().getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, capturedImageUri);
            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
