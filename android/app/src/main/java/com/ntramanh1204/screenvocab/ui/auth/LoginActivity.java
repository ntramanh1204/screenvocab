package com.ntramanh1204.screenvocab.ui.auth;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private TextView tvSignUpLink, tvForgotPassword;

    // Firebase
    private FirebaseAuth firebaseAuth;

    // Field touched tracking
    private boolean emailFieldTouched = false;
    private boolean passwordFieldTouched = false;
    private boolean formSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        initViews();
        setupFocusListeners();
        setupTextWatchers();
        setupClickListeners();
    }

    private void initViews() {
        // TextInputLayouts (for error display)
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);

        // EditTexts (for user input)
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        // Buttons and other views
        btnSignIn = findViewById(R.id.btn_sign_in);
        progressBar = findViewById(R.id.progress_bar);
        tvSignUpLink = findViewById(R.id.tv_sign_up_link);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void setupFocusListeners() {
        // Track when user focuses and blurs each field
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
        // Email field validation
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailFieldTouched || formSubmitted) {
                    validateEmail();
                }
                updateSignInButtonState();
            }
        });

        // Password field validation
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
            // Navigate to Sign Up screen
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
        // Enable button if both fields have content and email is valid
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        boolean isValid = ValidationUtils.isValidEmail(email) && password.length() > 0;
        btnSignIn.setEnabled(isValid);
    }

    private void validateAllFields() {
        // Mark all fields as touched and validate
        formSubmitted = true;
        emailFieldTouched = true;
        passwordFieldTouched = true;

        validateEmail();
        validatePassword();
    }

    private void attemptLogin() {
        // Validate all fields before submission
        validateAllFields();

        // Get form data
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Check if form is valid
        if (!ValidationUtils.isValidEmail(email) || password.length() == 0) {
            Toast.makeText(this, "Please fix the errors above", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        showLoadingState(true);

        // Attempt Firebase login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            showLoadingState(false);
                            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

                            // Navigate to Dashboard
                            Intent intent = new Intent(this, com.ntramanh1204.screenvocab.ui.dashboard.DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Login failed
                        showLoadingState(false);
                        handleLoginError(task.getException());
                    }
                });
    }

    private void handleForgotPassword() {
        String email = etEmail.getText().toString().trim();

        if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        showLoadingState(true);

        // Send password reset email with custom settings
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showLoadingState(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.password_reset_sent)
                                + "\nPlease check your spam folder if not received.", Toast.LENGTH_LONG).show();
                    } else {
                        handlePasswordResetError(task.getException());
                    }
                });
    }

    private void handlePasswordResetError(Exception exception) {
        String errorMessage = getString(R.string.password_reset_error);

        if (exception != null) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage != null) {
                if (exceptionMessage.contains("user-not-found")) {
                    errorMessage = "No account found with this email address";
                } else if (exceptionMessage.contains("invalid-email")) {
                    errorMessage = "Invalid email address";
                }
            }
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
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

    private void handleLoginError(Exception exception) {
        String errorMessage = getString(R.string.error_network_error);

        if (exception != null) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage.contains("There is no user record")
                    || exceptionMessage.contains("user-not-found")) {
                errorMessage = getString(R.string.error_user_not_found);
            } else if (exceptionMessage.contains("password is invalid")
                    || exceptionMessage.contains("wrong-password")) {
                errorMessage = getString(R.string.error_wrong_password);
            } else if (exceptionMessage.contains("user-disabled")) {
                errorMessage = getString(R.string.error_user_disabled);
            } else if (exceptionMessage.contains("too-many-requests")) {
                errorMessage = getString(R.string.error_too_many_requests);
            } else if (exceptionMessage.contains("invalid-email")
                    || exceptionMessage.contains("invalid-credential")
                    || exceptionMessage.contains("INVALID_LOGIN_CREDENTIALS")) {
                errorMessage = getString(R.string.error_invalid_credentials);
            } else if (exceptionMessage.contains("network")
                    || exceptionMessage.contains("timeout")) {
                errorMessage = getString(R.string.error_network_error);
            } else {
                // For debugging - show actual error message
                errorMessage = getString(R.string.error_invalid_credentials);
            }
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
