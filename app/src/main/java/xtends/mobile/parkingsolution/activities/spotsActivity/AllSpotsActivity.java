package xtends.mobile.parkingsolution.activities.spotsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.models.Spot;

public class AllSpotsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<Spot> allSpots;
    private AllSpotsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_spots);

        initRecycler();
    }

    private void initRecycler() {
        allSpots = new ArrayList<>();



        recyclerView = findViewById(R.id.all_spots_recycler);
        adapter = new AllSpotsAdapter(this, allSpots);
    }
}
