package com.mustofa27.banksampah.view.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentHistorySavingBinding;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.entity.Transaction;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistorySavingViewModel;
import com.mustofa27.banksampah.viewmodel.HistoryTransactionViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryTransactionFragment extends BaseFragment implements View.OnClickListener {
    FragmentHistorySavingBinding binding;
    ArrayList<Transaction> transactions, filteredTransactions;
    GenericRecyclerAdapter adapter;

    String status = "semua";
    ImageView prev;
    View prevStatus;
    TextView prevTxt;
    boolean isLoadingNext = false;
    HistoryTransactionViewModel viewModel;
    public HistoryTransactionFragment() {
        // Required empty public constructor
    }


    public static HistoryTransactionFragment newInstance() {
        HistoryTransactionFragment fragment = new HistoryTransactionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getParentFragment(), new CustomViewModelFactory(getContext())).get(HistoryTransactionViewModel.class);
        transactions = new ArrayList<>();
        filteredTransactions = new ArrayList<>();
        adapter = new GenericRecyclerAdapter(filteredTransactions, R.layout.saving_item_list, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Transaction transaction = (Transaction) object;
                TextView name = view.findViewById(R.id.name);
                TextView weight = view.findViewById(R.id.weight);
                TextView tanggal = view.findViewById(R.id.date);
                TextView status = view.findViewById(R.id.status);
                TextView message = view.findViewById(R.id.message);
                tanggal.setText(isStringNotEmpty(transaction.getCreated_at()) ? getDate(transaction.getCreated_at()) : "-");
                weight.setText(getMoneyFormat(transaction.getTotal_price()));
                name.setText("Transaksi-"+transaction.getId()+"-"+transaction.getUnique_code());
                ProcessStatus(status, transaction, message);
            }

            @Override
            public View.OnClickListener onClickItem(Object object) {
                return view -> {

                };
            }
        });
        adapter.setFilter(new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                filteredTransactions.removeAll(filteredTransactions);
                if (charString.equalsIgnoreCase("semua")) {
                    filteredTransactions.addAll(transactions);
                } else {
                    ArrayList<Transaction> filteredList = new ArrayList<>();
                    int status = charString.equalsIgnoreCase("menunggu") ? 0 :
                            (charString.equalsIgnoreCase("diterima") ? 1 : (charString.equalsIgnoreCase("selesai") ? 2 :-1));
                    for (Transaction row : transactions) {
                        if (row.getStatus() == status) {
                            filteredList.add(row);
                        }
                    }
                    filteredTransactions.addAll(filteredList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTransactions;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistorySavingBinding.inflate(getLayoutInflater(), container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.list.setAdapter(adapter);
        binding.status1.setOnClickListener(this);
        binding.status2.setOnClickListener(this);
        binding.status3.setOnClickListener(this);
        binding.status4.setOnClickListener(this);
        binding.status5.setOnClickListener(this);
        binding.status5.setVisibility(VISIBLE);
        binding.status1.setActivated(true);
        binding.txtStatus1.setActivated(true);
        binding.icon1.setVisibility(VISIBLE);
        prevStatus = binding.status1;
        prev = binding.icon1;
        prevTxt = binding.txtStatus1;
        initObserver();
        return binding.getRoot();
    }

    void ProcessStatus(TextView textView, Transaction transaction, TextView message){
        if(transaction.getStatus() == 1){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_accepted));
            textView.setTextColor(getActivity().getColor(R.color.success_1));
            message.setVisibility(GONE);
        } else if(transaction.getStatus() == 0){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_menunggu));
            textView.setTextColor(getActivity().getColor(R.color.info_1));
            message.setVisibility(VISIBLE);
        } else if(transaction.getStatus() == -1){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_reject));
            textView.setTextColor(getActivity().getColor(R.color.red_3));
            message.setVisibility(GONE);
        } else if(transaction.getStatus() == 2){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_accepted));
            textView.setTextColor(getActivity().getColor(R.color.white));
            message.setVisibility(GONE);
        }
        textView.setText(transaction.getStatusText());
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
            binding.loadingNext.setVisibility(GONE);
            if(!status) {
                showMessage(status);
            }
        });
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            showLoading(loading);
        });
        viewModel.getData().observe(getViewLifecycleOwner(), transactions -> {
            viewModel.getLoading().setValue(false);
            this.transactions.removeAll(this.transactions);
            this.transactions.addAll(transactions);
            for (Transaction transaction : transactions) {
                if(transaction.getStatusText().equalsIgnoreCase(status) || status.equalsIgnoreCase("semua")){
                    filteredTransactions.add(transaction);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onClick(View v) {
        v.setActivated(true);
        prev.setVisibility(GONE);
        prevStatus.setActivated(false);
        prevTxt.setActivated(false);
        prevStatus = v;
        switch (v.getId()){
            case R.id.status_1:
                status = "semua";
                binding.icon1.setVisibility(VISIBLE);
                prev = binding.icon1;
                binding.txtStatus1.setActivated(true);
                prevTxt = binding.txtStatus1;
                break;
            case R.id.status_2:
                status = "menunggu";
                binding.icon2.setVisibility(VISIBLE);
                prev = binding.icon2;
                binding.txtStatus2.setActivated(true);
                prevTxt = binding.txtStatus2;
                break;
            case R.id.status_3:
                status = "diterima";
                binding.icon3.setVisibility(VISIBLE);
                prev = binding.icon3;
                binding.txtStatus3.setActivated(true);
                prevTxt = binding.txtStatus3;
                break;
            case R.id.status_4:
                status = "ditolak";
                binding.icon4.setVisibility(VISIBLE);
                prev = binding.icon4;
                binding.txtStatus4.setActivated(true);
                prevTxt = binding.txtStatus4;
                break;
            case R.id.status_5:
                status = "selesai";
                binding.icon5.setVisibility(VISIBLE);
                prev = binding.icon5;
                binding.txtStatus5.setActivated(true);
                prevTxt = binding.txtStatus5;
                break;
        }
        adapter.getFilter().filter(status);
    }
}