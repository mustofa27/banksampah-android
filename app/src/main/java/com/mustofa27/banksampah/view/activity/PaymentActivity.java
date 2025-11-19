package com.mustofa27.banksampah.view.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityPaymentBinding;
import com.mustofa27.banksampah.model.datasource.network.MultipartFile;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistoryTransactionViewModel;
import com.mustofa27.banksampah.viewmodel.PaymentViewModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends BaseActivity {
    ActivityPaymentBinding binding;
    PaymentViewModel viewModel;
    Transaction transaction;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(PaymentViewModel.class);
        transaction = (Transaction) getIntent().getSerializableExtra("data");
        binding.topbar.title.setText("Pembayaran");
        binding.topbar.back.setOnClickListener(v -> finish());
        // Registers a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                selectedImage = uri;
                binding.foto.setImageURI(uri);
            }
        });
        initView();
        initObserver();
    }
    private void initView(){
        if(transaction == null){
            showMessageFailed("Transaksi tidak dikenali");
            finish();
        } else{
            binding.nominal.setText(getMoneyFormat(transaction.getTotal_price()+transaction.getUnique_code()-transaction.getBalance_used()));
            binding.salinNominal.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("nominal", String.valueOf(transaction.getTotal_price()+transaction.getUnique_code()-transaction.getBalance_used()));
                clipboard.setPrimaryClip(clip);
            });
            binding.salin.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("rekening", "741701044050536");
                clipboard.setPrimaryClip(clip);
            });
            binding.buttonTake.setOnClickListener(v -> {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            });
            binding.btnSubmit.setOnClickListener(v -> {
                if(selectedImage == null){
                    showMessageFailed("Silahkan pilih bukti transfer terlebih dahulu");
                } else{
                    try {
                        Map<String, Object> param = new HashMap<>();
                        param.put("id", transaction.getId());
                        Map<String, MultipartFile> paramFile = new HashMap<>();
                        paramFile.put("image", new MultipartFile("bukti transfer", selectedImage, this));
                        viewModel.pay(param,paramFile).observe(this, transaction1 -> {
                            finish();
                        });
                    } catch (IOException e) {
                        showMessageFailed("Terjadi kesalahan dalam membaca file");
                    }
                }
            });
        }
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        showDefaulLoading(isLoading);
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(this, status -> {
            viewModel.getLoading().setValue(false);
            showMessage(status);
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }
}