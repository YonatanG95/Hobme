package AppUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.firebase.firestore.Blob;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import androidx.room.TypeConverter;

public class DataConverters {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static Blob drawableToBlob(Drawable drawable){
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArr = stream.toByteArray();
        return Blob.fromBytes(byteArr);
    }

    public static Bitmap blobToBitmap(Blob blob){
        byte[] bytes = blob.toBytes();
        return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
    }

//    public static Bitmap rotateImageIfNeeded(Bitmap bitmap, String path) throws IOException {
//        ExifInterface ei = new ExifInterface(path);
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_UNDEFINED);
//
//        Bitmap rotatedBitmap = null;
//        switch(orientation) {
//
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(bitmap, 90);
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(bitmap, 180);
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(bitmap, 270);
//
//            case ExifInterface.ORIENTATION_NORMAL:
//            default:
//                return bitmap;
//        }
//    }
//
//    private static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }

//    public static String getRealPathFromURIPath(Uri contentURI, Context context) {
//        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            return cursor.getString(idx);
//        }
//    }
//
//    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
//        Matrix matrix = new Matrix();
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_NORMAL:
//                return bitmap;
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                matrix.setScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                matrix.setRotate(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                matrix.setRotate(180);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_TRANSPOSE:
//                matrix.setRotate(90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                matrix.setRotate(90);
//                break;
//            case ExifInterface.ORIENTATION_TRANSVERSE:
//                matrix.setRotate(-90);
//                matrix.postScale(-1, 1);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                matrix.setRotate(-90);
//                break;
//            default:
//                return bitmap;
//        }
//        try {
//            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            bitmap.recycle();
//            return bmRotated;
//        }
//        catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static Bitmap getBitmapFromUri(Uri imageUri,Context context ) {

        context.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = context.getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
