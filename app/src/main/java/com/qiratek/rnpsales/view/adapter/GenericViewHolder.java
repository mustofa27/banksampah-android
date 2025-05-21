package com.qiratek.rnpsales.view.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenericViewHolder extends RecyclerView.ViewHolder {
    public GenericViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(Object object, AdapterCallback adapterCallback){
        adapterCallback.bindView(itemView, object);
        if(adapterCallback.onClickItem(object) != null) {
            itemView.setOnClickListener(adapterCallback.onClickItem(object));
        }
    }
}
