package com.example.carpoolbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by VehicleInfoActivity's Recycler View, it also contains the ViewHolder class
 * It is called by VehicleInfoActivity to display the vehicle model and capacity on RecyclerView
 * It also implements the onClick listener for user to click into a vehicle to see details
 *
 * @author addison lee
 * @version 0.0
 */
public class VehicleAdaptor extends RecyclerView.Adapter<VehicleAdaptor.VehicleActivityHolder>
{
    // defines local variable
        private ArrayList<Vehicle> allVehicles;
        private RecyclerViewClickListener listener;  // for RV click

    // constructor
        public VehicleAdaptor (ArrayList<Vehicle> myVehicles, RecyclerViewClickListener listener)
        {
            allVehicles = myVehicles;
            this.listener = listener; // for RV click
            System.out.println("*** at end of VehicleAdaptor");
        }

    /**
     * This class is Activity Holder class for RecyclerView, with OnClickListener for click
     */
    public class VehicleActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // the two fields to display on RV
            protected TextView ModelText;
            protected TextView CapacityText;

            // Activity Holder for RV with onClick listener
            public VehicleActivityHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this); // for RV click, bind the listener

                // this is Activity Holder of the Name and Status fields of recycler view
                ModelText = itemView.findViewById(R.id.vecRecTextModel);
                CapacityText = itemView.findViewById(R.id.vecRecTextCapacity);
            }

            @Override
            public void onClick(View view) {
                listener.onClick(view, getAdapterPosition());  // call the onClick in listener
            }
        }

    /**
     * This is the ViewHolder for the recycler view
     * @param parent
     * @param viewType
     * @return
     */
        @NonNull
        @Override
    public VehicleActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View myView = LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.activity_vehicle_rec_view, parent, false);
            VehicleActivityHolder holder = new VehicleActivityHolder(myView);
            return holder;
        }

    /**
     * This method pass the vehicle data to ViewHolder to display
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull VehicleActivityHolder holder, int position) {
            Vehicle v = allVehicles.get(position);
            holder.ModelText.setText("    "+v.getVehicleType()+" - "+v.getModel());
            holder.CapacityText.setText("Seats left: "+v.getCapacity().toString());
        }

        @Override
    public int getItemCount()
    {
        return allVehicles.size();
    }


    /**
     * This is the interface for OnClick listener
     */
    public interface RecyclerViewClickListener{
            void onClick(View v, int position);
    }
}
