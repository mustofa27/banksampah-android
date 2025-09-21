package com.mustofa27.banksampah.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.mustofa27.banksampah.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    View itemView;
    ImageView imageView;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        this.itemView = itemView;
    }
}
