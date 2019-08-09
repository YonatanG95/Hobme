package AppModel.Entity;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

public class SimplePlace {

    private String name;
    @PrimaryKey
    @NonNull
    private String id;
    private Double latitude;
    private Double longitude;

    public SimplePlace() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
