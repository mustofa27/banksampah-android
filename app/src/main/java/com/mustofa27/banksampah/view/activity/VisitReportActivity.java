package com.mustofa27.banksampah.view.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityVisitReportBinding;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.VisitPlanDb;
import com.mustofa27.banksampah.model.entity.VisitReport;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.VisitReportViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisitReportActivity extends BaseActivity {

    ActivityVisitReportBinding binding;
    int indikator = 0;
    VisitReportViewModel viewModel;
    File fotoFile1, fotoFile2, fotoFile3;
    Uri selectedImage1, selectedImage2, selectedImage3;
    VisitPlanDb visitPlanDb;
    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        viewModel.getStatusCamera().setValue(result);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitReportBinding.inflate(getLayoutInflater());
        visitPlanDb = (VisitPlanDb) getIntent().getSerializableExtra("data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_no_icon);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("VISIT REPORT");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setVisibility(View.GONE);
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(VisitReportViewModel.class);
        binding.tpTakephoto.setOnClickListener(view -> {
            indikator = 1;
            takeCamera();
        });
        binding.tpTakephoto1.setOnClickListener(view -> {
            indikator = 2;
            takeCamera();
        });
        binding.tpTakephoto2.setOnClickListener(view -> {
            indikator = 3;
            takeCamera();
        });
        binding.btnSubmit.setOnClickListener(view -> {
            if(selectedImage1 != null || selectedImage2 != null || selectedImage3 != null) {
                if(isStringNotEmpty(visitPlanDb.getCreated_at())) {
                    try {
                        Map<String, Object> param = new HashMap<>();
                        Map<String, MultipartFile> paramFile = new HashMap<>();
                        param.put("outlet_id", visitPlanDb.getOutlet_id());
                        param.put("note", isStringNotEmpty(binding.note) ? binding.note.getText().toString() : "-");
                        if (selectedImage1 != null) {
                            paramFile.put("path_image_1", new MultipartFile("foto1.jpeg", selectedImage1, this));
                        }
                        if (selectedImage2 != null) {
                            paramFile.put("path_image_2", new MultipartFile("foto2.jpeg", selectedImage2, this));
                        }
                        if (selectedImage3 != null) {
                            paramFile.put("path_image_3", new MultipartFile("foto3.jpeg", selectedImage3, this));
                        }
                        viewModel.setVisitBukti(param, paramFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else{
                    VisitReport visitReport = new VisitReport();
                    visitReport.setId(visitPlanDb.getOutlet_id());
                    visitReport.setOutlet_id(visitPlanDb.getOutlet_id());
                    visitReport.setNote(isStringNotEmpty(binding.note) ? binding.note.getText().toString() : "-");
                    visitReport.setPath_image_1(selectedImage1 == null ? "" : selectedImage1.toString());
                    visitReport.setPath_image_2(selectedImage2 == null ? "" : selectedImage2.toString());
                    visitReport.setPath_image_3(selectedImage3 == null ? "" : selectedImage3.toString());
                    viewModel.saveVisitBukti(visitReport);
                }
            } else{
                showMessageFailed("Pastikan semua isian terisi");
            }
        });
        viewModel.setActivityResultLauncher(activityResultLauncher);
        initObserver();
    }

    private void takeCamera(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pastikan anda mengambil foto dalam posisi LANDSCAPE");
        builder.setTitle("Perhatikan !!");
        builder.setPositiveButton("Siap", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(VisitReportActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        if (indikator == 1) {
                            fotoFile1 = new File(getFilesDir(), imageFileName);
                            selectedImage1 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile1);
                            viewModel.getActivityResultLauncher().launch(selectedImage1);
                        } else if (indikator == 2) {
                            fotoFile2 = new File(getFilesDir(), imageFileName);
                            selectedImage2 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile2);
                            viewModel.getActivityResultLauncher().launch(selectedImage2);
                        } else {
                            fotoFile3 = new File(getFilesDir(), imageFileName);
                            selectedImage3 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile3);
                            viewModel.getActivityResultLauncher().launch(selectedImage3);
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(VisitReportActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(VisitReportActivity.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(VisitReportActivity.this, new String[]{Manifest.permission.CAMERA},
                                    1);
                        }
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(VisitReportActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(VisitReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        if (indikator == 1) {
                            fotoFile1 = new File(getFilesDir(), imageFileName);
                            selectedImage1 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile1);
                            viewModel.getActivityResultLauncher().launch(selectedImage1);
                        } else if (indikator == 2) {
                            fotoFile2 = new File(getFilesDir(), imageFileName);
                            selectedImage2 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile2);
                            viewModel.getActivityResultLauncher().launch(selectedImage2);
                        } else {
                            fotoFile3 = new File(getFilesDir(), imageFileName);
                            selectedImage3 = FileProvider.getUriForFile(VisitReportActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile3);
                            viewModel.getActivityResultLauncher().launch(selectedImage3);
                        }
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(VisitReportActivity.this, Manifest.permission.CAMERA) &&
                                ActivityCompat.shouldShowRequestPermissionRationale(VisitReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(VisitReportActivity.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(VisitReportActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        }
                    }
                }
            }
        });
        builder.show();
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
            } else{
                showMessageSuccess("Visit Report Sukses");
                Intent intent = new Intent(VisitReportActivity.this, CountDownTimerActivity.class);
                intent.putExtra("data", visitPlanDb);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getStatusCamera().observe(this, status -> {
            if(status) {
                if(indikator == 1) {
                    try {
                        selectedImage1 = getResizedImage(this, selectedImage1, fotoFile1.getName());
                        binding.tpTakephoto.setImageURI(selectedImage1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessageFailed("Camera failed, please clear some space and try again");
                    }
                } else if(indikator == 2) {
                    try {
                        selectedImage2 = getResizedImage(this, selectedImage2, fotoFile2.getName());
                        binding.tpTakephoto1.setImageURI(selectedImage2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessageFailed("Camera failed, please clear some space and try again");
                    }
                } else {
                    try {
                        selectedImage3 = getResizedImage(this, selectedImage3, fotoFile3.getName());
                        binding.tpTakephoto2.setImageURI(selectedImage3);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessageFailed("Camera failed, please clear some space and try again");
                    }
                }
            } else{
                showMessageFailed("Camera failed");
            }
        });

        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }
}