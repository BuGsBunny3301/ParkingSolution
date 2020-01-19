package xtends.mobile.parkingsolution.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import xtends.mobile.parkingsolution.models.Review;
import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.models.User;

public class DbRepository {

    private DbCalls dbCalls;
    public static final String TAG = DbRepository.class.getSimpleName();

    public DbRepository(Application application) {
        RoomDb db = RoomDb.getInstance(application);
        dbCalls = db.dbCalls();
    }

    //region locationCalls
    public LiveData<List<Spot>> getAllSpots() {
        return dbCalls.getAllSpots();
    }

    public void insertLocation(Spot spot) {
        new InsertAsyncTask(dbCalls).execute(spot);
    }

    public void updateSpot(Spot spot) {
        new UpdateAsyncTask(dbCalls).execute(spot);
    }
    //endregion

    //region user
    public LiveData<List<User>> getAllUsers() {
        return dbCalls.getAllUsers();
    }

    public void insertUser(User user) {
        new InsertAsyncTask(dbCalls).execute(user);
    }

    public void updateUser(User user) {
        new UpdateAsyncTask(dbCalls).execute(user);
    }
    //endregion

    //region review
    public void insertReview(Review review){
        new InsertAsyncTask(dbCalls).execute(review);
    }

    public LiveData<List<Review>> getAllReviews(int spotId) {
        return dbCalls.getAllReviews(spotId);
    }
    //endregion

    //region asyncTasks
    private static class InsertAsyncTask extends AsyncTask<Object, Void, Void> {
        private DbCalls dbCalls;

        InsertAsyncTask(DbCalls dbCalls){
            this.dbCalls = dbCalls;
        }

        //TODO: make this accept any type and just get called
        @Override
        protected Void doInBackground(Object... objects) {
            if(objects[0] instanceof Spot)
                dbCalls.insertLocation((Spot) objects[0]);
            if(objects[0] instanceof User)
                dbCalls.insertUser((User) objects[0]);
            if(objects[0] instanceof Review)
                dbCalls.insertReview((Review) objects[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Object, Void, Void> {
        private DbCalls dbCalls;

        UpdateAsyncTask(DbCalls dbCalls){
            this.dbCalls = dbCalls;
        }

        //TODO: make this accept any type and just get called
        @Override
        protected Void doInBackground(Object... objects) {
            if(objects[0] instanceof User)
                dbCalls.updateUser((User) objects[0]);
            else if(objects[0] instanceof Spot)
                dbCalls.updateSpot((Spot) objects[0]);
            return null;
        }
    }
    //endregion
}
