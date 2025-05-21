package com.qiratek.rnpsales.view.activity;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityBillingBinding;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.entity.BillingData;
import com.qiratek.rnpsales.model.entity.Outlet;
import com.qiratek.rnpsales.model.helper.MoneyWatcher;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.BillingViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BillingActivity extends BaseActivity {

    ActivityBillingBinding binding;
    BillingViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private Outlet selectedOutlet;
    double latitude, longitude, distance = -1;
    File fotoFile;
    Uri selectedImage;
    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        viewModel.getStatusCamera().setValue(result);
    });
    String[] isiStatus = new String[]{"Visit", "Tidak Visit"};
    ArrayAdapter arrayAdapter;
    MoneyWatcher jumlahWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerActivityResult();
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(BillingViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("BILLING");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        binding.btnCheckIn.setOnClickListener(view -> {
            if (selectedOutlet != null) {
                if (ContextCompat.checkSelfPermission(BillingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(BillingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BillingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(BillingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showMessageFailed("This application need permission to access location");
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
                }
            }
        });
        binding.selectVisit.setOnClickListener(view -> selectOutlet("outlet"));
        binding.btnSubmit.setOnClickListener(view -> {
            if (isStringNotEmpty(binding.jumlah) && selectedOutlet != null) {
                if (binding.status.getSelectedItem().toString().equalsIgnoreCase("visit")) {
                    viewModel.getConfiguration(false).observe(this, configuration -> {
                        double toleransi = configuration.getToleransi_max() + 1;
                        if (distance == -1)
                            showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                        else if (distance > toleransi) {
                            showMessageFailed("Koordinat anda tidak valid");
                        } else {
                            submitData();
                        }
                        viewModel.getConfigurationLiveData().removeObservers(this);
                    });
                } else {
                    submitData();
                }
            } else {
                if(selectedOutlet == null) {
                    showMessageFailed("Pastikan untuk memilih outlet terlebih dahulu");
                } else {
                    showMessageFailed("Pastikan nominal pembayaran sudah terisi");
                }
            }
        });
        binding.btnSave.setOnClickListener(view -> {
            if (isStringNotEmpty(binding.jumlah) && selectedOutlet != null) {
                if (binding.status.getSelectedItem().toString().equalsIgnoreCase("visit")) {
                    viewModel.getConfiguration(true).observe(this, configuration -> {
                        double toleransi = configuration.getToleransi_draft() + 1;
                        if (distance == -1)
                            showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                        else if (distance > toleransi) {
                            showMessageFailed("Koordinat anda tidak valid");
                        } else {
                            saveData();
                        }
                        viewModel.getConfigurationLiveData().removeObservers(this);
                    });
                } else {
                    saveData();
                }
            } else {
                if(selectedOutlet == null) {
                    showMessageFailed("Pastikan untuk memilih outlet terlebih dahulu");
                } else {
                    showMessageFailed("Pastikan nominal pembayaran sudah terisi");
                }
            }
        });
        binding.buttonTake.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Pastikan anda mengambil foto dalam posisi LANDSCAPE");
            builder.setTitle("Perhatikan !!");
            builder.setPositiveButton("Siap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(BillingActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                            fotoFile = new File(getFilesDir(), imageFileName);
                            selectedImage = FileProvider.getUriForFile(BillingActivity.this, getApplicationContext().getPackageName() + ".provider",fotoFile);
                            activityResultLauncher.launch(selectedImage);
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BillingActivity.this, Manifest.permission.CAMERA)) {
                                showMessageFailed("This application need permission to use camera and save the image in storage");
                            } else {
                                ActivityCompat.requestPermissions(BillingActivity.this, new String[]{Manifest.permission.CAMERA},
                                        1);
                            }
                        }
                    } else{
                        if (ContextCompat.checkSelfPermission(BillingActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(BillingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                            fotoFile = new File(getFilesDir(), imageFileName);
                            selectedImage = FileProvider.getUriForFile(BillingActivity.this, getApplicationContext().getPackageName() + ".provider",fotoFile);
                            activityResultLauncher.launch(selectedImage);
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BillingActivity.this, Manifest.permission.CAMERA) &&
                                    ActivityCompat.shouldShowRequestPermissionRationale(BillingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageFailed("This application need permission to use camera and save the image in storage");
                            } else {
                                ActivityCompat.requestPermissions(BillingActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1);
                            }
                        }
                    }
                }
            });
            builder.show();
        });
        jumlahWatcher = new MoneyWatcher(binding.jumlah, new Locale("in_ID"), 3);
        binding.jumlah.addTextChangedListener(jumlahWatcher);
        arrayAdapter = new ArrayAdapter(this, R.layout.custom_spinner, isiStatus);
        binding.status.setAdapter(arrayAdapter);
        binding.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                validationProcess(binding.status.getSelectedItem().toString().equalsIgnoreCase("visit"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.tipePembayaran.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i == radioGroup.getChildAt(1).getId()){
                binding.containerTransfer.setVisibility(View.VISIBLE);
            } else{
                binding.containerTransfer.setVisibility(View.GONE);
            }
        });
        ((RadioButton)binding.tipePembayaran.getChildAt(0)).setChecked(true);
        initObserver();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if (isLoading) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            if(status) {
                showMessageSuccess("Submit data berhasil");
                finish();
            } else{
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getStatusCamera().observe(this, status -> {
            if(status) {
                try {
                    selectedImage = getResizedImage(this, selectedImage, fotoFile.getName());
                    binding.tpTakephoto.setImageURI(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessageFailed("Camera failed, please clear some space and try again");
                }
            } else{
                showMessageFailed("Camera failed");
            }
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
                    }
                }));
    }

    void submitData(){
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("outlet_id", selectedOutlet.getKd_outlet());
            param.put("status", binding.status.getSelectedItemPosition() == 0 ? 1 : 0);
            param.put("nilai_transfer", binding.containerTransfer.getVisibility() == View.VISIBLE ? binding.jumlah.getText().toString().replace(",", "") : 0);
            param.put("nilai_cash", binding.containerTransfer.getVisibility() == View.GONE ? binding.jumlah.getText().toString().replace(",", "") : 0);
            param.put("nominal_giro", 0);
            param.put("nomor_giro", 0);
            param.put("nama_bank", "-");
            param.put("note", binding.note.getText().toString());
            param.put("metode_pembayaran", 1);
            //param.put("due_date", null);
            Map<String, MultipartFile> paramFile = new HashMap<>();
            if (selectedImage != null) {
                paramFile.put("path_image", new MultipartFile(fotoFile.getName(), selectedImage, this));
            }
            viewModel.submit(param, paramFile);
        } catch (IOException e) {
            showMessageFailed("Gagal membaca file gambar");
            e.printStackTrace();
        }
    }

    void saveData(){
        BillingData billingData = new BillingData();
        billingData.setOutlet_id(selectedOutlet.getKd_outlet());
        billingData.setStatus(binding.status.getSelectedItemPosition() == 0 ? 1 : 0);
        billingData.setTransfer_value(binding.containerTransfer.getVisibility() == View.VISIBLE ? Integer.valueOf(binding.jumlah.getText().toString().replace(",", "")) : 0);
        billingData.setCash_value(binding.containerTransfer.getVisibility() == View.GONE ? Integer.valueOf(binding.jumlah.getText().toString().replace(",", "")) : 0);
        billingData.setNominal_giro(0);
        billingData.setNomor_giro(null);
        billingData.setBank_nm(null);
        billingData.setNote(binding.note.getText().toString());
        billingData.setPayment_method(1);
        billingData.setDue_date(null);
        billingData.setGambar(selectedImage != null ? selectedImage.toString() : "-");
        billingData.setCreated_at(getDateNowFormal());
        viewModel.submit(billingData);
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
}