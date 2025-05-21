package com.qiratek.rnpsales.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class CustomFragmentPagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<String> titles;
    FragmentAdapterCallback adapterCallback;

    public CustomFragmentPagerAdapter(@NonNull Fragment fragment, ArrayList<Fragment> fragments, ArrayList<String> titles, FragmentAdapterCallback adapterCallback) {
        super(fragment);
        this.fragmentArrayList = fragments;
        this.titles = titles;
        this.adapterCallback = adapterCallback;
    }

    public CustomFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle, ArrayList<Fragment> fragments, ArrayList<String> titles, FragmentAdapterCallback adapterCallback) {
        super(fragmentManager, lifecycle);
        this.fragmentArrayList = fragments;
        this.titles = titles;
        this.adapterCallback = adapterCallback;
    }

    public String getTitle(int position){
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        adapterCallback.onSelectFragment(position);
        return super.getItemId(position);
    }
}
