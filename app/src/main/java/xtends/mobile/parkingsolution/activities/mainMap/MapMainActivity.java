package xtends.mobile.parkingsolution.activities.mainMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.utils.GpsUtils;
import xtends.mobile.parkingsolution.R;

public class MapMainActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String TAG = MapMainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 1;
    private GoogleMap map;
    private MapActivityViewModel viewModel;
    private Location currentLocation;
    private List<Spot> spotsList;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProvider;
    private boolean locationPermissionGranted, gpsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getPermissions();

        viewModel = ViewModelProviders.of(MapMainActivity.this).get(MapActivityViewModel.class);
        spotsList = new ArrayList<>();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); //10 seconds
        locationRequest.setFastestInterval(5 * 1000); //5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation = location;
                        Toast.makeText(MapMainActivity.this, "CallbackHit", Toast.LENGTH_SHORT).show();
                        zoomToLocation();
                        fusedLocationProvider.removeLocationUpdates(locationCallback);
                    }
                }
            }
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getLocation() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                gpsEnabled = isGPSEnable;
            }
        });

        fusedLocationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    Toast.makeText(MapMainActivity.this, "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    zoomToLocation();
                } else {
                    fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocation();
    }

    private void zoomToLocation() {
        if(currentLocation != null) {
            map.clear();
            float zoomLevel = 10.0f;
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.addMarker(
                    new MarkerOptions()
                            .position(location)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_user_location_64))
                            .title("Your Location")
            );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

            observeLocations();
        }
    }

    private void updateSpotMarkers() {
        LatLng location;
        for(Spot spot: spotsList) {
            location = new LatLng(spot.getLat(), spot.getLng());
            map.addMarker(new MarkerOptions().position(location).title(spot.getName()));
        }
    }

    private void observeLocations() {
        viewModel.getAllLocations().observe(this, new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                spotsList.clear();
                spotsList.addAll(spots);
                updateSpotMarkers();
            }
        });
    }

    //region permissions
    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == REQUEST_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }
    //endregion

}
