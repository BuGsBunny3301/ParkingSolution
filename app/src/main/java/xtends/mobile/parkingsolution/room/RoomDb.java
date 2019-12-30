package xtends.mobile.parkingsolution.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import xtends.mobile.parkingsolution.models.Spot;


@Database(entities = {
        Spot.class
}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class RoomDb extends RoomDatabase{

    private static volatile RoomDb INSTANCE;

    public static RoomDb getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (RoomDb.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDb.class, "app_db")
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DbCalls dbCalls();

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DbCalls dbCalls;

        PopulateDbAsyncTask(RoomDb roomDb) {
            this.dbCalls = roomDb.dbCalls();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //String name, double lat, double lng

            dbCalls.insertLocation(new Spot(
                    0,
                    "ParkHere",
                    34.822,
                    35.099,
                    ""
            ));

            return null;
        }
    }

}

