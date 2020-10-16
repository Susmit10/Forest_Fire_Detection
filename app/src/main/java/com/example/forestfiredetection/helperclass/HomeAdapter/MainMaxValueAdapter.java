package com.example.forestfiredetection.helperclass.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forestfiredetection.R;

import java.util.ArrayList;

public class MainMaxValueAdapter extends RecyclerView.Adapter<MainMaxValueAdapter.MaxValueViewHolder> {

    ArrayList<MaxValueHelperClass> maxValueLocations;

    public MainMaxValueAdapter(ArrayList<MaxValueHelperClass> maxValueLocations) {
        this.maxValueLocations = maxValueLocations;
    }




    @NonNull
    @Override
    public MaxValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.max_value_card_design, parent, false);

        return new MaxValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaxValueViewHolder holder, int position) {

        MaxValueHelperClass Locations = maxValueLocations.get(position);

        holder.image.setImageResource(Locations.getImage());
        holder.title.setText(Locations.getHeading());
        holder.description.setText(Locations.getValue());
        holder.layout.setBackground(Locations.getGradientDrawable());
    }

    @Override
    public int getItemCount() {
        return maxValueLocations.size();
    }




    public static class MaxValueViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;
        LinearLayout layout;

        public MaxValueViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.max_image);
            title = itemView.findViewById(R.id.max_Heading);
            description = itemView.findViewById(R.id.max_values);
            layout = itemView.findViewById(R.id.maxValueCard);

        }

    }
}
