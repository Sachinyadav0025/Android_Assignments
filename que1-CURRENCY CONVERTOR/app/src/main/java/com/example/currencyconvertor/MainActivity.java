package com.example.currencyconvertor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etFrom, etTo;
    private Spinner spinnerFrom, spinnerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load Theme BEFORE super.onCreate
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("DarkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        Button btnConvert = findViewById(R.id.btnConvert);
        ImageView btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnSwap = findViewById(R.id.btnSwap);

        // Setup Spinners with the correct array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        spinnerTo.setSelection(1); // Default to second item

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnConvert.setOnClickListener(v -> performConversion());

        btnSwap.setOnClickListener(v -> {
            int fromPos = spinnerFrom.getSelectedItemPosition();
            spinnerFrom.setSelection(spinnerTo.getSelectedItemPosition());
            spinnerTo.setSelection(fromPos);
            performConversion();
        });
    }

    private void performConversion() {
        if (spinnerFrom.getSelectedItem() == null || spinnerTo.getSelectedItem() == null) return;
        
        String fromItem = spinnerFrom.getSelectedItem().toString();
        String toItem = spinnerTo.getSelectedItem().toString();
        String input = etFrom.getText().toString();

        if (input.isEmpty()) {
            etTo.setText("");
            return;
        }

        try {
            double amount = Double.parseDouble(input);
            double result = CurrencyUtils.convert(amount, fromItem, toItem);
            etTo.setText(String.format(Locale.getDefault(), "%.2f", result));
        } catch (NumberFormatException e) {
            etTo.setText("Error");
        }
    }
}