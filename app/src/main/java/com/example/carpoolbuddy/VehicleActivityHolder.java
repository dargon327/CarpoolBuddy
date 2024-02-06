package com.example.carpoolbuddy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class VehicleActivityHolder extends RecyclerView.ViewHolder
{

        protected TextView ModelText;
        protected TextView CapacityText;

        public VehicleActivityHolder(@NonNull View itemView)
        {
            super(itemView);

            // this is Activity Holder of the Name and Status fields of recycler view
            ModelText = itemView.findViewById(R.id.vecRecTextModel);
            CapacityText = itemView.findViewById(R.id.vecRecTextCapacity);
        }

}
