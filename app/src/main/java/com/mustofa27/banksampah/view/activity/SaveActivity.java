package com.mustofa27.banksampah.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivitySaveBinding;


public class SaveActivity extends AppCompatActivity {
    ActivitySaveBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}