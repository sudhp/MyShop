package com.examp.myshop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {

    public SigninFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAccount;
    private FrameLayout parentFrameLayout;

    private EditText email;
    private EditText password;
    private TextView forgotPassword;

    private ProgressBar progressBar;
    private ImageButton closeButton;
    private Button signinButton;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    FirebaseAuth firebaseAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        dontHaveAccount = view.findViewById(R.id.tv_dont_hav_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);
        closeButton = view.findViewById(R.id.sign_in_close_btn);
        signinButton = view.findViewById(R.id.sign_in_button);
        progressBar = view.findViewById(R.id.sing_in_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPassword = view.findViewById(R.id.sign_in_forgot_password);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ResetPasswordFragment());
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinButton.setEnabled(true);
                signinButton.setTextColor(Color.rgb(255,255,255));
            }else{

            }
        }else{

        }
    }

    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(password.length() >= 8){

                progressBar.setVisibility(View.VISIBLE);
                signinButton.setEnabled(false);
                signinButton.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mainIntent();
                                }else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signinButton.setEnabled(true);
                                    signinButton.setTextColor(Color.rgb(255,255,255));

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(),"Incorrect Email or Password!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getActivity(),"Incorrect Email or Password!",Toast.LENGTH_LONG).show();
        }
    }

    private void mainIntent(){
        Intent mainIntent = new Intent(getActivity(), Main2Activity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
