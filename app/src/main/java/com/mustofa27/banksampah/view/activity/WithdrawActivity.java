package com.mustofa27.banksampah.view.activity;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityWithdrawBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.Balance;
import com.mustofa27.banksampah.model.entity.WithdrawOption;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.SavingViewModel;
import com.mustofa27.banksampah.viewmodel.WithdrawViewModel;

import java.util.Objects;

public class WithdrawActivity extends BaseActivity {
    ActivityWithdrawBinding binding;
    WithdrawViewModel viewModel;
    WithdrawOption selected;
    Balance saldo;
    int WITHDRAWOPTION_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(WithdrawViewModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            saldo = getIntent().getSerializableExtra("saldo", Balance.class);
        } else{
            saldo = (Balance) getIntent().getSerializableExtra("saldo");
        }
        binding.opsi.setOnClickListener(v -> {
            Intent intent = new Intent(WithdrawActivity.this, WithdrawOptionActivity.class);
            intent.putExtra("request_code", WITHDRAWOPTION_CODE);
            getActivityResultLaunch().launch(intent);
        });
        binding.topbar.title.setText("Penarikan Saldo");
        binding.topbar.back.setOnClickListener(v -> finish());
        binding.saldo.setText(getMoneyFormat(saldo.getBalance()));
        binding.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    binding.estimasi.setText(getMoneyFormat(Integer.valueOf(s.toString()) * selected.getPrice()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.submit.setOnClickListener(v -> viewModel.addWithdraw(Integer.valueOf(binding.count.getText().toString()),
                Integer.valueOf(binding.count.getText().toString())*selected.getPrice(), selected.getId()));
        setActivityResult();
        initObserver();
    }

    void setActivityResult(){
        setActivityResultLaunch(registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData().getIntExtra("request_code", 0) == WITHDRAWOPTION_CODE) {
                        selected = (WithdrawOption) result.getData().getSerializableExtra("data");
                        initViewOption();
                    }
                }));
    }
    void initViewOption(){
        binding.containerKontenJenis.setVisibility(VISIBLE);
        Glide.with(this).load(ConnectionHandler.IMAGE_URL + selected.getImage_path()).
                placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(binding.imageview);
        binding.name.setText(selected.getName());
        binding.description.setText(selected.getDescription());
        binding.stock.setText("Stok: " + selected.getStock());
        binding.price.setText(getMoneyFormat(selected.getPrice()));
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
            if(status) {
                finish();
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
    }
}