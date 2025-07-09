package com.ntramanh1204.screenvocab;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ntramanh1204.screenvocab.ui.auth.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        // For now, always start with Welcome screen
        // Later we'll add logic to check if user is already logged in
        startActivity(new Intent(this, WelcomeActivity.class));
        finish(); // Close MainActivity so user can't go back to it
    
    }
}