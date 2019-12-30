package xtends.mobile.parkingsolution.activities.mainMap;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.room.DbRepository;

public class MapActivityViewModel extends AndroidViewModel {

    private final DbRepository dbRepository;

    public MapActivityViewModel(@NonNull Application application) {
        super(application);
        dbRepository = new DbRepository(application);
    }

    LiveData<List<Spot>> getAllLocations() {
        return dbRepository.getAllSpots();
    }
}
