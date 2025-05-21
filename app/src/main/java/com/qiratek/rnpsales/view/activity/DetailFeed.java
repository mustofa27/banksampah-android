package com.qiratek.rnpsales.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityDetailFeedBinding;
import com.qiratek.rnpsales.model.entity.FeedOutlet;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;

public class DetailFeed extends BaseActivity {
    ActivityDetailFeedBinding binding;
    FeedOutlet data;
    BottomSheetBehavior behavior;
    float slide = 0;
    float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("Detail Feed");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(v -> onBackPressed());
        data = (FeedOutlet) getIntent().getSerializableExtra("data");
        Glide.with(this).load(data.getGambar1()).
                placeholder(R.drawable.no_banner_image).error(R.drawable.no_banner_image).into(binding.imageview);
        binding.tpOutletname.setText(data.getOutlets().getNm_outlet());
        binding.tpDate.setText(getDate(data.getDate_upload()));
        binding.tpNote.setText(data.getNote());
        behavior = BottomSheetBehavior.from(binding.bottomSheet);
        height = binding.bottomSheet.getHeight()/100;
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float selisih = slideOffset-slide;
                if(selisih > 0.01) {
                    behavior.setPeekHeight(behavior.getPeekHeight() + (int) (selisih * height), true);
                }
                else {
                    behavior.setPeekHeight(behavior.getPeekHeight() - (int) (selisih * height), true);
                }
            }
        });
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected void showLoading(boolean isLoading) {

    }

    @Override
    protected void initObserver() {

    }
}