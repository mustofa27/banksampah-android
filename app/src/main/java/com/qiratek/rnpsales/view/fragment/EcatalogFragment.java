package com.qiratek.rnpsales.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.FragmentEcatalogBinding;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.view.adapter.CustomFragmentPagerAdapter;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;


import java.util.ArrayList;

public class EcatalogFragment extends BaseFragment {

    FragmentEcatalogBinding binding;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<String> titles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(CatalogFragment.newInstance());
        fragmentArrayList.add(PriceListFragment.newInstance());
        titles = new ArrayList<>();
        titles.add(getString(R.string.product_catalog));
        titles.add(getString(R.string.pricelist));
        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getChildFragmentManager(), getLifecycle(), fragmentArrayList, titles,
                position -> {});
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEcatalogBinding.inflate(getLayoutInflater());
        binding.pager.setAdapter(customFragmentPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> tab.setText(customFragmentPagerAdapter.getTitle(position))).attach();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void showLoading(boolean isLoading) {

    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {

    }
}