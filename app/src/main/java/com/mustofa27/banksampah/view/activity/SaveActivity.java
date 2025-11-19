package com.mustofa27.banksampah.view.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivitySaveBinding;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HomeViewModel;
import com.mustofa27.banksampah.viewmodel.SavingViewModel;

import java.util.ArrayList;

public class SaveActivity extends BaseActivity {
    ActivitySaveBinding binding;
    ArrayList<Garbage> garbageArrayList;
    ArrayAdapter garbageArrayAdapter;
    SavingViewModel viewModel;
    ProgressDialog progressDialog;
    InputMethodManager imm;
    boolean statusSubmit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.topbar.title.setText("Simpan Sampah");
        binding.topbar.back.setOnClickListener(v -> finish());
        garbageArrayList = new ArrayList<>();
        garbageArrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, garbageArrayList);
        binding.jenisSampah.setAdapter(garbageArrayAdapter);
        binding.jenisSampah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showDetail(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.topbar.cartContainer.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait");
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(SavingViewModel.class);
        binding.berat.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.estimasi.setText(getMoneyFormat((long) Math.floor(Float.parseFloat(binding.berat.getText().toString())*((Garbage)binding.jenisSampah.getSelectedItem()).getPrice_per_kg()/100)*100));
                imm.hideSoftInputFromWindow(binding.berat.getWindowToken(), 0);
            }
            return true;
        });
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        binding.submit.setOnClickListener(v -> {
            statusSubmit = true;
            viewModel.addSaving(Float.parseFloat(binding.berat.getText().toString()),((Garbage)binding.jenisSampah.getSelectedItem()).getId());
        });
        initObserver();
    }

    private void showDetail(int position){
        Garbage current = garbageArrayList.get(position);
        if(current != null){
            binding.containerKontenJenis.setVisibility(VISIBLE);
            binding.category.setText(current.getCategoryText());
            binding.price.setText(getMoneyFormat(current.getPrice_per_kg()));
            binding.description.setText(current.getDescription());
            binding.estimasi.setText(getMoneyFormat((long) Math.floor(Float.parseFloat(binding.berat.getText().toString())*current.getPrice_per_kg()/100)*100));
        } else{
            binding.containerKontenJenis.setVisibility(GONE);
            binding.estimasi.setText(getMoneyFormat(0));
        }
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if(isLoading){
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            if(!status || statusSubmit) {
                showMessage(status);
                finish();
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getAllGarbage().observe(this, garbages -> {
            if(!garbageArrayList.isEmpty()){
                garbageArrayList.clear();
            }
            garbageArrayList.addAll(garbages);
            garbageArrayAdapter.notifyDataSetChanged();
            showLoading(false);
        });
        viewModel.getCart().observe(this, carts -> {
            if(carts.size() > 0){
                binding.topbar.indicator.setText(String.valueOf(carts.size()));
                binding.topbar.indicator.setVisibility(VISIBLE);
            } else{
                binding.topbar.indicator.setVisibility(GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getCart();
    }
}