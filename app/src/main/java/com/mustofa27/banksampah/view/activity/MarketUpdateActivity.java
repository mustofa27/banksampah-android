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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityMarketUpdateBinding;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.Outlet;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.MarketUpdateViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MarketUpdateActivity extends BaseActivity {

    ActivityMarketUpdateBinding binding;
    MarketUpdateViewModel viewModel;
    File fotoFile1;
    Uri selectedImage1;
    Outlet selectedOutlet;
    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        viewModel.getStatusCamera().setValue(result);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarketUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerActivityResult();
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(MarketUpdateViewModel.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.title)).setText(getResources().getString(R.string.takePhoto));
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        ((ImageView)getSupportActionBar().getCustomView().findViewById(R.id.icon_activity)).setImageResource(R.drawable.take_photo_icon);
        binding.buttonTake.setOnClickListener(view -> {
            takeCamera();
        });
        binding.btnSubmit.setOnClickListener(view -> {
            if(selectedOutlet != null && isStringNotEmpty(binding.tpNote) &&
                    (selectedImage1 != null)){
                try {
                    Map<String, Object> param = new HashMap<>();
                    param.put("outlet_id", selectedOutlet.getKd_outlet());
                    param.put("tipe_photo_id", 2);
                    param.put("nama", selectedOutlet.getNm_outlet()+"_"+getDateNow());
                    param.put("note", binding.tpNote.getText().toString());
                    param.put("date_take_photo", getDateNowFormal());
                    Map<String, MultipartFile> paramFile = new HashMap<>();
                    if(selectedImage1 != null) {
                        paramFile.put("img_path1", new MultipartFile(fotoFile1.getName(), selectedImage1, this));
                    }
                    viewModel.submit(param, paramFile);
                } catch (IOException e) {
                    showMessageFailed("Terjadi Kesalahan");
                    e.printStackTrace();
                }
            } else{
                showMessageFailed("Pastikan semua sudah terisi");
            }
        });
        binding.tpDate.setText(getDateNow());
        binding.tpOutletname.setOnClickListener(view -> selectOutlet("outlet"));
        viewModel.setActivityResultLauncher(activityResultLauncher);
        initObserver();
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
            showMessage(status);
            if(status) {
                finish();
            }
        });

        viewModel.getStatusCamera().observe(this, status -> {
            if(status) {
                try {
                    selectedImage1 = getResizedImage(this, selectedImage1, fotoFile1.getName());
                    binding.foto.setImageURI(selectedImage1);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessageFailed("Camera failed, please clear some space and try again");
                }
            } else{
                showMessageFailed("Camera failed");
            }
        });

        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }

    private void takeCamera(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pastikan anda mengambil foto dalam posisi Landscape");
        builder.setTitle("Perhatikan !!");
        builder.setPositiveButton("Siap", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(MarketUpdateActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        fotoFile1 = new File(getFilesDir(), imageFileName);
                        selectedImage1 = FileProvider.getUriForFile(MarketUpdateActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile1);
                        viewModel.getActivityResultLauncher().launch(selectedImage1);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MarketUpdateActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(MarketUpdateActivity.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(MarketUpdateActivity.this, new String[]{Manifest.permission.CAMERA},
                                    1);
                        }
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(MarketUpdateActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MarketUpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                        fotoFile1 = new File(getFilesDir(), imageFileName);
                        selectedImage1 = FileProvider.getUriForFile(MarketUpdateActivity.this, getApplicationContext().getPackageName() + ".provider", fotoFile1);
                        viewModel.getActivityResultLauncher().launch(selectedImage1);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MarketUpdateActivity.this, Manifest.permission.CAMERA) &&
                                ActivityCompat.shouldShowRequestPermissionRationale(MarketUpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(MarketUpdateActivity.this, "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                        } else {
                            ActivityCompat.requestPermissions(MarketUpdateActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        }
                    }
                }
            }
        });
        builder.show();
    }

    void registerActivityResult(){
        setActivityResultLaunch(registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == SELECT_OUTLET_CODE) {
                        selectedOutlet = (Outlet) result.getData().getSerializableExtra("data");
                        binding.tpOutletname.setText(selectedOutlet.getNm_outlet());
                    }
                }));
    }
}