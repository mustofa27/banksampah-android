package com.mustofa27.banksampah.view.fragment;

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

import com.bumptech.glide.Glide;
import com.mustofa27.banksampah.R;
import com.mustofa27.banksampah.databinding.FragmentHistorySavingBinding;
import com.mustofa27.banksampah.model.datasource.network.ConnectionHandler;
import com.mustofa27.banksampah.model.entity.Saving;
import com.mustofa27.banksampah.model.entity.Withdraw;
import com.mustofa27.banksampah.view.BaseFragment;
import com.mustofa27.banksampah.view.adapter.AdapterCallback;
import com.mustofa27.banksampah.view.adapter.GenericRecyclerAdapter;
import com.mustofa27.banksampah.viewmodel.BaseViewModel;
import com.mustofa27.banksampah.viewmodel.CustomViewModelFactory;
import com.mustofa27.banksampah.viewmodel.HistorySavingViewModel;
import com.mustofa27.banksampah.viewmodel.HistoryWithdrawViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryWithdrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryWithdrawFragment extends BaseFragment implements View.OnClickListener {
    FragmentHistorySavingBinding binding;
    ArrayList<Withdraw> withdraws, filteredwithdraws;
    GenericRecyclerAdapter adapter;

    String status = "semua";
    ImageView prev;
    View prevStatus;
    TextView prevTxt;
    boolean isLoadingNext = false;
    HistoryWithdrawViewModel viewModel;
    public HistoryWithdrawFragment() {
        // Required empty public constructor
    }


    public static HistoryWithdrawFragment newInstance() {
        HistoryWithdrawFragment fragment = new HistoryWithdrawFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getParentFragment(), new CustomViewModelFactory(getContext())).get(HistoryWithdrawViewModel.class);
        withdraws = new ArrayList<>();
        filteredwithdraws = new ArrayList<>();
        adapter = new GenericRecyclerAdapter(filteredwithdraws, R.layout.withdraw_item_list, new AdapterCallback() {
            @Override
            public void bindView(View view, Object object) {
                Withdraw withdraw = (Withdraw) object;
                ImageView imageView = view.findViewById(R.id.imageview);
                TextView name = view.findViewById(R.id.name);
                TextView weight = view.findViewById(R.id.weight);
                TextView tanggal = view.findViewById(R.id.date);
                TextView status = view.findViewById(R.id.status);
                tanggal.setText(isStringNotEmpty(withdraw.getCreated_at()) ? getDate(withdraw.getCreated_at()) : "-");
                weight.setText(withdraw.getCount() + " pcs ~ " + getMoneyFormat(withdraw.getBalance_used()));
                name.setText(withdraw.getSelected_option().getName());
                Glide.with(getContext()).load(ConnectionHandler.IMAGE_URL + withdraw.getSelected_option().getImage_path()).
                        placeholder(R.drawable.icons8_no_image).error(R.drawable.icons8_no_image).centerCrop().into(imageView);
                ProcessStatus(status, withdraw);
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
                filteredwithdraws.removeAll(filteredwithdraws);
                if (charString.equalsIgnoreCase("semua")) {
                    filteredwithdraws.addAll(withdraws);
                } else {
                    ArrayList<Withdraw> filteredList = new ArrayList<>();
                    int status = charString.equalsIgnoreCase("menunggu") ? 0 : (charString.equalsIgnoreCase("diterima") ? 1 : -1);
                    for (Withdraw row : withdraws) {
                        if (row.getStatus() == status) {
                            filteredList.add(row);
                        }
                    }
                    filteredwithdraws.addAll(filteredList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredwithdraws;
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
        binding.status1.setOnClickListener(this);
        binding.status2.setOnClickListener(this);
        binding.status3.setOnClickListener(this);
        binding.status4.setOnClickListener(this);
        binding.status1.setActivated(true);
        binding.txtStatus1.setActivated(true);
        binding.icon1.setVisibility(View.VISIBLE);
        prevStatus = binding.status1;
        prev = binding.icon1;
        prevTxt = binding.txtStatus1;
        initObserver();
        return binding.getRoot();
    }

    void ProcessStatus(TextView textView, Withdraw withdraw){
        if(withdraw.getStatus() == 1){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_accepted));
            textView.setTextColor(getActivity().getColor(R.color.success_1));
        } else if(withdraw.getStatus() == 0){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_menunggu));
            textView.setTextColor(getActivity().getColor(R.color.info_1));
        } else if(withdraw.getStatus() == -1){
            textView.setBackground(getActivity().getDrawable(R.drawable.bg_saving_reject));
            textView.setTextColor(getActivity().getColor(R.color.red_3));
        }
        textView.setText(withdraw.getStatusText());
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
            if(!status && !viewModel.isNextPage()) {
                showMessage(status);
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
        viewModel.getWithdraw().observe(getViewLifecycleOwner(), withdraws -> {
            viewModel.getLoading().setValue(false);
            int tmp = this.withdraws.size();
            if(tmp > 0 && !viewModel.isNextPage()){
                this.withdraws.removeAll(this.withdraws);
            }
            this.withdraws.addAll(withdraws);
            for (Withdraw withdraw : withdraws) {
                if(withdraw.getStatusText().equalsIgnoreCase(status) || status.equalsIgnoreCase("semua")){
                    filteredwithdraws.add(withdraw);
                }
            }
            if(!viewModel.isNextPage()) {
                adapter.notifyDataSetChanged();
            } else{
                adapter.notifyItemInserted(tmp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        v.setActivated(true);
        prev.setVisibility(View.GONE);
        prevStatus.setActivated(false);
        prevTxt.setActivated(false);
        prevStatus = v;
        switch (v.getId()){
            case R.id.status_1:
                status = "semua";
                binding.icon1.setVisibility(View.VISIBLE);
                prev = binding.icon1;
                binding.txtStatus1.setActivated(true);
                prevTxt = binding.txtStatus1;
                break;
            case R.id.status_2:
                status = "menunggu";
                binding.icon2.setVisibility(View.VISIBLE);
                prev = binding.icon2;
                binding.txtStatus2.setActivated(true);
                prevTxt = binding.txtStatus2;
                break;
            case R.id.status_3:
                status = "diterima";
                binding.icon3.setVisibility(View.VISIBLE);
                prev = binding.icon3;
                binding.txtStatus3.setActivated(true);
                prevTxt = binding.txtStatus3;
                break;
            case R.id.status_4:
                status = "ditolak";
                binding.icon4.setVisibility(View.VISIBLE);
                prev = binding.icon4;
                binding.txtStatus4.setActivated(true);
                prevTxt = binding.txtStatus4;
                break;
        }
        adapter.getFilter().filter(status);
    }
}