package com.mustofa27.banksampah.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityDetailCatalogBinding;
import com.mustofa27.banksampah.model.entity.Brand;
import com.mustofa27.banksampah.model.entity.Catalog;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.BannerCatalogAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CatalogViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;

import java.util.ArrayList;

public class DetailCatalogActivity extends BaseActivity {

    ActivityDetailCatalogBinding binding;
    CatalogViewModel viewModel;
    Brand current;
    ArrayList<Catalog> catalogs;
    BannerCatalogAdapter adapter;
    private DownloadManager downloadManager;
    private Uri downloadUri;
    private BroadcastReceiver onDownloadComplete;
    private long downloadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("Catalog Detail");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(CatalogViewModel.class);
        current = (Brand) getIntent().getSerializableExtra("data");
        catalogs = new ArrayList<>();
        adapter = new BannerCatalogAdapter(this, catalogs);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.download.setOnClickListener(view -> askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101));
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadID == id) {
                    openDownloadedAttachment(DetailCatalogActivity.this, downloadID);
                }
            }
        };
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        initObserver();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            downloadData();
        } else {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                downloadData();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showMessageSuccess("This app need to access your external storage");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    private void downloadData(){
        Toast.makeText(this, "Downloading image", Toast.LENGTH_LONG).show();
        for (Catalog data: catalogs) {
            String fileName = data.getPath_image();
            //Log.d("Trace", "onClick: "+PATH_CATALOG+fileName);

            downloadUri = Uri.parse(fileName);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("Downloading " + "Catalog_" + current.getName() + "/" + data.getNama() + ".png");
            request.setDescription("Downloading " + "Catalog_" + current.getName() + "/" + data.getNama() + ".png");
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/PMPSales/" + "Catalog_" + current.getName() + "/" + data.getNama() + ".png");
            if(catalogs.indexOf(data) == catalogs.size()-1){
                downloadID = downloadManager.enqueue(request);
            } else{
                downloadManager.enqueue(request);
            }
        }
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
        viewModel.getAllCatalog(current.getId()).observe(this, catalogs -> {
            if(this.catalogs.size() > 0){
                this.catalogs.removeAll(this.catalogs);
            }
            this.catalogs.addAll(catalogs);
            adapter.notifyDataSetChanged();
            viewModel.getLoading().setValue(false);
        });
    }
}