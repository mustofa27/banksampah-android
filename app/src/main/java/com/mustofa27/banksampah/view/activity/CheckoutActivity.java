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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityCheckoutBinding;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CheckoutViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends BaseActivity {

    ActivityCheckoutBinding binding;
    CheckoutViewModel viewModel;
    private Outlet selectedOutlet;
    private VisitPlanDb selectedVisit;
    private FusedLocationProviderClient fusedLocationClient;
    double latitude, longitude, distance = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerActivityResult();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("CHECKOUT");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(CheckoutViewModel.class);
        selectedVisit = (VisitPlanDb) getIntent().getSerializableExtra("data");
        //binding.selectVisit.setOnClickListener(v -> selectOutlet("visitplan"));
        binding.btnCheckIn.setOnClickListener(v -> {
            if (selectedOutlet != null) {
                if (ContextCompat.checkSelfPermission(CheckoutActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(CheckoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CheckoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(CheckoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showMessageFailed("This application need permission to access location");
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
                }
            }
        });
        binding.btnSubmit.setOnClickListener(v -> {
            if (selectedOutlet != null) {
                double toleransi = selectedOutlet.getToleransi_jarak() + 1;
                if (distance == -1)
                    showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                else if (distance > toleransi) {
                    showMessageFailed("Koordinat anda tidak valid");
                } else {
                    if(isStringNotEmpty(selectedVisit.getCreated_at())) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("visit_plan_id", selectedVisit.getId());
                            jsonObject.put("date_checkout", getCurrentDateFormal());
                            viewModel.submitCheckout(jsonObject, selectedVisit).observe(this, visitPlanDb -> {
                                if (visitPlanDb != null) {
                                    if (visitPlanDb.getDate_checkout() != null && isStringNotEmpty(visitPlanDb.getDate_checkout())) {
                                        showMessageSuccess("Checkout berhasil");
                                        finish();
                                    } else {
                                        showMessageFailed("Checkout gagal");
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            showMessageFailed("Terjadi kesalahan, silahkan coba lagi");
                            e.printStackTrace();
                        }
                    } else{
                        selectedVisit.setIs_checkout(1);
                        selectedVisit.setDate_checkout(getCurrentDateFormal());
                        viewModel.saveVisit(selectedVisit).observe(this, visitPlanDb -> {
                            if (visitPlanDb != null) {
                                if (visitPlanDb.getDate_checkout() != null && isStringNotEmpty(visitPlanDb.getDate_checkout())) {
                                    showMessageSuccess("Checkout berhasil");
                                    finish();
                                } else {
                                    showMessageFailed("Checkout gagal");
                                }
                            }
                        });
                    }
                }
            } else {
                showMessageFailed("Silahkan pilih kunjungan terlebih dahulu");
            }
        });
        initObserver();
    }

    void registerActivityResult(){
        setActivityResultLaunch(registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == SELECT_OUTLET_CODE) {
                        selectedOutlet = (Outlet) result.getData().getSerializableExtra("data");
                        selectedVisit = (VisitPlanDb) result.getData().getSerializableExtra("data_visit");
                        ((RelativeLayout)binding.namaOutlet.getParent()).setVisibility(View.VISIBLE);
                        binding.namaOutlet.setText(selectedOutlet.getNm_outlet());
                        binding.svJarak.setText("");
                        binding.svKoordinat.setText("");
                        distance = -1;
                        binding.alamatOutlet.setText(selectedOutlet.getAlmt_outlet());
                        if(viewModel.getTipeLiveData() != null && viewModel.getTipeLiveData().hasActiveObservers()){
                            viewModel.getTipe(selectedOutlet.getKd_tipe());
                        } else {
                            viewModel.getTipe(selectedOutlet.getKd_tipe()).observe(CheckoutActivity.this, tipe -> {
                                binding.tipeOutlet.setText(tipe != null ? tipe.getNama() : "-");
                            });
                        }
                        Glide.with(CheckoutActivity.this).load(selectedOutlet.getFoto())
                                .into(binding.imOutlet);
                    }
                }));
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

        if(selectedVisit != null){
            selectedOutlet = selectedVisit.getOutlets();
            ((RelativeLayout)binding.namaOutlet.getParent()).setVisibility(View.VISIBLE);
            binding.namaOutlet.setText(selectedOutlet.getNm_outlet());
            binding.svJarak.setText("");
            binding.svKoordinat.setText("");
            distance = -1;
            binding.alamatOutlet.setText(selectedOutlet.getAlmt_outlet());
            if(viewModel.getTipeLiveData() != null && viewModel.getTipeLiveData().hasActiveObservers()){
                viewModel.getTipe(selectedOutlet.getKd_tipe());
            } else {
                viewModel.getTipe(selectedOutlet.getKd_tipe()).observe(CheckoutActivity.this, tipe -> {
                    binding.tipeOutlet.setText(tipe != null ? tipe.getNama() : "-");
                });
            }
            Glide.with(CheckoutActivity.this).load( selectedOutlet.getFoto())
                    .into(binding.imOutlet);
        }
    }
}