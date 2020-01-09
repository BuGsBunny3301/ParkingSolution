package xtends.mobile.parkingsolution.fragments.ui.userProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.models.User;

public class ProfileFragment extends Fragment {

    private View view;
    private ProfileViewModel viewModel;
    private Toolbar toolbar;
    private EditText firstName, lastName, email, phoneNumber;
    private String menuString = "editOff";

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        this.view = view;
        initViews();

        return view;
    }

    private void initViews() {
        firstName = view.findViewById(R.id.first_name_value);
        lastName = view.findViewById(R.id.family_name_value);
        email = view.findViewById(R.id.email_value);
        phoneNumber = view.findViewById(R.id.mobile_value);

        observeUsers();
        disableViews();
    }

    private void observeUsers() {
        viewModel.getAllUsers().observe(Objects.requireNonNull(getActivity()), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                User user = users.get(0);
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                phoneNumber.setText(String.valueOf(user.getPhoneNumber()));
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        if(menuString.equals("editOff"))
            inflater.inflate(R.menu.profile_fragment_menu, menu);
        else if(menuString.equals("editOn"))
            inflater.inflate(R.menu.profile_fragment_confirm_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_edit) {
            menuString = "editOn";
            enableViews();
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        } else if(item.getItemId() == R.id.action_confirm) {
            updateChanges();
            menuString = "editOff";
            disableViews();
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        } else if(item.getItemId() == R.id.action_cancel) {
            menuString = "editOff";
            disableViews();
            observeUsers();
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateChanges() {
        try{
            viewModel.updateUser(new User(
                    0,
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    Integer.valueOf(phoneNumber.getText().toString())
            ));
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Please check input", Toast.LENGTH_SHORT).show();
        }

    }

    private void enableViews() {
        firstName.setEnabled(true);
        lastName.setEnabled(true);
        email.setEnabled(true);
        phoneNumber.setEnabled(true);
    }

    private void disableViews() {
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        phoneNumber.setEnabled(false);
    }
}
