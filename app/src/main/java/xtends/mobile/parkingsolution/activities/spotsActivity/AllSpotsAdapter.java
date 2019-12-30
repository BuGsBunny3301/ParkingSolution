package xtends.mobile.parkingsolution.activities.spotsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xtends.mobile.parkingsolution.R;
import xtends.mobile.parkingsolution.models.Spot;

public class AllSpotsAdapter extends RecyclerView.Adapter<AllSpotsAdapter.ViewHolder> {

    private Context context;
    private List<Spot> spots;

    public AllSpotsAdapter(Context context, List<Spot> spots) {
        this.context = context;
        this.spots = spots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_spots_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
