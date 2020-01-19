package xtends.mobile.parkingsolution.fragments.ui.mapFragment;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.activities.mainMap.MapMainActivity;
import xtends.mobile.parkingsolution.activities.spotDetails.SpotDetails;
import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.utils.GpsUtils;

public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    public static final String TAG = MapMainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 1;
    private GoogleMap map;
    private MapView mapView;
    private MapViewModel viewModel;
    private Location currentLocation;
    private List<Spot> spotsList;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProvider;
    private boolean locationPermissionGranted, gpsEnabled;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermissions();

        viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
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
                        Toast.makeText(getContext(), "CallbackHit", Toast.LENGTH_SHORT).show();
                        zoomToLocation();
                        fusedLocationProvider.removeLocationUpdates(locationCallback);
                    }
                }
            }
        };


        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if(locationPermissionGranted) {
                    map.setMyLocationEnabled(true);
                    getLocation();
                }
                map.setOnInfoWindowClickListener(MapFragment.this);
            }
        });


        return view;
    }


    private void getLocation() {
        new GpsUtils(getActivity()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                gpsEnabled = isGPSEnable;
            }
        });

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProvider.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    zoomToLocation();
                } else {
                    fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            }
        });
    }

    private void updateSpotMarkers() {
        LatLng location;
        for(Spot spot: spotsList) {
            location = new LatLng(spot.getLat(), spot.getLng());
            map.addMarker(new MarkerOptions().position(location).title(spot.getName()));
        }
    }

    private void zoomToLocation() {
        if(currentLocation != null) {
            map.clear();
            float zoomLevel = 15.0f;
            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

            observeLocations();
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(), SpotDetails.class);
        for(Spot spot: spotsList)
            if(marker.getTitle().equals(spot.getName()))
                intent.putExtra("selected_spot", spot);
        startActivity(intent);
    }

    //region permissions
    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == REQUEST_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                if(map != null) {
                    map.setMyLocationEnabled(true);
                    getLocation();
                }
            }
        }
    }


    //endregion
}
