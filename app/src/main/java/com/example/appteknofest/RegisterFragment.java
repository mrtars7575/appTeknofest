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


public class RegisterFragment extends Fragment {

    private Button registerBtn;
    private ImageView btnBack;
    private TextView registerEmail;
    private TextView registerPassword;
    private FirebaseAuth mAuth;

    public RegisterFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerBtn = view.findViewById(R.id.register);
        btnBack = view.findViewById(R.id.backForRegisterIv);
        registerEmail = view.findViewById(R.id.registerEmail);
        registerPassword = view.findViewById(R.id.registerPassword);
        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        //register btn click
        registerBtn.setOnClickListener(view1 -> register(view1));
        // back arrow btn click
        btnBack.setOnClickListener(view2 -> goToIntroductionFragment(view2));
    }
    //register to firebase
    //in addition go to login fragment
    //also send data (string)"register" with bundle
    //In this way, the login fragment will understand
    //that it came from the register fragment.
    public void register(View view){

        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("argument","register");
                    //go to login fragment
                    Navigation.findNavController(view)
                            .navigate(R.id.action_registerFragment_to_logInFragment,bundle);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                });
    }
    //go to introduction fragment

    public void goToIntroductionFragment(View view){

        Navigation.findNavController(view)
                .navigate(R.id.action_registerFragment_to_introductionragment);
    }

}