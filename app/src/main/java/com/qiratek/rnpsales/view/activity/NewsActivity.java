package com.qiratek.rnpsales.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.ActivityNewsBinding;
import com.qiratek.rnpsales.model.datasource.network.ConnectionHandler;
import com.qiratek.rnpsales.model.entity.NewsClass;
import com.qiratek.rnpsales.view.BaseActivity;
import com.qiratek.rnpsales.view.adapter.AdapterCallback;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.NewsViewModel;

import java.util.ArrayList;

public class NewsActivity extends BaseActivity {

    ArrayList<NewsClass> newsClasses;
    GenericRecyclerAdapter adapter;
    ActivityNewsBinding binding;
    NewsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_topbar);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("News & Promos");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        newsClasses = new ArrayList<>();
        adapter = new GenericRecyclerAdapter(newsClasses, R.layout.news_list_item, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                NewsClass current = (NewsClass) object;
                ImageView imNews = view.findViewById(R.id.im_news);
                TextView headline = view.findViewById(R.id.headline);
                headline.setText(current.getJudul());
                TextView tgl = view.findViewById(R.id.news_date);
                tgl.setText(current.getCreated_at() == null ? "-" : getDate(current.getCreated_at()));
                ProgressBar loading = view.findViewById(R.id.loading);
                loading.setVisibility(View.VISIBLE);
                Glide.with(NewsActivity.this)
                        .load(current.getImage())
                        .error(R.drawable.no_banner_image)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                loading.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                loading.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imNews);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {
                    Intent intent = new Intent(NewsActivity.this, NewsDetail.class);
                    intent.putExtra("data", (NewsClass) object);
                    startActivity(intent);
                };
            }
        });
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(NewsViewModel.class);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
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
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getAllNews().observe(this, newsClasses -> {
            if(this.newsClasses.size() > 0){
                this.newsClasses.removeAll(this.newsClasses);
            }
            this.newsClasses.addAll(newsClasses);
            adapter.notifyDataSetChanged();
        });
    }
}