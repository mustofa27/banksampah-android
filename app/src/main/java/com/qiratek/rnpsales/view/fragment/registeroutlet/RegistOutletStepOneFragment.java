package com.qiratek.rnpsales.view.fragment.registeroutlet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.FragmentRegistOutletStepOneBinding;
import com.qiratek.rnpsales.model.entity.City;
import com.qiratek.rnpsales.model.entity.Distributor;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.RegisterOutletViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistOutletStepOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistOutletStepOneFragment extends BaseFragment {

    RegisterOutletViewModel viewModel;
    FragmentRegistOutletStepOneBinding binding;
    ArrayList<City> cities;
    ArrayList<Distributor> distributors;
    Map<String, Object > param;
    City selectedCity;
    Distributor selectedDist;
    ArrayAdapter cityAdapter;
    ArrayAdapter distAdapter;

    public RegistOutletStepOneFragment() {
        // Required empty public constructor
    }


    public static RegistOutletStepOneFragment newInstance() {
        RegistOutletStepOneFragment fragment = new RegistOutletStepOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new CustomViewModelFactory(getContext())).get(RegisterOutletViewModel.class);
        param = new HashMap<>();
        cities = new ArrayList<>();
        distributors = new ArrayList<>();
        cityAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, cities);
        distAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner, distributors);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegistOutletStepOneBinding.inflate(getLayoutInflater());
        binding.roS1Next.setOnClickListener(v -> {
            if(selectedCity == null || selectedDist == null || !isStringNotEmpty(binding.kodeOutlet) ||
                    !isStringNotEmpty(binding.roAddress) || !isStringNotEmpty(binding.roOutletname)){
                showMessageFailed("Pastikan semua sudah terisi");
            } else {
                param.put("distributor_id",selectedDist.getId());
                param.put("kota_id", selectedCity.getId());
                param.put("nama",binding.roOutletname.getText().toString());
                param.put("alamat",binding.roAddress.getText().toString());
                param.put("kode",binding.kodeOutlet.getText().toString());
                param.put("rank","A");
                param.put("take_order","0");
                viewModel.setParam(param);
                viewModel.getNavController().navigate(R.id.action_step_one_to_step_two);
            }
        });
        binding.roCity.setAdapter(cityAdapter);
        binding.roDistributor.setAdapter(distAdapter);
        binding.roCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = (City)parent.getItemAtPosition(position);
            }
        });
        binding.roDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDist = distributors.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDist = null;
            }
        });
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getActivity(), status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });
        viewModel.getAllCity().observe(getActivity(), cities -> {
            if(this.cities.size() > 0){
                this.cities.removeAll(this.cities);
            }
            this.cities.addAll(cities);
            cityAdapter.notifyDataSetChanged();
            viewModel.getAllDist().observe(getActivity(), distributors -> {
                if(this.distributors.size() > 0){
                    this.distributors.removeAll(this.distributors);
                }
                this.distributors.addAll(distributors);
                distAdapter.notifyDataSetChanged();
            });
        });
    }
}