package com.mustofa27.banksampah.view.activity;

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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivitySubmitVisitBinding;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.CheckInTimer;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.helper.TimerPreference;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.SubmitVisitViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmitVisit extends BaseActivity {

    ActivitySubmitVisitBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Outlet selectedOutlet;
    double latitude, longitude, distance = -1;
    SubmitVisitViewModel viewModel;
    File fotoFile;
    Uri selectedImage;
    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        viewModel.getStatusCamera().setValue(result);
    });
    TimerPreference timerPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerActivityResult();
        binding = ActivitySubmitVisitBinding.inflate(getLayoutInflater());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("CHECK IN");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        timerPreference = new TimerPreference(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(SubmitVisitViewModel.class);
        binding.btnCheckIn.setOnClickListener(view -> {
            if (selectedOutlet != null) {
                if (ContextCompat.checkSelfPermission(SubmitVisit.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(SubmitVisit.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SubmitVisit.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(SubmitVisit.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        showMessageFailed("This application need permission to access location");
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);
                    }
                }
            }
        });
        binding.btnSubmit.setOnClickListener(view -> {
            if (selectedOutlet != null) {
                viewModel.getConfiguration(false).observe(this, configuration -> {
                    double toleransi = configuration.getToleransi_max() + 1;
                    if (distance == -1)
                        showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                    else if (distance > toleransi) {
                        showMessageFailed("Koordinat anda tidak valid");
                    } else if (selectedImage == null) {
                        showMessageFailed("Pastikan semua data terisi");
                    } else {
                        view.setEnabled(false);
                        try {
                            Map<String, Object> param = new HashMap<>();
                            param.put("outlet_id", selectedOutlet.getKd_outlet());
                            param.put("date_visit", getCurrentDateFormal());
                            param.put("skip_order_reason", "");
                            param.put("if_close", 0);
                            param.put("keterangan_if_close", "-");
                            Map<String, MultipartFile> paramFile = new HashMap<>();
                            paramFile.put("path_image", new MultipartFile(fotoFile.getName(), selectedImage, this));
                            viewModel.submitVisit(param, paramFile).observe(this, visitPlanDb -> {
                                if(visitPlanDb != null) {
                                    if(isStringNotEmpty(visitPlanDb.getDate_checkout())){
                                        showMessageFailed("Hari ini kamu sudah pernah checkin pada outlet yang sama");
                                        showLoading(false);
                                    } else{
                                        Calendar calendar = Calendar.getInstance();
                                        CheckInTimer checkInTimer = new CheckInTimer();
                                        checkInTimer.setRunning(true);
                                        checkInTimer.setStartTime(calendar.getTimeInMillis());
                                        checkInTimer.setEndTime(calendar.getTimeInMillis() + 10*60*1000);
                                        checkInTimer.setRemainingTime(10*60*1000);
                                        timerPreference.setTimerPreference(checkInTimer);
                                        Intent intent = new Intent(this, CountDownTimerActivity.class);
                                        intent.putExtra("data", visitPlanDb);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            view.setEnabled(true);
                            showMessageFailed("Terjadi kesalahan, silahkan coba lagi");
                            e.printStackTrace();
                        }
                    }
                    viewModel.getConfigurationLiveData().removeObservers(this);
                });
            } else
                showMessageFailed("Silahkan pilih outlet terlebih dahulu");
        });
        binding.btnSave.setOnClickListener(view -> {
            if (selectedOutlet != null) {
                showLoading(true);
                viewModel.getConfiguration(true).observe(this, configuration -> {
                    double toleransi = configuration.getToleransi_draft() + 1;
                    if (distance == -1)
                        showMessageFailed("Silahkan cek koordinat anda terlebih dahulu.");
                    else if (distance > toleransi) {
                        showMessageFailed("Koordinat anda tidak valid");
                    } else if (selectedImage == null) {
                        showMessageFailed("Pastikan semua data terisi");
                    } else {
                        view.setEnabled(false);
                        VisitPlanDb visitPlanDb = new VisitPlanDb();
                        visitPlanDb.setId(selectedOutlet.getKd_outlet());
                        visitPlanDb.setOutlet_id(selectedOutlet.getKd_outlet());
                        visitPlanDb.setDate_visit(getCurrentDateFormal());
                        visitPlanDb.setSkip_order_reason("");
                        visitPlanDb.setIf_close(0);
                        visitPlanDb.setKeterangan_if_close("-");
                        visitPlanDb.setIs_checkout(0);
                        visitPlanDb.setGambar(selectedImage.toString());
                        visitPlanDb.setCreated_at("");
                        viewModel.saveVisit(visitPlanDb).observe(this, visitPlanDb1 -> {
                            if(visitPlanDb1 != null) {
                                Calendar calendar = Calendar.getInstance();
                                CheckInTimer checkInTimer = new CheckInTimer();
                                checkInTimer.setRunning(true);
                                checkInTimer.setStartTime(calendar.getTimeInMillis());
                                checkInTimer.setEndTime(calendar.getTimeInMillis() + 10*60*1000);
                                checkInTimer.setRemainingTime(10*60*1000);
                                timerPreference.setTimerPreference(checkInTimer);
                                Intent intent = new Intent(this, CountDownTimerActivity.class);
                                intent.putExtra("data", visitPlanDb1);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    viewModel.getConfigurationLiveData().removeObservers(this);
                });
            } else
                showMessageFailed("Silahkan pilih outlet terlebih dahulu");
        });
        binding.svOutletname.setOnClickListener(view -> selectOutlet("outlet"));
        viewModel.setActivityResultLauncher(activityResultLauncher);
        binding.buttonTake.setOnClickListener(view -> {
            takeCamera();
        });
        initObserver();
    }

    void registerActivityResult(){
        setActivityResultLaunch(registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == SELECT_OUTLET_CODE) {
                        selectedOutlet = (Outlet) result.getData().getSerializableExtra("data");
                        binding.outletInfo.setVisibility(View.VISIBLE);
                        binding.namaOutlet.setText(selectedOutlet.getNm_outlet());
                        binding.svJarak.setText("");
                        binding.svKoordinat.setText("");
                        distance = -1;
                        binding.alamatOutlet.setText(selectedOutlet.getAlmt_outlet());
                        if(viewModel.getTipeLiveData() != null && viewModel.getTipeLiveData().hasActiveObservers()){
                            viewModel.getTipe(selectedOutlet.getKd_tipe());
                        } else {
                            viewModel.getTipe(selectedOutlet.getKd_tipe()).observe(SubmitVisit.this, tipe -> {
                                binding.tipeOutlet.setText(tipe != null ? tipe.getNama() : "-");
                            });
                        }
                        Glide.with(SubmitVisit.this).load(selectedOutlet.getFoto())
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
            if(!status) {
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
                    binding.foto.setImageURI(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessageFailed("Camera failed, please clear some space and try again");
                }
            } else{
                showMessageFailed("Camera failed");
            }
        });
    }

    private void takeCamera(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pastikan anda mengambil foto dalam posisi LANDSCAPE");
        builder.setTitle("Perhatikan !!");
        builder.setPositiveButton("Siap", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(SubmitVisit.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        fotoFile = new File(getFilesDir(), imageFileName);
                        selectedImage = FileProvider.getUriForFile(SubmitVisit.this, getApplicationContext().getPackageName() + ".provider", fotoFile);
                        viewModel.getActivityResultLauncher().launch(selectedImage);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SubmitVisit.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(SubmitVisit.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(SubmitVisit.this, new String[]{Manifest.permission.CAMERA},
                                    1);
                        }
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(SubmitVisit.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(SubmitVisit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        fotoFile = new File(getFilesDir(), imageFileName);
                        selectedImage = FileProvider.getUriForFile(SubmitVisit.this, getApplicationContext().getPackageName() + ".provider", fotoFile);
                        viewModel.getActivityResultLauncher().launch(selectedImage);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SubmitVisit.this, Manifest.permission.CAMERA) &&
                                ActivityCompat.shouldShowRequestPermissionRationale(SubmitVisit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(SubmitVisit.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(SubmitVisit.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        }
                    }
                }
            }
        });
        builder.show();
    }
}