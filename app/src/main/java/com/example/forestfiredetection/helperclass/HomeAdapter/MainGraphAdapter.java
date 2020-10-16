package com.example.forestfiredetection.helperclass.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forestfiredetection.R;

import java.util.ArrayList;

public class MainGraphAdapter extends RecyclerView.Adapter<MainGraphAdapter.GraphViewHolder> {

    // gets the array list of locations of all the assets of each card view in packets or groups
    ArrayList<GraphHelperClass> graphLocations;


    // the constructor
    public MainGraphAdapter(ArrayList<GraphHelperClass> graphLocations) {
        this.graphLocations = graphLocations;
    }



    @NonNull
    @Override
    public GraphViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graphical_card_design, parent, false);

        return new GraphViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphViewHolder holder, int position) {

        GraphHelperClass graphHelperClass = graphLocations.get(position);

        holder.image.setImageResource(graphHelperClass.getImage());
        holder.title.setText(graphHelperClass.getTitle());
        holder.description.setText(graphHelperClass.getDescription());

    }

    @Override
    public int getItemCount() {
        return graphLocations.size();
    }




    // holds the view or the design
    public static class GraphViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, description;

        // the constructor
        public GraphViewHolder(@NonNull View itemView) {
            super(itemView);

            // hooks of the various assets in the card view
            image = itemView.findViewById(R.id.graph_image);
            title = itemView.findViewById(R.id.graph_title);
            description = itemView.findViewById(R.id.graph_desc);

        }

    }

}
