package com.mustofa27.banksampah.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityNewsDetailBinding;
import com.mustofa27.banksampah.model.entity.NewsClass;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;


public class NewsDetail extends BaseActivity {
    ActivityNewsDetailBinding binding;
    NewsClass newsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("NEWS DETAIL");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        newsClass = (NewsClass) getIntent().getSerializableExtra("data");
        binding.judul.setText(newsClass.getJudul());
        binding.headline.setText(newsClass.getHeadline());
        if(newsClass.getHeadline().equalsIgnoreCase("-")){
            binding.headline.setVisibility(View.GONE);
        } else{
            binding.headline.setVisibility(View.VISIBLE);
        }
        binding.date.setText(newsClass.getCreated_at() == null ? "-" : getDate(newsClass.getCreated_at()));
        binding.content.setText(isStringNotEmpty(newsClass.getContent()) ? Html.fromHtml(newsClass.getContent()) : "-");
        Glide.with(this)
                .load(newsClass.getImage())
                .error(R.drawable.no_banner_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.imNews);
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