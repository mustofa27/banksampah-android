package com.qiratek.rnpsales.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.entity.PriceList;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class BannerPriceListAdapter extends SliderViewAdapter<SliderAdapterVH> {

    private Context context;
    private List<PriceList> mSliderItems;

    public BannerPriceListAdapter(Context context, List<PriceList> promos) {
        this.context = context;
        mSliderItems = promos;
    }

    public void renewItems(List<PriceList> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(PriceList sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        PriceList sliderItem = mSliderItems.get(position);
        Glide.with(context).load(sliderItem.getPath_image()).
                placeholder(R.drawable.no_banner_image).error(R.drawable.no_banner_image).into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

}
