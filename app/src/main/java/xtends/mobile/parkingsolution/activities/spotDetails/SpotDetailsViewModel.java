package xtends.mobile.parkingsolution.activities.spotDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xtends.mobile.parkingsolution.models.Review;
import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.room.DbRepository;

public class SpotDetailsViewModel extends AndroidViewModel {

    private DbRepository dbRepository;

    public SpotDetailsViewModel(@NonNull Application application) {
        super(application);
        dbRepository = new DbRepository(application);
    }

    public LiveData<List<Review>> getAllReviews(int spotId) {
        return dbRepository.getAllReviews(spotId);
    }

    public void insertReview(Review review) {
        dbRepository.insertReview(review);
    }

    public void updateSpot(Spot spot) {
        dbRepository.updateSpot(spot);
    }

}
