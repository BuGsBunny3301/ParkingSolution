package xtends.mobile.parkingsolution.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xtends.mobile.parkingsolution.models.Review;
import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.models.User;


@Database(entities = {
        Spot.class,
        User.class,
        Review.class
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
            new PopulateDbAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DbCalls dbCalls;

        PopulateDbAsyncTask(RoomDb roomDb) {
            this.dbCalls = roomDb.dbCalls();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            //insert locations
            dbCalls.insertLocation(new Spot(
                    "abo hamza parking",
                    35.496161,
                    33.871253,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "abc xyz",
                    35.539119,
                    33.882352,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "capital park",
                    35.539098,
                    33.893342,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "Parking du liban",
                    35.592506,
                    33.941080,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "Park Park",
                    35.608213,
                    33.817271,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "abo l nar parking",
                    35.527750,
                    33.821203,
                    "",
                    3,
                    3.5
            ));


            dbCalls.insertLocation(new Spot(
                    "sheel parking",
                    35.518391,
                    33.887375,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "atyab parking",
                    35.498135,
                    33.880891,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "Malek l parking",
                    35.487321,
                    33.889452,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "Parking center",
                    35.492041,
                    33.897011,
                    "",
                    3,
                    3.5
            ));
            dbCalls.insertLocation(new Spot(
                    "I'm a parking",
                    35.516803,
                    33.878041,
                    "",
                    3,
                    3.5
            ));



            dbCalls.insertUser(new User(
                    0,
                    "Lana",
                    "Rhoades",
                    "lanaInMe@gmail.com",
                    78666666
            ));

            return null;
        }
    }

}

