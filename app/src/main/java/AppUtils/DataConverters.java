package AppUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.room.TypeConverter;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.Blob;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import AppModel.Entity.SimplePlace;

/**
 * Converts objects from one type to another
 */
public class DataConverters {

    /**
     * Converts Long timestamps to date object
     * @param timestamp
     * @return Date object
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Converts Date objects to Long timestamps
     * @param date
     * @return Long representation of Date
     */
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Converts Blob objects (images) to byte array
     * @param blob
     * @return byte array representation of an image
     */
    @TypeConverter
    public static byte[] toByteArray(Blob blob) {
        return blob.toBytes();
    }

    /**
     * Converts byte arrays to Blob object (image)
     * @param bytes
     * @return Blob (image) object
     */
    @TypeConverter
    public static Blob toBlob(byte[] bytes) {
        return Blob.fromBytes(bytes);
    }

    /**
     * Converts a place (google place API) object to a more simple representation of a place
     * @param place
     * @return
     */
    public static SimplePlace placeToSimplePlace(Place place){
        SimplePlace simplePlace = new SimplePlace();
        simplePlace.setId(place.getId());
        simplePlace.setName(place.getName());
        simplePlace.setLatitude(place.getLatLng().latitude);
        simplePlace.setLongitude(place.getLatLng().longitude);
        return simplePlace;
    }

    /**
     * Converts Blob object to Bitmap object
     * @param blob
     * @return Bitmap representation of a Blob
     */
    public static Bitmap blobToBitmap(Blob blob) {
        byte[] bytes = blob.toBytes();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Converts Bitmap object to Blob object
     * @param bitmap
     * @return Blob representation of a Bitmap
     */
    public static Blob bitmapToBlob(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArr = stream.toByteArray();
        return Blob.fromBytes(byteArr);
    }

}