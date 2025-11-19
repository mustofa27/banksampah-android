package com.mustofa27.banksampah.view.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentProductBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.activity.CartActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistorySavingViewModel;
import com.mustofa27.banksampah.viewmodel.ProductViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends BaseFragment {

    FragmentProductBinding binding;
    ProductViewModel viewModel;
    GenericRecyclerAdapter productAdapter;
    ArrayList<Product> products;
    boolean isLoadingNext = false;
    boolean addToCartFlag = false;
    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(getContext())).get(ProductViewModel.class);
        products = new ArrayList<>();
        productAdapter = new GenericRecyclerAdapter(products, R.layout.item_product, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Product tmp = (Product) object;
                ImageView imageView = view.findViewById(R.id.imageview);
                TextView productName = view.findViewById(R.id.product_name);
                TextView productPrice = view.findViewById(R.id.product_price);
                TextView productPriceDiscount = view.findViewById(R.id.product_price_discount);
                TextView productPriceHanya = view.findViewById(R.id.product_price_title);
                Glide.with(getContext()).load(ConnectionHandler.IMAGE_URL + tmp.getImage_path()).
                        placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(imageView);
                productName.setText(tmp.getName());
                if(tmp.getValidDiscount() != null){
                    productPrice.setVisibility(View.VISIBLE);
                    productPriceHanya.setVisibility(View.VISIBLE);
                    productPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    productPrice.setText(getMoneyFormat(tmp.getPrice()));
                    productPriceDiscount.setText(getMoneyFormat((100-tmp.getValidDiscount().getPercentage()) * tmp.getPrice()/100));
                } else{
                    productPrice.setVisibility(View.GONE);
                    productPriceHanya.setVisibility(View.GONE);
                    productPriceDiscount.setText(getMoneyFormat(tmp.getPrice()));
                }
                view.findViewById(R.id.add_to_cart).setOnClickListener(this.onClickItem(tmp));
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return v -> {
                    addToCartFlag = true;
                    viewModel.addToCart(((Product) object).getId());
                };
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(getLayoutInflater(), container, false);
        binding.list.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.list.setAdapter(productAdapter);
        binding.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int pastVisiblesItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if(!isLoadingNext && visibleItemCount + pastVisiblesItems >= totalItemCount){
                        if(viewModel.isNextAvailable()) {
                            isLoadingNext = true;
                            binding.loadingNext.setVisibility(View.VISIBLE);
                            viewModel.getNextData();
                        }
                    }
                }
            }
        });
        binding.cartContainer.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        if(isLoading)
            showLoadingDialog();
        else
            dismissLoadingDialog();
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            viewModel.getLoading().setValue(false);
            binding.loadingNext.setVisibility(View.GONE);
            if(!status && !viewModel.isNextPage() || addToCartFlag) {
                showMessage(status);
                if(addToCartFlag){
                    viewModel.getCart();
                }
                addToCartFlag = false;
            } else{
                if(viewModel.isNextPage()){
                    viewModel.setPage(viewModel.getPage()+1);
                }
            }
            if(viewModel.isNextPage()){
                isLoadingNext = false;
            }
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            showLoading(loading);
        });
        viewModel.getProduct().observe(getViewLifecycleOwner(), products -> {
            viewModel.getLoading().setValue(false);
            int tmp = this.products.size();
            if(tmp > 0 && !viewModel.isNextPage()){
                this.products.removeAll(this.products);
            }
            this.products.addAll(products);
            if(!viewModel.isNextPage()) {
                productAdapter.notifyDataSetChanged();
            } else{
                productAdapter.notifyItemInserted(tmp);
            }
        });
        viewModel.getCart().observe(getActivity(), cartItems -> {
            binding.indicator.setText("" + cartItems.size());
            binding.indicator.setVisibility(cartItems.size() > 0 ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getCart();
    }
}