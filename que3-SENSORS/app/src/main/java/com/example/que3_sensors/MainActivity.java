package com.example.que3_sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, lightSensor, proximitySensor;
    private TextView accelText, lightText, proxText, statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        accelText = findViewById(R.id.accelData);
        lightText = findViewById(R.id.lightData);
        proxText = findViewById(R.id.proxData);
        statusText = findViewById(R.id.proxStatus);

        // Initialize Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            // Define Sensors
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelText.setText(String.format(Locale.getDefault(), "X: %.2f\nY: %.2f\nZ: %.2f", 
                    event.values[0], event.values[1], event.values[2]));
        }
        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightText.setText(String.format(Locale.getDefault(), "Intensity: %.2f lx", event.values[0]));
        }
        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            proxText.setText(String.format(Locale.getDefault(), "Distance: %.2f cm", distance));

            // Logic for the NEAR/FAR status indicator
            if (distance < proximitySensor.getMaximumRange()) {
                statusText.setText("STATUS: NEAR ●");
                statusText.setTextColor(ContextCompat.getColor(this, R.color.prox_green));
            } else {
                statusText.setText("STATUS: FAR ○");
                statusText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            if (accelerometer != null)
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            if (lightSensor != null)
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (proximitySensor != null)
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}