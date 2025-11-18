package com.mustofa27.banksampah.view.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.ActivityCartBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.Balance;
import com.mustofa27.banksampah.model.entity.Cart;
import com.mustofa27.banksampah.model.entity.Product;
import com.mustofa27.banksampah.model.helper.CustomActivityHelper;
import com.mustofa27.banksampah.view.BaseActivity;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CartViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.WithdrawViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    CartViewModel viewModel;
    boolean submitFlag = false;
    GenericRecyclerAdapter adapter;
    ArrayList<Cart> carts, selected;
    long total = 0;
    int totalPoint = 0;
    long totalDiscount = 0;
    long balanceUsed = 0;
    Balance balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new CustomViewModelFactory(this)).get(CartViewModel.class);
        carts = new ArrayList<>();
        selected = new ArrayList<>();
        binding.topbar.title.setText("Keranjang");
        binding.topbar.back.setOnClickListener(v -> finish());
        adapter = new GenericRecyclerAdapter(carts, R.layout.item_cart, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Cart tmp = (Cart) object;
                ImageView imageView = view.findViewById(R.id.imageview);
                TextView productName = view.findViewById(R.id.product_name);
                TextView productPrice = view.findViewById(R.id.product_price);
                TextView productPriceDiscount = view.findViewById(R.id.product_price_discount);
                TextView minus = view.findViewById(R.id.minus);
                TextView plus = view.findViewById(R.id.plus);
                EditText jumlah = view.findViewById(R.id.jumlah);
                Glide.with(CartActivity.this).load(ConnectionHandler.IMAGE_URL + tmp.getProduct().getImage_path()).
                        placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(imageView);
                productName.setText(tmp.getProduct().getName());
                if(tmp.getProduct().getValidDiscount() != null){
                    productPrice.setVisibility(VISIBLE);
                    productPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    productPrice.setText(getMoneyFormat(tmp.getProduct().getPrice()));
                    productPriceDiscount.setText(getMoneyFormat((100-tmp.getProduct().getValidDiscount().getPercentage()) * tmp.getProduct().getPrice()/100));
                } else{
                    productPrice.setVisibility(GONE);
                    productPriceDiscount.setText(getMoneyFormat(tmp.getProduct().getPrice()));
                }
                CheckBox checkBox = view.findViewById(R.id.checkbox);
                checkBox.setChecked(tmp.isSelected());
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    tmp.setSelected(isChecked);
                    if(!isChecked){
                        selected.remove(tmp);
                        total -= tmp.getSubtotal();
                        totalPoint -= tmp.getSubtotal_point();
                        totalDiscount -= tmp.getSubtotal_discount();
                    } else{
                        selected.add(tmp);
                        total += tmp.getSubtotal();
                        totalPoint += tmp.getSubtotal_point();
                        totalDiscount += tmp.getSubtotal_discount();
                    }
                    countTotal();
                });
                plus.setOnClickListener(view1 -> {
                    tmp.setCount(tmp.getCount()+1);
                    jumlah.setText("" + tmp.getCount());
                    tmp.countTotal();
                });
                minus.setOnClickListener(view1 -> {
                    tmp.setCount(tmp.getCount() > 0 ? tmp.getCount()-1 : 0);
                    jumlah.setText("" + tmp.getCount());
                    tmp.countTotal();
                });
                jumlah.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(isStringNotEmpty(s.toString())) {
                            tmp.setCount(Integer.valueOf(s.toString()));
                        } else{
                            tmp.setCount(0);
                        }
                        if (tmp.isSelected()) {
                            long total_bef = tmp.getSubtotal();
                            int total_poin_bef = tmp.getSubtotal_point();
                            long total_discount_bef = tmp.getSubtotal_discount();
                            tmp.countTotal();
                            total += (tmp.getSubtotal() - total_bef);
                            totalPoint += (tmp.getSubtotal_point() - total_poin_bef);
                            totalDiscount += (tmp.getSubtotal_discount() - total_discount_bef);
                        }
                        countTotal();
                    }
                });
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return null;
            }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setAdapter(adapter);
        binding.checkout.setOnClickListener(v -> {
            if(!selected.isEmpty() && total != 0) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("total_price", total);
                    jsonObject.put("total_point", totalPoint);
                    jsonObject.put("total_discount", totalDiscount);
                    jsonObject.put("balance_used", balanceUsed);
                    JSONArray orders = new JSONArray();
                    for (Cart cart : selected) {
                        JSONObject order = new JSONObject();
                        order.put("product_id", cart.getProduct_id());
                        order.put("quantity", cart.getCount());
                        order.put("subtotal_price", cart.getSubtotal());
                        order.put("subtotal_discount", cart.getSubtotal_discount());
                        order.put("subtotal_point", cart.getSubtotal_point());
                        orders.put(order);
                    }
                    jsonObject.put("orders", orders);
                    submitFlag = true;
                    viewModel.checkout(jsonObject).observe(CartActivity.this, transaction -> {
                        finish();
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else{
                showMessageFailed("Minimal harus memilih 1 produk");
            }
        });
        binding.checkSaldo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            balanceUsed = (isChecked ? balance.getBalance() : 0);
            countTotal();
        });
        initObserver();
    }
    private void countTotal(){
        binding.total.setText(getMoneyFormat(binding.checkSaldo.isChecked() ? total-balanceUsed : total));
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
            if(!status || submitFlag) {
                showMessage(status);
                submitFlag = false;
            }
        });
        viewModel.getLoading().observe(this, loading -> {
            showLoading(loading);
        });
        viewModel.getCart().observe(this, cartItems -> {
            carts.removeAll(carts);
            carts.addAll(cartItems);
            adapter.notifyDataSetChanged();
        });
        viewModel.getBalance().observe(this, balance -> {
            this.balance = balance;
            binding.saldo.setText("Saldo Tersedia: "+getMoneyFormat(balance.getBalance()));
            binding.checkSaldo.setVisibility(balance.getBalance() > 0 ? VISIBLE : GONE);
        });
    }
}