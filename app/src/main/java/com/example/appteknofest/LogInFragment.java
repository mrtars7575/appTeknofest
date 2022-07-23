package com.example.appteknofest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInFragment extends Fragment {

    private Button login;
    private TextView loginTv;
    private ImageView back;
    private TextView loginEmail;
    private TextView loginPassword;
    private FirebaseAuth mAuth;

    public LogInFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginTv =   view.findViewById(R.id.loginTv);
        login = (Button) view.findViewById(R.id.login);
        back = (ImageView) view.findViewById(R.id.backForLoginIv);
        loginEmail =  view.findViewById(R.id.loginEmail);
        loginPassword =  view.findViewById(R.id.loginPassword);
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        // login btn click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(view);
            }
        });
        // back arrow btn click
        back.setOnClickListener(view2 -> backFragment(view2));


    }
    //after login operation,go to main fragment
    public void logIn(View view){

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> Navigation.findNavController(view)
                        .navigate(R.id.action_logInFragment_to_mainFragment))
                .addOnFailureListener(e -> Toast.makeText(getActivity(),e.getLocalizedMessage().toString()
                        ,Toast.LENGTH_LONG).show());

    }

    // for back to fragment but which fragment
    //if coming data equals (string)'introduction',go to  introductionFragment
    //else go to registerFragment
    public void backFragment(View view){
        String getData;
        assert getArguments() != null;
        getData = getArguments().getString("argument");

        if (getData.equals("introduction")){
            Navigation.findNavController(view)
                    .navigate(R.id.action_logInFragment_to_introductionragment);
        }else if(getData.equals("register")){
            Navigation.findNavController(view)
                    .navigate(R.id.action_logInFragment_to_registerFragment);
        }
    }


}