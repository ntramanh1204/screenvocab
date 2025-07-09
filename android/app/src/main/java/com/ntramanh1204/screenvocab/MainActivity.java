package com.ntramanh1204.screenvocab;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ntramanh1204.screenvocab.ui.auth.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // User is already logged in, navigate to Dashboard
            // TODO: Navigate to Dashboard
            // For now, just show welcome screen
            startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            // User is not logged in, show welcome screen
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        finish(); // Close MainActivity so user can't go back to it
    }
}