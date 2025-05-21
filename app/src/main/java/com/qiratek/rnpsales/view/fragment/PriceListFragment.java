package com.qiratek.rnpsales.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.FragmentPriceListBinding;
import com.qiratek.rnpsales.model.entity.Brand;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.view.activity.DetailPriceListActivity;
import com.qiratek.rnpsales.view.adapter.AdapterCallback;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.PriceListViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceListFragment extends BaseFragment {

    PriceListViewModel viewModel;
    ArrayList<Brand> brands;
    GenericRecyclerAdapter adapter;
    FragmentPriceListBinding binding;

    public PriceListFragment() {
        // Required empty public constructor
    }

    public static PriceListFragment newInstance() {
        PriceListFragment fragment = new PriceListFragment();
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
                    Intent intent = new Intent(getContext(), DetailPriceListActivity.class);
                    intent.putExtra("data", (Brand) object);
                    startActivity(intent);
                };
            }
        });
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(PriceListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPriceListBinding.inflate(getLayoutInflater(), container, false);
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