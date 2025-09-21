package com.mustofa27.banksampah.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityOutletDetailBinding;
import com.mustofa27.banksampah.model.entity.Outlet;


public class OutletDetail extends AppCompatActivity {

    ActivityOutletDetailBinding binding;
    Outlet current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutletDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("DETAIL OUTLET");
        ((LinearLayout) getSupportActionBar().getCustomView().findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        current = (Outlet) getIntent().getSerializableExtra("data");
        if(current != null){
            binding.outKode.setText(binding.outKode.getText().toString()+" "+current.getKode());
            binding.outNama.setText(binding.outNama.getText().toString()+" "+current.getNm_outlet());
            binding.outAlamat.setText(binding.outAlamat.getText().toString()+" "+current.getAlmt_outlet());
            binding.outRank.setText(binding.outRank.getText().toString()+" "+current.getRank_outlet());
            binding.outTelp.setText(binding.outTelp.getText().toString()+" "+current.getKodepos());
            binding.outNmPIC.setText(binding.outNmPIC.getText().toString() + " " + current.getNm_pic());
            binding.outTlpPIC.setText(binding.outTlpPIC.getText().toString()+" "+current.getTlp_pic());
            binding.outKota.setText(binding.outKota.getText().toString()+" "+(current.getCity() ==  null ? "" : " - " +
                    current.getCity().getNm_kota()));
            binding.outTipe.setText(binding.outTipe.getText().toString()+" "+current.getTipe().getNama());
            Glide.with(this)
                    .load(current.getFoto())
                    .into(binding.outFoto);
        }
    }
}