package xtends.mobile.parkingsolution.fragments.ui.allSpotsFragment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.room.DbRepository;

public class AllSpotsViewModel extends AndroidViewModel {

    private final DbRepository dbRepository;

    public AllSpotsViewModel(Application application) {
        super(application);
        this.dbRepository = new DbRepository(application);
    }

    LiveData<List<Spot>> getAllSpots() {
        return dbRepository.getAllSpots();
    }
}
