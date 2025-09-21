package com.mustofa27.banksampah.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityProductSearchBinding;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Satuan;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.ProductSearchViewModel;

import java.util.ArrayList;

public class ProductSearch extends BaseActivity {

    ActivityProductSearchBinding binding;
    ProductSearchViewModel viewModel;
    ArrayList<Brand> brands;
    ArrayList<Product> products, selectedProduct;
    ArrayList<Satuan> satuans;
    ArrayAdapter brandArrayAdapter, otherSatuanAdapter;
    boolean isRefreshed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(ProductSearchViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("SEARCH PRODUCT");
        (getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        brands = new ArrayList<>();
        products = new ArrayList<>();
        satuans = new ArrayList<>();
        selectedProduct = new ArrayList<>();
        brandArrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, brands);
        otherSatuanAdapter = new ArrayAdapter(this, R.layout.custom_spinner, satuans);
        binding.brand.setAdapter(brandArrayAdapter);
        binding.brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Brand selected = brands.get(i);
                if(selected.getDefaultSatuan() == -1){
                    selected.setDefaultSatuan(searchSatuan(selected.getId_satuan()));
                }
                if(selected.getProductAdapter() == null){
                    selected.setAll(new ArrayList<>());
                    selected.setFiltered(new ArrayList<>());
                    selected.setProductAdapter(new GenericRecyclerAdapter(selected.getFiltered(), R.layout.listto_item, new AdapterCallback() {
                        @Override
                        public void bindView(View view, Object object) {
                            Product current = (Product) object;
                            ImageView minus = view.findViewById(R.id.minus);
                            ImageView plus = view.findViewById(R.id.plus);
                            CheckBox checkBox = view.findViewById(R.id.cb_produk);
                            TextView name = view.findViewById(R.id.prod_name);
                            EditText qty = view.findViewById(R.id.edt_qty);
                            if(current.getSatuanAdapter() == null){
                                current.setSatuanAdapter(new ArrayAdapter(ProductSearch.this, R.layout.custom_spinner, satuans));
                            }
                            Spinner satuan = view.findViewById(R.id.spin_satuan);
                            satuan.setAdapter(current.getSatuanAdapter());
                            satuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    current.setSatuan(satuans.get(i));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            if(current.getSatuan() != null) {
                                satuan.setSelection(satuans.indexOf(current.getSatuan()) < 0 ? 0 : satuans.indexOf(current.getSatuan()));
                            } else{
                                satuan.setSelection(selected.getDefaultSatuan());
                                current.setSatuan(satuans.get(selected.getDefaultSatuan()));
                            }
                            checkBox.setChecked(current.isSelected());
                            name.setText(current.getNama());
                            qty.setText(current.getQty() != 0 ? String.valueOf(current.getQty()) : "");
                            qty.setOnFocusChangeListener((view1, b) -> {
                                if(!b){
                                    if(isStringNotEmpty(qty) && !qty.getText().toString().equalsIgnoreCase("0")) {
                                        current.setQty(Integer.valueOf(qty.getText().toString()));
                                        if(!checkBox.isChecked()){
                                            selectedProduct.add(current);
                                            checkBox.setChecked(true);
                                            current.setSelected(checkBox.isChecked());
                                        }
                                    } else{
                                        current.setQty(0);
                                        if(checkBox.isChecked()) {
                                            checkBox.setChecked(!checkBox.isChecked());
                                            current.setSelected(checkBox.isChecked());
                                            selectedProduct.remove(current);
                                        }
                                    }
                                }
                            });
                            qty.setOnEditorActionListener((v, actionId, event) -> {
                                if(actionId== EditorInfo.IME_ACTION_DONE){
                                    v.clearFocus();
                                }
                                return false;
                            });
                            name.setOnClickListener(view1 -> {
                                checkBox.setChecked(!checkBox.isChecked());
                                current.setSelected(checkBox.isChecked());
                                if(checkBox.isChecked()){
                                    if(current.getQty() == 0) {
                                        qty.setText("1");
                                        current.setQty(1);
                                    }
                                    selectedProduct.add(current);
                                } else{
                                    qty.setText("");
                                    current.setQty(0);
                                    selectedProduct.remove(current);
                                }
                            });
                            checkBox.setOnClickListener(view1 -> {
                                current.setSelected(checkBox.isChecked());
                                if(checkBox.isChecked()){
                                    if(current.getQty() == 0) {
                                        qty.setText("1");
                                        current.setQty(1);
                                    }
                                    selectedProduct.add(current);
                                } else{
                                    qty.setText("");
                                    current.setQty(0);
                                    selectedProduct.remove(current);
                                }
                            });
                            minus.setOnClickListener(view1 -> {
                                if(current.getQty() > 0){
                                    current.setQty(current.getQty()-1);
                                    if(current.getQty() == 0){
                                        selectedProduct.remove(current);
                                        checkBox.setChecked(!checkBox.isChecked());
                                        current.setSelected(checkBox.isChecked());
                                        qty.setText("");
                                    } else{
                                        qty.setText(String.valueOf(current.getQty()));
                                    }
                                }
                            });
                            plus.setOnClickListener(view1 -> {
                                if(current.getQty() == 0){
                                    checkBox.setChecked(!checkBox.isChecked());
                                    current.setSelected(checkBox.isChecked());
                                    selectedProduct.add(current);
                                }
                                current.setQty(current.getQty()+1);
                                qty.setText(String.valueOf(current.getQty()));
                            });
                        }

                        @Override
                        public View.OnClickListener onClickItem(Object object) {
                            return null;
                        }
                    }));
                    selected.getProductAdapter().setFilter(new Filter() {
                        @Override
                        protected FilterResults performFiltering(CharSequence charSequence) {
                            String charString = charSequence.toString();
                            selected.getFiltered().removeAll(selected.getFiltered());
                            if (charString.isEmpty()) {
                                selected.getFiltered().addAll(selected.getAll());
                            } else {
                                for (Product row : selected.getAll()) {
                                    if (row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                                        selected.getFiltered().add(row);
                                    }
                                }
                            }

                            FilterResults filterResults = new FilterResults();
                            filterResults.values = selected.getFiltered();
                            return filterResults;
                        }

                        @Override
                        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                            selected.getProductAdapter().notifyDataSetChanged();
                        }
                    });
                }
                if(selected.getAll().size() == 0){
                    for(Product product : products){
                        if(product.getBrand_id() == selected.getId()){
                            selected.getAll().add(product);
                        }
                    }
                    selected.getFiltered().addAll(selected.getAll());
                }
                binding.productList.setAdapter(selected.getProductAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.productList.setLayoutManager(new LinearLayoutManager(this));
        binding.spinSatuanOther.setAdapter(otherSatuanAdapter);
        binding.minus.setOnClickListener(view1 -> {
            int qty = isStringNotEmpty(binding.edtQtyOther) ? Integer.valueOf(binding.edtQtyOther.getText().toString()) : 0;
            if(qty > 0){
                qty--;
                if(qty == 0){
                    binding.edtQtyOther.setText("");
                } else{
                    binding.edtQtyOther.setText(String.valueOf(qty));
                }
            }
        });
        binding.plus.setOnClickListener(view1 -> {
            int qty = isStringNotEmpty(binding.edtQtyOther) ? Integer.valueOf(binding.edtQtyOther.getText().toString()) : 0;
            qty++;
            binding.edtQtyOther.setText(String.valueOf(qty));
        });
        binding.btnSelectProduct.setOnClickListener(view -> {
            if(isStringNotEmpty(binding.name)){
                if(isStringNotEmpty(binding.edtQtyOther)){
                    Product other = new Product();
                    other.setId("0");
                    other.setNama(binding.name.getText().toString());
                    other.setQty(Integer.valueOf(binding.edtQtyOther.getText().toString()));
                    other.setSatuan((Satuan) binding.spinSatuanOther.getSelectedItem());
                    selectedProduct.add(other);
                } else{
                    showMessageFailed("Pastikan semua isian terisi");
                    return;
                }
            }
            if(selectedProduct.size() == 0){
                showMessageFailed("Pastikan anda telah memilih minimal 1 produk");
            } else{
                Intent intent = getIntent();
                intent.putExtra("data", selectedProduct);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        initObserver();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.loading.getRoot().setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
        viewModel.getAllBrand(isRefreshed).observe(this, brands -> {
            if(this.brands.size() > 0){
                this.brands.removeAll(this.brands);
            }
            this.brands.addAll(brands);
            viewModel.getAllProduct(isRefreshed).observe(this, products -> {
                if(this.products.size() > 0){
                    this.products.removeAll(this.products);
                }
                this.products.addAll(products);
                viewModel.getAllSatuan(isRefreshed).observe(this, satuans -> {
                    if(this.satuans.size() > 0){
                        this.satuans.removeAll(this.satuans);
                    }
                    for(Brand brand : brands){
                        if(brand.getProductAdapter() != null){
                            brand.getAll().removeAll(brand.getAll());
                            brand.getFiltered().removeAll(brand.getFiltered());
                            brand.getAll().addAll(products);
                            brand.getFiltered().addAll(products);
                            brand.getProductAdapter().notifyDataSetChanged();
                        }
                    }
                    this.satuans.addAll(satuans);
                    otherSatuanAdapter.notifyDataSetChanged();
                    brandArrayAdapter.notifyDataSetChanged();
                    viewModel.getLoading().setValue(false);
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
                    //Log.d(TAG, "onQueryTextSubmit: ");
                    Brand brandSelected = ((Brand) binding.brand.getSelectedItem());
                    brandSelected.getProductAdapter().getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Log.d(TAG, "onQueryTextChange: ");
                    Brand brandSelected = ((Brand) binding.brand.getSelectedItem());
                    brandSelected.getProductAdapter().getFilter().filter(newText);
                    return true;
                }
            });
            searchView.setQueryHint("Cari Produk");
            searchView.setIconifiedByDefault(false);
        }
        return true;
    }

    private int searchSatuan(int id){
        for(int i = 0; i < satuans.size(); i++){
            if(id == satuans.get(i).getId()){
                return i;
            }
        }
        return 0;
    }
}