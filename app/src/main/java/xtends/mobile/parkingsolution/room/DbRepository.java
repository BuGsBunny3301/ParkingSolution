package xtends.mobile.parkingsolution.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;

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
        new DbAsyncTask(dbCalls).execute(spot);
    }

    //endregion

    //region asyncTasks

    private static class DbAsyncTask extends AsyncTask<Object, Void, Void> {
        private DbCalls dbCalls;

        DbAsyncTask(DbCalls dbCalls){
            this.dbCalls = dbCalls;
        }

        //TODO: make this accept any type and just get called
        @Override
        protected Void doInBackground(Object... objects) {
            if(objects[0] instanceof Spot)
                dbCalls.insertLocation((Spot) objects[0]);
            return null;
        }
    }

    //endregion

}
