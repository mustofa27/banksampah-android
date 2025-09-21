package com.mustofa27.banksampah.view.activity;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityTakeOrderBinding;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.TakeOrderData;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.TakeOrderViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TakeOrderActivity extends BaseActivity {

    ActivityTakeOrderBinding binding;
    TakeOrderViewModel viewModel;
    Outlet selectedOutlet;
    double latitude, longitude, distance = -1;
    private FusedLocationProviderClient fusedLocationClient;
    ArrayList<Product> selectedProduct;
    String[] isiStatus = new String[]{"Visit", "Tidak Visit"};
    ArrayAdapter statusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTakeOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("TAKE ORDER");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        selectedProduct = new ArrayList<>();
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(TakeOrderViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        binding.selectVisit.setOnClickListener(v -> {
            selectOutlet("outlet");
        });
        binding.selectProduct.setOnClickListener(v -> {
            selectProduct();
        });
        binding.btnCheckIn.setOnClickListener(v -> {
            if (selectedOutlet != null) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Location Getter");
                    progressDialog.setMessage("Getting location, please wait!");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                        @Override
                        public boolean isCancellationRequested() {
                            return false;
                        }

                        @NonNull
                        @Override
                        public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                            return null;
                        }
                    }).addOnSuccessListener(this, location -> {
                        progressDialog.dismiss();
                        if (location != null) {
                            binding.svKoordinat.setText(location.getLatitude() + " , " + location.getLongitude());
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Location asal = new Location(location);
                            asal.setLongitude(Double.valueOf(selectedOutlet.getLongitude()));
                            asal.setLatitude(Double.valueOf(selectedOutlet.getLatitude()));
                            distance = calculateDistance(asal, location);
                            binding.svJarak.setText(String.valueOf((int) distance) + " meter");
                        } else{
                            showMessageFailed("Get location failed, please try again later");
                        }
                    });
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showMessageFailed("This application need permission to access location");
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
                }
            }
        });
        registerActivityResult();
        statusAdapter = new ArrayAdapter(this, R.layout.custom_spinner, isiStatus);
        binding.status.setAdapter(statusAdapter);
        binding.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                validationProcess(binding.status.getSelectedItem().toString().equalsIgnoreCase("visit"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnSubmit.setOnClickListener(view -> {
            if (selectedOutlet == null) {
                showMessageFailed("Silahkan pilih outlet terlebih dahulu");
            }
            else if (selectedProduct.size() == 0){
                showMessageFailed("Silahkan pilih produk terlebih dahulu");
            }
            else {
                if (binding.status.getSelectedItem().toString().equalsIgnoreCase("visit")) {
                    viewModel.getConfiguration(false).observe(this, configuration -> {
                        double toleransi = configuration.getToleransi_max() + 1;
                        if (distance == -1)
                            showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                        else if (distance > toleransi) {
                            showMessageFailed("Koordinat anda tidak valid");
                        } else {
                            submitData(false);
                        }
                        viewModel.getConfigurationLiveData().removeObservers(this);
                    });
                }
                else {
                    submitData(false);
                }
            }
        });

        binding.btnSave.setOnClickListener(view -> {
            if (selectedOutlet == null) {
                showMessageFailed("Silahkan pilih outlet terlebih dahulu");
            }
            else if (selectedProduct.size() == 0){
                showMessageFailed("Silahkan pilih produk terlebih dahulu");
            }
            else {
                if (binding.status.getSelectedItem().toString().equalsIgnoreCase("visit")) {
                    viewModel.getConfiguration(true).observe(this, configuration -> {
                        double toleransi = configuration.getToleransi_draft() + 1;
                        if (distance == -1)
                            showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                        else if (distance > toleransi) {
                            showMessageFailed("Koordinat anda tidak valid");
                        } else {
                            submitData(true);
                        }
                        viewModel.getConfigurationLiveData().removeObservers(this);
                    });
                }
                else {
                    submitData(true);
                }
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
            showMessage(status);
            if(status) {
                finish();
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }

    void registerActivityResult(){
        setActivityResultLaunch(registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == SELECT_OUTLET_CODE) {
                        selectedOutlet = (Outlet) result.getData().getSerializableExtra("data");
                        binding.selectVisit.setText(selectedOutlet.getNm_outlet());
                        binding.svJarak.setText("");
                        binding.svKoordinat.setText("");
                        distance = -1;
                    } else if(result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == SELECT_PRODUCT_CODE){
                        ArrayList<Product> products = (ArrayList<Product>) result.getData().getSerializableExtra("data");
                        selectedProduct.addAll(products);
                        for(Product product : products){
                            final View tambahan = getLayoutInflater().inflate(R.layout.to_item, null);
                            TextView nama = tambahan.findViewById(R.id.prod_name);
                            TextView tvqty = tambahan.findViewById(R.id.qty);
                            FrameLayout delete = tambahan.findViewById(R.id.delete);
                            nama.setText(product.getNama());
                            tvqty.setText("" + product.getQty() + " " + product.getSatuan().getNama());
                            binding.container.addView(tambahan);
                            delete.setOnClickListener(view -> {
                                binding.container.removeView(tambahan);
                                selectedProduct.remove(product);
                            });
                        }
                    }
                }));
    }

    private void validationProcess(boolean status) {
        if (status) {
            binding.containerValidasi.setVisibility(View.VISIBLE);
            binding.svJarak.setText("");
            binding.svKoordinat.setText("");
            distance = -1;
        } else {
            binding.containerValidasi.setVisibility(View.GONE);
        }
    }

    private void submitData(boolean isSaved) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("outlet_id", selectedOutlet.getKd_outlet());
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < selectedProduct.size(); i++) {
                JSONObject to = new JSONObject();
                if (selectedProduct.get(i).getQty() > 0) {
                    to.put("produk_id", selectedProduct.get(i).getId());
                    to.put("qty_order", selectedProduct.get(i).getQty());
                    to.put("other_produk", selectedProduct.get(i).getNama());
                    to.put("satuan_id", selectedProduct.get(i).getSatuan().getId());
                    to.put("catatan", binding.note.getText().toString());
                    to.put("status_order", binding.status.getSelectedItemPosition() == 0 ? 1 : 0);
                    jsonArray.put(to);
                }
            }
            jsonObject.put("items", jsonArray);
            if(isSaved){
                TakeOrderData takeOrderData = new TakeOrderData();
                jsonObject.put("draft", 1);
                takeOrderData.setJsonContent(jsonObject.toString());
                viewModel.saveTakeOrder(takeOrderData);
            } else {
                viewModel.submitTakeOrder(jsonObject);
            }
        } catch (JSONException e) {
            showMessageFailed("Terjadi kesalahan, silahkan coba lagi");
            e.printStackTrace();
        }
    }
}