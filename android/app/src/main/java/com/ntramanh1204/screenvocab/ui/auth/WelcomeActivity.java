package com.ntramanh1204.screenvocab.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnSignUp;
    private Button btnContinueGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnContinueGuest = findViewById(R.id.btn_continue_guest);
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(v -> {
            // TODO: Navigate to Sign Up screen
            Toast.makeText(this, "Sign Up clicked - Will implement next", Toast.LENGTH_SHORT).show();
        });

        btnContinueGuest.setOnClickListener(v -> {
            // TODO: Navigate to main app as guest
            Toast.makeText(this, "Continue as Guest clicked - Will implement next", Toast.LENGTH_SHORT).show();
        });
    }
}