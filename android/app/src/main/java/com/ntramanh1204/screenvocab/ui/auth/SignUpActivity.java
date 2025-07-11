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
import com.google.firebase.firestore.FirebaseFirestore;

import com.ntramanh1204.screenvocab.R;
import com.ntramanh1204.screenvocab.data.models.User;
import com.ntramanh1204.screenvocab.utils.Constants;
import com.ntramanh1204.screenvocab.utils.ValidationUtils;

public class SignUpActivity extends AppCompatActivity {
    
    // UI Components
    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private TextView tvSignInLink;
    
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    
    // Field touched tracking
    private boolean nameFieldTouched = false;
    private boolean emailFieldTouched = false;
    private boolean passwordFieldTouched = false;
    private boolean confirmPasswordFieldTouched = false;
    private boolean formSubmitted = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        
        initViews();
        setupFocusListeners();
        setupTextWatchers();
        setupClickListeners();
    }
    
    private void initViews() {
        // TextInputLayouts (for error display)
        tilFullName = findViewById(R.id.til_full_name);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        
        // EditTexts (for user input)
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        
        // Buttons and other views
        btnSignUp = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progress_bar);
        tvSignInLink = findViewById(R.id.tv_sign_in_link);
    }
    
    private void setupFocusListeners() {
        // Track when user focuses and blurs each field
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
        // Only validate touched fields as user types
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
                // Always validate confirm password if it has content
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
            // Navigate to Login screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
    
    // Functional interface for validation
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
        // Only enable button if all fields are valid
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        
        boolean isFormValid = ValidationUtils.isFormValid(name, email, password, confirmPassword);
        btnSignUp.setEnabled(isFormValid);
    }
    
    private void validateAllFields() {
        // Mark all fields as touched and validate
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
        // Validate all fields before submission
        validateAllFields();
        
        // Get form data
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        
        // Check if form is valid
        if (!ValidationUtils.isFormValid(name, email, password, etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Please fix the errors above", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show loading state
        showLoadingState(true);
        
        // Create Firebase user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign up success
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        createUserProfile(firebaseUser.getUid(), name, email);
                    }
                } else {
                    // Sign up failed
                    showLoadingState(false);
                    handleSignUpError(task.getException());
                }
            });
    }
    
    private void createUserProfile(String userId, String displayName, String email) {
        // Create user model
        User user = new User(userId, displayName, email, false);
        
        // Save to Firestore
        firestore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .set(user)
            .addOnSuccessListener(aVoid -> {
                showLoadingState(false);
                Toast.makeText(this, getString(R.string.success_account_created), Toast.LENGTH_LONG).show();
                
                Intent intent = new Intent(this, com.ntramanh1204.screenvocab.ui.dashboard.DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .addOnFailureListener(e -> {
                showLoadingState(false);
                Toast.makeText(this, "Error creating profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
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
    
    private void handleSignUpError(Exception exception) {
        String errorMessage = getString(R.string.error_network_error);
        
        if (exception != null) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage != null) {
                if (exceptionMessage.contains("email address is already in use")) {
                    errorMessage = getString(R.string.error_email_already_exists);
                } else if (exceptionMessage.contains("weak password")) {
                    errorMessage = getString(R.string.error_weak_password);
                }
            }
        }
        
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}