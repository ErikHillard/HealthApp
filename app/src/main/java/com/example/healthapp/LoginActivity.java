package com.example.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        TextView username = findViewById(R.id.usernameInput);
//        TextView password = findViewById(R.id.passwordInput);
//
//        MaterialButton loginButton = (MaterialButton) findViewById(R.id.loginbutton);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (username.getText().toString().equals("user")
//                        && password.getText().toString().equals("pass")){
//                    Intent switchActivity = new Intent(LoginActivity.this, MainActivity.class);
//                    switchActivity.putExtra("Username", username.getText().toString());
//                    switchActivity.putExtra("NewUser", false);
//                    startActivity(switchActivity);
//                } else {
//                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_login_container, LoginFragment.class, null)
                    .commit();
        }
    }
}