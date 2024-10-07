package com.cst2335.lab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    public static final String PREFS_NAME = "UserPrefs";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);

        // Load saved email from SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        emailInput.setText(savedEmail);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                Intent goToProfile = new Intent(MainActivity.this, Ppactivity.class);
                goToProfile.putExtra("EMAIL", email);
                startActivity(goToProfile);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save email to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", emailInput.getText().toString());
        editor.apply();
    }
}