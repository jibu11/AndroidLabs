package com.cst2335.lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear); // Set this to load your desired layout

        Button button = findViewById(R.id.button);
        Switch switch1 = findViewById(R.id.switch1);

        // Button Click Listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });

        // Switch CheckedChangeListener
        switch1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            String switchState = isChecked ? getString(R.string.switch_on) : getString(R.string.switch_off);
            Snackbar snackbar = Snackbar.make(compoundButton, getString(R.string.switch_is_now) + " " + switchState, Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> compoundButton.setChecked(!isChecked));
            snackbar.show();
        });
    }
}
