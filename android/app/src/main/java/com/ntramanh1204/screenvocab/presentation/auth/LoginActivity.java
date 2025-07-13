package com.ntramanh1204.screenvocab.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.domain.model.User;
import com.ntramanh1204.screenvocab.presentation.dashboard.DashboardActivity;
import com.ntramanh1204.screenvocab.core.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private TextView tvSignUpLink, tvForgotPassword;

    private LoginViewModel loginViewModel;
    private boolean emailFieldTouched = false;
    private boolean passwordFieldTouched = false;
    private boolean formSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViewModel();
        initViews();
        setupObservers();
        setupFocusListeners();
        setupTextWatchers();
        setupClickListeners();
    }

    private void initViewModel() {
        AuthViewModelFactory factory = new AuthViewModelFactory();
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }

    private void setupObservers() {
        loginViewModel.getIsLoading().observe(this, this::showLoadingState);
        loginViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
        loginViewModel.getLoginSuccess().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }
        });
        loginViewModel.getPasswordResetSuccess().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message + "\nPlease check your spam folder if not received.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initViews() {
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        progressBar = findViewById(R.id.progress_bar);
        tvSignUpLink = findViewById(R.id.tv_sign_up_link);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void setupFocusListeners() {
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailFieldTouched = true;
                validateEmail();
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordFieldTouched = true;
                validatePassword();
            }
        });
    }

    private void setupTextWatchers() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (emailFieldTouched || formSubmitted) {
                    validateEmail();
                }
                updateSignInButtonState();
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordFieldTouched || formSubmitted) {
                    validatePassword();
                }
                updateSignInButtonState();
            }
        });
    }

    private void setupClickListeners() {
        btnSignIn.setOnClickListener(v -> attemptLogin());
        tvSignUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void validateEmail() {
        String email = etEmail.getText().toString().trim();
        if (ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(null);
        } else {
            tilEmail.setError(getString(R.string.error_invalid_email));
        }
    }

    private void validatePassword() {
        String password = etPassword.getText().toString();
        if (password.length() > 0) {
            tilPassword.setError(null);
        } else {
            tilPassword.setError(getString(R.string.error_field_required));
        }
    }

    private void updateSignInButtonState() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        boolean isValid = ValidationUtils.isValidEmail(email) && password.length() > 0;
        btnSignIn.setEnabled(isValid);
    }

    private void validateAllFields() {
        formSubmitted = true;
        emailFieldTouched = true;
        passwordFieldTouched = true;
        validateEmail();
        validatePassword();
    }

    private void attemptLogin() {
        loginViewModel.clearError();
        validateAllFields();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (!ValidationUtils.isValidEmail(email) || password.length() == 0) {
            Toast.makeText(this, "Please fix the errors above", Toast.LENGTH_SHORT).show();
            return;
        }
        loginViewModel.login(email, password);
    }

    private void handleForgotPassword() {
        String email = etEmail.getText().toString().trim();
        if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        loginViewModel.clearError();
        loginViewModel.sendPasswordResetEmail(email);
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            btnSignIn.setEnabled(false);
            btnSignIn.setText("");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setEnabled(true);
            btnSignIn.setText(getString(R.string.sign_in));
            progressBar.setVisibility(View.GONE);
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}