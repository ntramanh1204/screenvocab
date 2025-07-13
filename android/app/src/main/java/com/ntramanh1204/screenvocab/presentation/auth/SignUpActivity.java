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

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private TextView tvSignInLink;

    private SignUpViewModel signUpViewModel;
    private boolean nameFieldTouched = false;
    private boolean emailFieldTouched = false;
    private boolean passwordFieldTouched = false;
    private boolean confirmPasswordFieldTouched = false;
    private boolean formSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViewModel();
        initViews();
        setupFocusListeners();
        setupTextWatchers();
        setupClickListeners();
        setupObservers();
    }

    private void initViewModel() {
        AuthViewModelFactory factory = new AuthViewModelFactory();
        signUpViewModel = new ViewModelProvider(this, factory).get(SignUpViewModel.class);
    }

    private void setupObservers() {
        signUpViewModel.getIsLoading().observe(this, this::showLoadingState);
        signUpViewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
        signUpViewModel.getSignUpSuccess().observe(this, user -> {
            if (user != null) {
                String name = etFullName.getText().toString().trim();
                signUpViewModel.createUserProfile(user, name);
            }
        });
        signUpViewModel.getProfileCreated().observe(this, created -> {
            if (Boolean.TRUE.equals(created)) {
                Toast.makeText(this, getString(R.string.success_account_created), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        tilFullName = findViewById(R.id.til_full_name);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);

        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnSignUp = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progress_bar);
        tvSignInLink = findViewById(R.id.tv_sign_in_link);
    }

    private void setupFocusListeners() {
        etFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                nameFieldTouched = true;
                validateIndividualField(etFullName, tilFullName, ValidationUtils::isValidName,
                        getString(R.string.error_name_too_short));
            }
        });

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailFieldTouched = true;
                validateIndividualField(etEmail, tilEmail, ValidationUtils::isValidEmail,
                        getString(R.string.error_invalid_email));
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordFieldTouched = true;
                validateIndividualField(etPassword, tilPassword, ValidationUtils::isValidPassword,
                        getString(R.string.error_password_too_short));
            }
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                confirmPasswordFieldTouched = true;
                validatePasswordMatch();
            }
        });
    }

    private void setupTextWatchers() {
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (nameFieldTouched || formSubmitted) {
                    validateIndividualField(etFullName, tilFullName, ValidationUtils::isValidName,
                            getString(R.string.error_name_too_short));
                }
                updateSignUpButtonState();
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (emailFieldTouched || formSubmitted) {
                    validateIndividualField(etEmail, tilEmail, ValidationUtils::isValidEmail,
                            getString(R.string.error_invalid_email));
                }
                updateSignUpButtonState();
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
                    validateIndividualField(etPassword, tilPassword, ValidationUtils::isValidPassword,
                            getString(R.string.error_password_too_short));
                }
                if (confirmPasswordFieldTouched || formSubmitted) {
                    validatePasswordMatch();
                }
                updateSignUpButtonState();
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (confirmPasswordFieldTouched || formSubmitted) {
                    validatePasswordMatch();
                }
                updateSignUpButtonState();
            }
        });
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(v -> attemptSignUp());

        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private interface FieldValidator {
        boolean validate(String input);
    }

    private void validateIndividualField(TextInputEditText editText, TextInputLayout layout,
                                         FieldValidator validator, String errorMessage) {
        String input = editText.getText().toString().trim();
        if (validator.validate(input)) {
            layout.setError(null);
        } else {
            layout.setError(errorMessage);
        }
    }

    private void validatePasswordMatch() {
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (ValidationUtils.doPasswordsMatch(password, confirmPassword)) {
            tilConfirmPassword.setError(null);
        } else {
            tilConfirmPassword.setError(getString(R.string.error_passwords_dont_match));
        }
    }

    private void updateSignUpButtonState() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        boolean isFormValid = ValidationUtils.isFormValid(name, email, password, confirmPassword);
        btnSignUp.setEnabled(isFormValid);
    }

    private void validateAllFields() {
        formSubmitted = true;
        nameFieldTouched = true;
        emailFieldTouched = true;
        passwordFieldTouched = true;
        confirmPasswordFieldTouched = true;

        validateIndividualField(etFullName, tilFullName, ValidationUtils::isValidName,
                getString(R.string.error_name_too_short));
        validateIndividualField(etEmail, tilEmail, ValidationUtils::isValidEmail,
                getString(R.string.error_invalid_email));
        validateIndividualField(etPassword, tilPassword, ValidationUtils::isValidPassword,
                getString(R.string.error_password_too_short));
        validatePasswordMatch();
    }

    private void attemptSignUp() {
        signUpViewModel.clearError();
        validateAllFields();
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        if (!ValidationUtils.isFormValid(name, email, password, etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Please fix the errors above", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpViewModel.signUp(email, password);
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            btnSignUp.setEnabled(false);
            btnSignUp.setText("");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnSignUp.setEnabled(true);
            btnSignUp.setText(getString(R.string.sign_up));
            progressBar.setVisibility(View.GONE);
        }
    }
}