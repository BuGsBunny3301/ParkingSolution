package xtends.mobile.parkingsolution.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import xtends.mobile.parkingsolution.models.Spot;
import xtends.mobile.parkingsolution.models.User;

@Dao
public interface DbCalls {

    //region location
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Spot spot);

    @Query("SELECT * FROM Spot")
    LiveData<List<Spot>> getAllSpots();
    //endregion

    //region user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateUser(User user);
    //endregion

}
