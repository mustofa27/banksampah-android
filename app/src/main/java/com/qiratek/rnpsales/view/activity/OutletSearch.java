package com.qiratek.rnpsales.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityOutletSearchBinding;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.entity.City;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.model.entity.VisitPlanDb;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.view.adapter.AdapterCallback;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.OutletSearchViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class OutletSearch extends BaseActivity {

    String tipe;
    ActivityOutletSearchBinding binding;
    ArrayList<Outlet> allOutlets, filtered, outletFromDBs;
    GenericRecyclerAdapter adapter;
    ArrayList<Tipe> tipes;
    ArrayList<City> cities;
    OutletSearchViewModel viewModel;
    ArrayList<VisitPlanDb> visitPlanDbs, visitPart, visitPlanTerpakai;
    int day = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutletSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("SEARCH OUTLET");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        tipe = getIntent().getStringExtra("tipe");
        allOutlets = new ArrayList<>();
        filtered = new ArrayList<>();
        tipes = new ArrayList<>();
        cities = new ArrayList<>();
        if(tipe.equalsIgnoreCase("visitplan")){
            visitPart = new ArrayList<>();
            visitPlanDbs = new ArrayList<>();
            visitPlanTerpakai = new ArrayList<>();
            outletFromDBs = new ArrayList<>();
        }
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(OutletSearchViewModel.class);
        adapter = new GenericRecyclerAdapter(filtered, R.layout.outlet_list_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Outlet currentOutlet = (Outlet) object;
                TextView nama = view.findViewById(R.id.namaOutlet);
                TextView alamat = view.findViewById(R.id.alamatOutlet);
                nama.setText(currentOutlet.getNm_outlet());
                alamat.setText(currentOutlet.getAlmt_outlet());
                TextView type = view.findViewById(R.id.tipeOutlet);
                City kota = searchCity(currentOutlet.getKd_kota());
                type.setText(searchTipe(currentOutlet.getKd_tipe()).getNama() + (kota == null ? "":" - " +
                        kota.getNm_kota()));
                ImageView imOutlet = (ImageView) view.findViewById(R.id.im_outlet);
                Glide.with(OutletSearch.this).load(currentOutlet.getFoto())
                        .into(imOutlet);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
                    Intent returnIntent = getIntent();
                    if (tipe.equals("outlet") /*|| tipe.equalsIgnoreCase("outletvisit")*/) {
                        returnIntent.putExtra("data", (Outlet) object);
                    }
                    else if (tipe.equals("visitplan")) {
                        VisitPlanDb current = visitPart.get(filtered.indexOf((Outlet)object));
                        returnIntent.putExtra("data_visit", current);
                        returnIntent.putExtra("data", (Outlet) object);
                    }/* else if (tipe.equals("take_order") || tipe.equals("stock") || tipe.equals("sample") || tipe.equals("pop")) {
                        returnIntent.putExtra("kd_visit", kd_visits_used.get(position));
                        returnIntent.putExtra("outlet_name", ((Outlet) outletPart.get(position)).getNama());
                    }*/
                    if (!tipe.equals("registervisit")) {
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                };
            }
        });

        adapter.setFilter(new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                filtered.removeAll(filtered);
                if (tipe.equals("visitplan")) {
                    visitPart.removeAll(visitPart);
                }
                if (charString.isEmpty()) {
                    filtered.addAll(allOutlets);
                    if (tipe.equals("visitplan")) {
                        visitPart.addAll(visitPlanTerpakai);
                    }
                } else {
                    for (Outlet row : allOutlets) {
                        if (row.getNm_outlet().toLowerCase().contains(charString.toLowerCase())) {
                            filtered.add(row);
                            if (tipe.equals("visitplan")) {
                                visitPart.add(visitPlanTerpakai.get(allOutlets.indexOf(row)));
                            }
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                  adapter.notifyDataSetChanged();
            }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
        binding.list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initObserver();
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
                    if (OutletSearch.this.adapter != null)
                        OutletSearch.this.adapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (OutletSearch.this.adapter != null)
                        OutletSearch.this.adapter.getFilter().filter(newText);
                    return true;
                }
            });
            if (tipe.equals("visitplan") || tipe.equals("take_order") || tipe.equals("stock") || tipe.equals("sample") || tipe.equals("pop")) {
                searchView.setQueryHint("Cari Visit Plan");
            } else {
                searchView.setQueryHint(getResources().getString(R.string.search_hint));
            }
            searchView.setIconifiedByDefault(false);
        }
        return true;
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
                    if(this.allOutlets.size() > 0){
                        this.allOutlets.removeAll(this.allOutlets);
                    }
                    if(this.filtered.size() > 0){
                        this.filtered.removeAll(this.filtered);
                    }
                    if(tipe.equalsIgnoreCase("visitplan")){
                        outletFromDBs.removeAll(outletFromDBs);
                        outletFromDBs.addAll(outlets);
                        viewModel.getAllVisit().observe(this, visitPlans -> {
                            visitPlanDbs.removeAll(visitPlanDbs);
                            visitPlanDbs.addAll(visitPlans);
                            visitPlanTerpakai.removeAll(visitPlanTerpakai);
                            visitPart.removeAll(visitPart);
                            for (int i = 0; i < visitPlanDbs.size(); i++) {
                                VisitPlanDb current = (VisitPlanDb) visitPlanDbs.get(i);
                                if (compare(current.getDate_visit(), day)) {
                                    allOutlets.add(searchOutlet(current.getOutlet_id()));
                                    visitPlanTerpakai.add(visitPlanDbs.get(i));
                                    visitPart.add(visitPlanDbs.get(i));
                                }
                            }
                            filtered.addAll(allOutlets);
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        this.allOutlets.addAll(outlets);
                        this.filtered.addAll(outlets);
                        adapter.notifyDataSetChanged();
                    }
                });
            });
        });
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

    private Outlet searchOutlet(String outlet_id){
        for(Outlet current : outletFromDBs){
            if(current.getKd_outlet() == outlet_id){
                return current;
            }
        }
        return null;
    }

    private boolean compare(String date_visit, int dayTolerance) {
        try {
            String tanggal = date_visit.split(" ")[0];
            String[] sp_tanggal = tanggal.split("-");
            Calendar now = Calendar.getInstance();
            Calendar visit = Calendar.getInstance();
            visit.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sp_tanggal[2]));
            visit.set(Calendar.MONTH, Integer.parseInt(sp_tanggal[1]) - 1);
            visit.set(Calendar.YEAR, Integer.parseInt(sp_tanggal[0]));
            long nowMilis = now.getTimeInMillis();
            long visitMilis = visit.getTimeInMillis();
            return visitMilis >= nowMilis - dayTolerance * 24 * 3600 * 1000;
        } catch (Exception e) {
            return false;
        }
    }
}