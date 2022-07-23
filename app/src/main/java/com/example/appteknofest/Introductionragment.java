package com.example.appteknofest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Introductionragment extends Fragment {

    private Button loginBtn;
    private Button registerBtn;


    public Introductionragment() {
       
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_introductionragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginBtn = view.findViewById(R.id.loginBtn);
        registerBtn = view.findViewById(R.id.registerBtn);


        // login btn click
        loginBtn.setOnClickListener(view1 -> goToLoginFragment(view1));
        // register btn click
        registerBtn.setOnClickListener(view2 -> goToRegisterFragment(view2));

    }
    // go to login fragment
    public void goToLoginFragment(View view){


        //pass data between destinations with bundle
        //to find out where the login fragment came from

        Bundle bundle = new Bundle();
        bundle.putString("argument","introduction");

        // go to login fragment
        Navigation.findNavController(view)
                .navigate(R.id.action_introductionragment_to_logInFragment,bundle);


    }
    // go to register fragment
    public void goToRegisterFragment(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_introductionragment_to_registerFragment);
    }


}