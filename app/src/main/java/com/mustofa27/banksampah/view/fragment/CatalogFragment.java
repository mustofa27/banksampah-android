package com.mustofa27.banksampah.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentCatalogBinding;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.activity.DetailCatalogActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CatalogViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;

import java.util.ArrayList;

public class CatalogFragment extends BaseFragment {

    CatalogViewModel viewModel;
    ArrayList<Brand> brands;
    GenericRecyclerAdapter adapter;
    FragmentCatalogBinding binding;


    public CatalogFragment() {
        // Required empty public constructor
    }


    public static CatalogFragment newInstance() {
        CatalogFragment fragment = new CatalogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brands = new ArrayList<>();
        adapter = new GenericRecyclerAdapter(brands, R.layout.item_brand_catalog, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Brand current = (Brand) object;
                TextView nama = view.findViewById(R.id.nama_kode);
                TextView count = view.findViewById(R.id.count);
                nama.setText(current.getName() + " - " + current.getKode());
                count.setText(current.getCount() + " buah file catalog");
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
                    Intent intent = new Intent(getContext(), DetailCatalogActivity.class);
                    intent.putExtra("data", (Brand) object);
                    startActivity(intent);
                };
            }
        });
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(CatalogViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatalogBinding.inflate(getLayoutInflater(), container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(adapter);
        binding.list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
        viewModel.getAllBrands().observe(getActivity(), brands -> {
            if(this.brands.size() > 0){
                this.brands.removeAll(this.brands);
            }
            for (Brand brand : brands) {
                if(brand.getCount() > 0){
                    this.brands.add(brand);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
}