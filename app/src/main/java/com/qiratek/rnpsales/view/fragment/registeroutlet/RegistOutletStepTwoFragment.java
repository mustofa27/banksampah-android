package com.qiratek.rnpsales.view.fragment.registeroutlet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.qiratek.rnpsales.R;
import com.qiratek.rnpsales.databinding.FragmentRegistOutletStepTwoBinding;
import com.qiratek.rnpsales.model.datasource.network.MultipartFile;
import com.qiratek.rnpsales.model.entity.Tipe;
import com.qiratek.rnpsales.view.BaseFragment;
import com.qiratek.rnpsales.viewmodel.BaseViewModel;
import com.qiratek.rnpsales.viewmodel.CustomViewModelFactory;
import com.qiratek.rnpsales.viewmodel.RegisterOutletViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistOutletStepTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistOutletStepTwoFragment extends BaseFragment {

    RegisterOutletViewModel viewModel;
    FragmentRegistOutletStepTwoBinding binding;
    ArrayAdapter tipeAdapter;
    ArrayList<Tipe> tipes;
    Tipe selectedTipe;
    File fotoFile;


    public RegistOutletStepTwoFragment() {
        // Required empty public constructor
    }

    public static RegistOutletStepTwoFragment newInstance() {
        RegistOutletStepTwoFragment fragment = new RegistOutletStepTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity(), new CustomViewModelFactory(getContext())).get(RegisterOutletViewModel.class);
        tipes = new ArrayList<>();
        tipeAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner, tipes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegistOutletStepTwoBinding.inflate(getLayoutInflater());
        binding.roS2Next.setOnClickListener(v -> {
            if(selectedTipe == null || viewModel.getSelectedImage() == null || !isStringNotEmpty(binding.roOutletphone)){
                showMessageFailed("Pastikan semua sudah terisi");
            } else {
                try {
                    viewModel.getParam().put("tipe_id", selectedTipe.getId());
                    viewModel.getParam().put("kodepos", binding.roOutletphone.getText().toString());
                    Map<String, MultipartFile> paramFile = new HashMap<>();
                    paramFile.put("path_image", new MultipartFile(String.valueOf(viewModel.getParam().get("nama")) + ".jpeg", viewModel.getSelectedImage(), getContext()));
                    viewModel.setFileParam(paramFile);
                    viewModel.getNavController().navigate(R.id.action_step_two_to_step_three);
                } catch (IOException e) {
                    showMessageFailed("File read error");
                    e.printStackTrace();
                }
            }
        });

        binding.buttonTake.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Pastikan anda mengambil foto dalam posisi LANDSCAPE");
            builder.setTitle("Perhatikan !!");
            builder.setPositiveButton("Siap", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                            fotoFile = new File(getActivity().getFilesDir(), imageFileName);
                            viewModel.setSelectedImage(FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider",fotoFile));
                            viewModel.getActivityResultLauncher().launch(viewModel.getSelectedImage());
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                                Toast.makeText(getContext(), "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                                        1);
                            }
                        }
                    } else{
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + ".jpeg";
                            fotoFile = new File(getActivity().getFilesDir(), imageFileName);
                            viewModel.setSelectedImage(FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider",fotoFile));
                            viewModel.getActivityResultLauncher().launch(viewModel.getSelectedImage());
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) &&
                                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(getContext(), "This application need permission to use camera and save the image in storage", Toast.LENGTH_LONG).show();
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        1);
                            }
                        }
                    }
                }
            });
            builder.show();
        });
        binding.roOutlettype.setAdapter(tipeAdapter);
        binding.roOutlettype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTipe = tipes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTipe = null;
            }
        });
        initObserver();
        return binding.getRoot();
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
    protected boolean isValidInput() {
        return false;
    }

    @Override
    protected void initObserver() {
        viewModel.getStatus().observe(getActivity(), status -> {
            viewModel.getLoading().setValue(false);
            if(!status) {
                showMessage(status);
            }
        });

        viewModel.getStatusCamera().observe(getActivity(), status -> {
            if(status) {
                try {
                    viewModel.setSelectedImage(getResizedImage(getContext(), viewModel.getSelectedImage(), fotoFile.getName()));
                    binding.roTakePhoto.setImageURI(viewModel.getSelectedImage());
                } catch (IOException e) {
                    e.printStackTrace();
                    showMessageFailed("Camera failed, please clear some space and try again");
                }
            } else{
                showMessageFailed("Camera failed");
            }
        });

        viewModel.getLoading().observe(getActivity(), loading -> {
            showLoading(loading);
        });

        viewModel.getAllTipe().observe(getActivity(), tipes -> {
            if(this.tipes.size() > 0){
                this.tipes.removeAll(this.tipes);
            }
            this.tipes.addAll(tipes);
            tipeAdapter.notifyDataSetChanged();
        });
    }
}