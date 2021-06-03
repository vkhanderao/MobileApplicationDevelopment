package com.akshay.knowyourgovernment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class electedPersonListAdapter extends RecyclerView.Adapter<electedPersonViewHolder> {

    private static final String TAG="electedPersonListAdapter";
    private final MainActivity mainActivity;
    private final ArrayList<electorPersonMethod> electedPersonList;

    public electedPersonListAdapter(MainActivity mainActivity, ArrayList<electorPersonMethod> electedPersonList){
        this.mainActivity = mainActivity;
        this.electedPersonList = electedPersonList;
    }
    @NonNull
    @Override
    public electedPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "electedPersonViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elected_persons,parent,false);
        view.setOnClickListener(mainActivity);
        return new electedPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull electedPersonViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        electorPersonMethod electedperson = electedPersonList.get(position);
        holder.designation.setText(electedperson.getOffice());
        holder.name.setText(electedperson.getName()+" ("+electedperson.getParty()+") ");
    }

    @Override
    public int getItemCount() {
        return electedPersonList.size();
    }
}
