package xtends.mobile.parkingsolution.activities.spotDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.models.Review;
import xtends.mobile.parkingsolution.models.Spot;

public class SpotDetails extends AppCompatActivity {

    private SpotDetailsViewModel viewModel;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ReviewsListAdapter adapter;
    private List<Review> reviewList;
    private Spot spot;
    private TextView rateSpot, ratingText, priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_details);

        Intent intent = getIntent();
        spot = (Spot) intent.getSerializableExtra("selected_spot");

        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(spot != null)
            getSupportActionBar().setTitle(spot.getName());

        viewModel = ViewModelProviders.of(this).get(SpotDetailsViewModel.class);



        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.reviews_recycler_view);
        rateSpot = findViewById(R.id.rate_spot_text);
        ratingText = findViewById(R.id.rating_text_view);
        priceText = findViewById(R.id.price_text_view);

        ratingText.setText("Rating: " + spot.getRating());
        priceText.setText("Price: " + spot.getPricePerHour() + "$/h");
        rateSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        reviewList = new ArrayList<>();

        adapter = new ReviewsListAdapter(this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        observeReviews();
    }

    private void observeReviews() {
        viewModel.getAllReviews(spot.getId()).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewList.clear();
                reviewList.addAll(reviews);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.spot_details_rate_dialog, null);
        builder.setView(view);

        final MaterialRatingBar ratingBar = view.findViewById(R.id.rating_bar);
        final EditText reviewText = view.findViewById(R.id.review_edit_text);
        Button done = view.findViewById(R.id.done_button);

        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review review = new Review(
                        reviewText.getText().toString(),
                        ratingBar.getRating(),
                        spot.getId()
                );
                if(reviewList.size() > 0) {
                    spot.setRating((spot.getRating() + ratingBar.getRating()) / 2);
                    ratingText.setText("Rating: " + (spot.getRating() + ratingBar.getRating()) / 2);
                } else {
                    spot.setRating(ratingBar.getRating());
                    ratingText.setText("Rating: " + ratingBar.getRating());
                }
                viewModel.updateSpot(spot);
                viewModel.insertReview(review);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}
