package com.mustofa27.banksampah.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.tabs.TabLayoutMediator;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FormPersonalizeBinding;
import com.mustofa27.banksampah.databinding.FragmentHistoryBinding;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.activity.CartActivity;
import com.mustofa27.banksampah.view.adapter.CustomFragmentPagerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistoryViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    FragmentHistoryBinding binding;
    HistoryViewModel viewModel;
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<String> titles;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(HistoryViewModel.class);
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(HistorySavingFragment.newInstance());
        fragmentArrayList.add(HistoryTransactionFragment.newInstance());
        fragmentArrayList.add(HistoryWithdrawFragment.newInstance());
        titles = new ArrayList<>();
        titles.add(getContext().getString(R.string.riwayat_tab_title_1));
        titles.add(getContext().getString(R.string.riwayat_tab_title_2));
        titles.add(getContext().getString(R.string.riwayat_tab_title_3));
        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(this, fragmentArrayList, titles, null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        binding.title.setText(getContext().getText(R.string.history));
        binding.subtitle.setText(getContext().getText(R.string.riwayat_subtitle));
        //binding.cartContainer.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));
        binding.pager.setAdapter(customFragmentPagerAdapter);
        binding.pager.setUserInputEnabled(false);
        binding.tab1.setOnClickListener(this);
        binding.tab2.setOnClickListener(this);
        binding.tab3.setOnClickListener(this);
        binding.tab1.setActivated(true);
        binding.cartContainer.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if(isLoading)
            showLoadingDialog();
        else
            dismissLoadingDialog();
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            viewModel.getLoading().setValue(false);
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), this::showLoading);
        viewModel.getCart().observe(getActivity(), cartItems -> {
            binding.indicator.setText("" + cartItems.size());
            binding.indicator.setVisibility(cartItems.size() > 0 ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_1:
                binding.tab1.setActivated(true);
                binding.tab2.setActivated(false);
                binding.tab3.setActivated(false);
                binding.pager.setCurrentItem(0);
                break;
            case R.id.tab_2:
                binding.tab2.setActivated(true);
                binding.tab1.setActivated(false);
                binding.tab3.setActivated(false);
                binding.pager.setCurrentItem(1);
                break;
            case R.id.tab_3:
                binding.tab3.setActivated(true);
                binding.tab1.setActivated(false);
                binding.tab2.setActivated(false);
                binding.pager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCart();
    }
}