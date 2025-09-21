package com.mustofa27.banksampah.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.view.activity.NewsDetail;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class BannerPromoAdapter extends SliderViewAdapter<SliderAdapterVH> {

    private Context context;
    private List<NewsClass> mSliderItems;

    public BannerPromoAdapter(Context context, List<NewsClass> promos) {
        this.context = context;
        mSliderItems = promos;
    }

    public void renewItems(List<NewsClass> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(NewsClass sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promos, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        NewsClass sliderItem = mSliderItems.get(position);
        Glide.with(context).load(sliderItem.getImage()).
                placeholder(R.drawable.no_banner_image).error(R.drawable.no_banner_image).into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetail.class);
                intent.putExtra("data", sliderItem);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

}
