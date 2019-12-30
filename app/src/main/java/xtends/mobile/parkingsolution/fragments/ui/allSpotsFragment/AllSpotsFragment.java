package xtends.mobile.parkingsolution.fragments.ui.allSpotsFragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.activities.spotsActivity.AllSpotsAdapter;
import xtends.mobile.parkingsolution.fragments.ui.mapFragment.MapViewModel;
import xtends.mobile.parkingsolution.models.Spot;

public class AllSpotsFragment extends Fragment {

    private AllSpotsViewModel viewModel;
    private RecyclerView recyclerView;
    private List<Spot> allSpots;
    private AllSpotsAdapter adapter;

    public static AllSpotsFragment newInstance() {
        return new AllSpotsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_spots_fragment, container, false);
        viewModel = ViewModelProviders.of(this).get(AllSpotsViewModel.class);
        initRecycler(view);
        observeSpots();
        return view;
    }

    private void initRecycler(View view) {
        allSpots = new ArrayList<>();
        adapter = new AllSpotsAdapter(getActivity(), allSpots);
        recyclerView = view.findViewById(R.id.all_spots_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void observeSpots() {
        viewModel.getAllSpots().observe(getActivity(), new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                allSpots.clear();
                allSpots.addAll(spots);
                adapter.notifyDataSetChanged();
            }
        });
    }


}
