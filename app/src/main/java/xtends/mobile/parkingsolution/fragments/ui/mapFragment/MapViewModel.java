package xtends.mobile.parkingsolution.fragments.ui.mapFragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.room.DbRepository;

public class MapViewModel extends AndroidViewModel {
    private final DbRepository dbRepository;

    public MapViewModel(@NonNull Application application) {
        super(application);
        dbRepository = new DbRepository(application);
    }

    LiveData<List<Spot>> getAllLocations() {
        return dbRepository.getAllSpots();
    }
}
