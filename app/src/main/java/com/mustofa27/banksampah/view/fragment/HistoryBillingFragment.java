package com.mustofa27.banksampah.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentHistoryBillingBinding;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryBillingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryBillingFragment extends BaseFragment {

    FragmentHistoryBillingBinding binding;
    GenericRecyclerAdapter adapter;
//    ArrayList<BillingData> billingData;
//    HistoryViewModel viewModel;

    public HistoryBillingFragment() {
        // Required empty public constructor
    }


    public static HistoryBillingFragment newInstance() {
        HistoryBillingFragment fragment = new HistoryBillingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        billingData = new ArrayList<>();
//        adapter = new GenericRecyclerAdapter(billingData, R.layout.history_billing_item, new AdapterCallback() {
//            @Override
//            public void bindView(View view, Object object) {
//                BillingData current = (BillingData) object;
//                TextView outletName = view.findViewById(R.id.tv_outlet);
//                TextView note = view.findViewById(R.id.tv_note);
//                TextView input_date = view.findViewById(R.id.tv_input_date);
//                TextView cash = view.findViewById(R.id.tv_cash);
//                TextView transfer = view.findViewById(R.id.tv_transfer);
//                TextView nominal_giro = view.findViewById(R.id.tv_nominal_giro);
//                TextView no_giro = view.findViewById(R.id.tv_no_giro);
//                TextView due_date = view.findViewById(R.id.tv_due_date);
//                TextView bank = view.findViewById(R.id.tv_bank);
//                outletName.setText(current.getOutlets() != null ? current.getOutlets().getNm_outlet() : "Outlet telah terhapus");
//                note.setText(current.getNote());
//                input_date.setText(current.getCreated_at() == null ? "-" : getDate(current.getCreated_at()));
//
//                if (current.getCash_value() >= 0) {
//                    cash.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getCash_value()));
//                } else {
//                    cash.setVisibility(View.GONE);
//                }
//
//                if (current.getTransfer_value() >= 0) {
//                    transfer.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getTransfer_value()));
//                } else {
//                    transfer.setVisibility(View.GONE);
//                }
//
//                if (current.getNominal_giro() > 0) {
//                    view.findViewById(R.id.ll_container_giro).setVisibility(View.VISIBLE);
//                    nominal_giro.setText(" Rp. " + NumberFormat.getNumberInstance(Locale.US).format(current.getNominal_giro()));
//                    no_giro.setText(current.getNomor_giro());
//                    due_date.setText(getDate(current.getDue_date()));
//                } else {
//                    view.findViewById(R.id.ll_container_giro).setVisibility(View.GONE);
//                }
//
//                if (current.getNominal_giro() > 0 || current.getTransfer_value() > 0) {
//                    bank.setText(current.getBank_nm());
//                } else {
//                    bank.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public View.OnClickListener onClickItem(Object object) {
//                return null;
//            }
//        });
//        viewModel = new ViewModelProvider(getParentFragment(), new CustomViewModelFactory(getActivity())).get(HistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBillingBinding.inflate(getLayoutInflater());
        binding.rvHistoryBilling.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvHistoryBilling.setAdapter(adapter);
        binding.rvHistoryBilling.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initObserver();
        return binding.getRoot();
    }

    @Override
    protected BaseViewModel getViewModel() {
//        return viewModel;
        return null;
    }

    @Override
    protected void showLoading(boolean isLoading) {
        binding.loading.getRoot().setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
//        viewModel.getStatus().observe(getActivity(), status -> {
//            viewModel.getLoading().setValue(false);
//            if(!status) {
//                showMessage(status);
//            }
//        });
//        viewModel.getLoading().observe(getActivity(), loading -> {
//            showLoading(loading);
//        });
//
//        viewModel.getBillingData().observe(getActivity(), billingData1 -> {
//            if(billingData.size() > 0){
//                billingData.removeAll(billingData);
//            }
//            billingData.addAll(billingData1);
//            adapter.notifyDataSetChanged();
//            binding.noData.getRoot().setVisibility(billingData1.size() == 0 ? View.VISIBLE : View.GONE);
//        });
    }
}