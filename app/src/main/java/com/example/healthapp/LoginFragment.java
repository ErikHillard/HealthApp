package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

//    public static LoginFragment newInstance(String param1, String param2) {
//        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView username = view.findViewById(R.id.usernameInput);
        TextView password = view.findViewById(R.id.passwordInput);

        MaterialButton loginButton = (MaterialButton) view.findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("user")
                        && password.getText().toString().equals("pass")){
                    Intent switchActivity = new Intent(getActivity(), MainActivity.class);
                    switchActivity.putExtra("Username", username.getText().toString());
                    switchActivity.putExtra("NewUser", false);
                    startActivity(switchActivity);
                } else {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });

        MaterialButton registerNavButton = (MaterialButton) view.findViewById(R.id.registerNavButton);

        registerNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);

                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_login_container, newFragment);


                transaction.commit();
            }
        });
    }
}