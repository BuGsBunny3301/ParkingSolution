package xtends.mobile.parkingsolution.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;

@Dao
public interface DbCalls {

    //region locationCalls

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Spot spot);

    @Query("SELECT * FROM Spot")
    LiveData<List<Spot>> getAllSpots();

    //endregion

}
