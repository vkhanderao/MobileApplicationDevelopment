package com.akshay.knowyourgovernment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class electedPersonViewHolder extends RecyclerView.ViewHolder {
    TextView designation;
    TextView name;
    ImageView separatorImage;
    public electedPersonViewHolder(@NonNull View itemView) {
        super(itemView);
        designation=itemView.findViewById(R.id.epdesignation);
        name=itemView.findViewById(R.id.epelectedofficial);
        separatorImage =itemView.findViewById(R.id.epimageView );
    }
}