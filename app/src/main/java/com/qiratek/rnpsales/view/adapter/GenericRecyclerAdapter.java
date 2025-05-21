package com.qiratek.rnpsales.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenericRecyclerAdapter extends RecyclerView.Adapter<GenericViewHolder> implements Filterable {

    private ArrayList data;
    private int layoutId;
    private AdapterCallback adapterCallback;
    private Filter filter;

    public GenericRecyclerAdapter(ArrayList data, int layoutId, AdapterCallback adapterCallback) {
        this.data = data;
        this.layoutId = layoutId;
        this.adapterCallback = adapterCallback;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenericViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(data.get(position), adapterCallback);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
