package com.mustofa27.banksampah.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityOutletBinding;
import com.mustofa27.banksampah.model.entity.City;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Tipe;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.OutletViewModel;

import java.util.ArrayList;

public class OutletActivity extends BaseActivity {

    ActivityOutletBinding binding;
    OutletViewModel viewModel;
    ArrayList<Outlet> outlets, filteredOutlet;
    GenericRecyclerAdapter adapter;
    ArrayList<Tipe> tipes;
    ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("OUTLET LIST");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(OutletViewModel.class);
        binding.tambahOutlet.setOnClickListener(v -> {
            viewModel.setOpenCreateOutlet(true);
            startActivity(new Intent(OutletActivity.this, RegisterOutlet.class));
        });
        tipes = new ArrayList<>();
        cities = new ArrayList<>();
        outlets = new ArrayList<>();
        filteredOutlet = new ArrayList<>();
        adapter = new GenericRecyclerAdapter(filteredOutlet, R.layout.outlet_list_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Outlet currentOutlet = (Outlet) object;
                TextView nama = (TextView) view.findViewById(R.id.namaOutlet);
                nama.setText(currentOutlet.getNm_outlet());
                TextView alamat = (TextView) view.findViewById(R.id.alamatOutlet);
                alamat.setText(currentOutlet.getAlmt_outlet());
                TextView tipe = (TextView) view.findViewById(R.id.tipeOutlet);
                Tipe current = currentOutlet.getTipe();
                City currentCity = currentOutlet.getCity();
                if(current == null){
                    current = searchTipe(currentOutlet.getKd_tipe());
                    currentOutlet.setTipe(current);
                }
                if(currentCity == null){
                    currentCity = searchCity(currentOutlet.getKd_kota());
                    currentOutlet.setCity(currentCity);
                }
                tipe.setText(current == null ? "-" : current.getNama() + (currentCity ==  null ? "" : " - " +
                        currentCity.getNm_kota()));
                ImageView imOutlet = (ImageView) view.findViewById(R.id.im_outlet);
                Glide.with(OutletActivity.this).load(currentOutlet.getFoto())
                        .into(imOutlet);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
                    Intent intent = new Intent(OutletActivity.this, OutletDetail.class);
                    intent.putExtra("data", (Outlet) object);
                    ((Outlet) object).setCity(searchCity(((Outlet) object).getKd_kota()));
                    startActivity(intent);
                };
            }
        });
        adapter.setFilter(new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                filteredOutlet.removeAll(filteredOutlet);
                if (charString.isEmpty()) {
                    filteredOutlet.addAll(outlets);
                } else {
                    for (Outlet row : outlets) {
                        if (row.getNm_outlet().toLowerCase().contains(charString.toLowerCase())) {
                            filteredOutlet.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredOutlet;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.notifyDataSetChanged();
            }
        });
        binding.outletlist.setLayoutManager(new LinearLayoutManager(this));
        binding.outletlist.setAdapter(adapter);
        binding.outletlist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initObserver();
    }

    private Tipe searchTipe(int kd_tipe){
        for(Tipe current : tipes){
            if(current.getId() == kd_tipe){
                return current;
            }
        }
        return null;
    }

    private City searchCity(int kd_city){
        for(City current : cities){
            if(current.getId() == kd_city){
                return current;
            }
        }
        return null;
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
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getAllCity().observe(this, cities -> {
            if(this.cities.size() > 0){
                this.cities.removeAll(this.cities);
            }
            this.cities.addAll(cities);
            viewModel.getAllTipe().observe(this, tipes -> {
                if(this.tipes.size() > 0){
                    this.tipes.removeAll(this.tipes);
                }
                this.tipes.addAll(tipes);
                viewModel.getAllOutlet().observe(this, outlets -> {
                    if(this.outlets.size() > 0){
                        this.outlets.removeAll(this.outlets);
                    }
                    if(this.filteredOutlet.size() > 0){
                        this.filteredOutlet.removeAll(this.filteredOutlet);
                    }
                    this.outlets.addAll(outlets);
                    this.filteredOutlet.addAll(outlets);
                    adapter.notifyDataSetChanged();
                });
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.outlet_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            MenuItem menuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (adapter != null)
                        adapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null)
                        adapter.getFilter().filter(newText);
                    return true;
                }
            });
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setIconifiedByDefault(false);
        }
        return true;
    }

    @Override
    protected void onResume() {
        if(viewModel.isOpenCreateOutlet()) {
            viewModel.getAllOutlet();
        }
        super.onResume();
    }
}