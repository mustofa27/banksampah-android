package com.mustofa27.banksampah.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityWithdrawOptionBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.Garbage;
import com.mustofa27.banksampah.model.entity.WithdrawOption;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.WithdrawOptionViewModel;
import com.mustofa27.banksampah.viewmodel.WithdrawViewModel;

import java.util.ArrayList;

public class WithdrawOptionActivity extends BaseActivity {
    ActivityWithdrawOptionBinding binding;
    WithdrawOptionViewModel viewModel;
    ArrayList<WithdrawOption> withdrawOptions;
    GenericRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        withdrawOptions = new ArrayList<>();
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(WithdrawOptionViewModel.class);
        adapter = new GenericRecyclerAdapter(withdrawOptions, R.layout.item_option, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                WithdrawOption tmp = (WithdrawOption) object;
                TextView name = view.findViewById(R.id.name);
                ImageView imageView = view.findViewById(R.id.imageview);
                TextView price = view.findViewById(R.id.price);
                TextView desc = view.findViewById(R.id.description);
                TextView stock = view.findViewById(R.id.stock);
                name.setText(tmp.getName());
                desc.setText(tmp.getDescription());
                stock.setText("Stok: " + tmp.getStock());
                price.setText(getMoneyFormat(tmp.getPrice()));
                Glide.with(WithdrawOptionActivity.this).load(ConnectionHandler.IMAGE_URL + tmp.getImage_path()).
                        placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(imageView);
                view.findViewById(R.id.select).setOnClickListener(this.onClickItem(tmp));
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return v -> {
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("data", (WithdrawOption) object);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                };
            }
        });
        binding.topbar.title.setText("Opsi Penarikan Saldo");
        binding.topbar.back.setOnClickListener(v -> {
            Intent returnIntent = getIntent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        });
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new GridLayoutManager(this, 2));
        initObserver();
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
        viewModel.getData().observe(this, withdrawOptions -> {
            viewModel.getLoading().setValue(false);
            if(this.withdrawOptions.size() > 0){
                this.withdrawOptions.removeAll(this.withdrawOptions);
            }
            this.withdrawOptions.addAll(withdrawOptions);
            adapter.notifyDataSetChanged();
        });
    }
}