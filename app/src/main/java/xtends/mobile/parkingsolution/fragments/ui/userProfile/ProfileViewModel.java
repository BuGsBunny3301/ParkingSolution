package xtends.mobile.parkingsolution.fragments.ui.userProfile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xtends.mobile.parkingsolution.models.User;
import xtends.mobile.parkingsolution.room.DbRepository;

public class ProfileViewModel extends AndroidViewModel {

    private DbRepository dbRepository;


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.dbRepository = new DbRepository(application);
    }

    public LiveData<List<User>> getAllUsers() {
        return dbRepository.getAllUsers();
    }

    public void updateUser(User user) {
        dbRepository.updateUser(user);
    }
}
