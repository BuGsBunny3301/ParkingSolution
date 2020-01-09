package xtends.mobile.parkingsolution.activities.spotsActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.models.Spot;

public class AllSpotsAdapter extends RecyclerView.Adapter<AllSpotsAdapter.ViewHolder> {

    private Context context;
    private List<Spot> spots;
    private List<Integer> images;
    private List<String> drawables;
    private Random rand;

    public AllSpotsAdapter(Context context, List<Spot> spots) {
        this.context = context;
        this.spots = spots;
        images = new ArrayList<>();
        images.add(R.drawable.park1);
        images.add(R.drawable.park2);
        images.add(R.drawable.park3);
        images.add(R.drawable.park8);
        images.add(R.drawable.park5);
        rand = new Random();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_spots_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spot spot = spots.get(position);

        holder.spotImage.setImageResource(images.get(rand.nextInt(images.size() - 1)));
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView spotImage;

        ViewHolder(@NonNull View view) {
            super(view);
            spotImage = view.findViewById(R.id.location_image);
        }
    }
}
