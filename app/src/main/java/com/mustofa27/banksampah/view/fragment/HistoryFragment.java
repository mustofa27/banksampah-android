package com.mustofa27.banksampah.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.mustofa27.banksampah.view.adapter.CustomFragmentPagerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistoryViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryFragment extends BaseFragment {

    FragmentHistoryBinding binding;
    FormPersonalizeBinding formPersonalizeBinding;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<String> titles;
    HistoryViewModel viewModel;
    PopupWindow popupWindow;
    String[] isiStatus = new String[]{"Tidak", "Iya"};
    ArrayAdapter statusAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(HistoryViewModel.class);
        statusAdapter = new ArrayAdapter(getContext(), R.layout.custom_spinner, isiStatus);
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(HistoryTakeOrderFragment.newInstance());
        fragmentArrayList.add(HistoryBillingFragment.newInstance());
        titles = new ArrayList<>();
        titles.add(getString(R.string.takeOrder));
        titles.add(getString(R.string.billing));
        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getChildFragmentManager(), getLifecycle(), fragmentArrayList, titles,
                position -> binding.rightIcon.setVisibility(position == 0 ? View.VISIBLE : View.GONE));
        formPersonalizeBinding = FormPersonalizeBinding.inflate(getLayoutInflater());
        popupWindow = new PopupWindow(formPersonalizeBinding.getRoot(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
        formPersonalizeBinding.save.setOnClickListener(view -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("pilihan_tampil", formPersonalizeBinding.status.getSelectedItemPosition());
                //viewModel.setUserPersonalizeLiveData(jsonObject);
            } catch (JSONException e) {
                showMessageFailed("Json Error");
                popupWindow.dismiss();
                e.printStackTrace();
            }
        });
        formPersonalizeBinding.cancel.setOnClickListener(view -> {
            popupWindow.dismiss();
        });
        formPersonalizeBinding.status.setAdapter(statusAdapter);
        viewModel.setPopupWindow(popupWindow);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        binding.rightIcon.setOnClickListener(view -> {
//            if(viewModel.getUserPersonalize() != null){
//                formPersonalizeBinding.status.setSelection(viewModel.getUserPersonalize().getPilihan_tampil());
//            }
            popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 10, 10);
        });
        binding.pager.setAdapter(customFragmentPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> tab.setText(customFragmentPagerAdapter.getTitle(position))).attach();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
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