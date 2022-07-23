package com.example.appteknofest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainFragment extends Fragment {

    ImageView cameraBtn;
    ImageView diagnosticBtn;
    ImageView communicationBtn;
    ImageView deviceBtn;
    ImageView appBtn;
    ImageView othersBtn;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraBtn =  view.findViewById(R.id.cameraIv);
        diagnosticBtn =  view.findViewById(R.id.diagnosticIv);
        communicationBtn =  view.findViewById(R.id.communicationIv);
        deviceBtn =  view.findViewById(R.id.deviceSettingsIv);
        appBtn = view.findViewById(R.id.appSettingsIv);
        othersBtn = view.findViewById(R.id.othersIv);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCameraFragment(view);
            }
        });
        diagnosticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDiagnosticFragment(view);
            }
        });
        communicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCommunicationFragment(view);
            }
        });
        deviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDeviceSettingsFragment(view);
            }
        });
        appBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAppSettingsFragment(view);
            }
        });
        othersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOthersFragment(view);
            }
        });

    }

    public void goToCameraFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_cameraFragment);
    }

    public void goToDiagnosticFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_diagnosticFragment);
    }

    public void goToCommunicationFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_communicationFragment);
    }

    public void goToDeviceSettingsFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_deviceSettingsFragment);
    }

    public void goToAppSettingsFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_appSettingsFragment);
    }

    public void goToOthersFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_mainFragment_to_othersFragment);
    }
}