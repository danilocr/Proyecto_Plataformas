package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.proyecto.Vista.HistorialActivity;
import com.example.proyecto.Vista.Maps.MapActivity;

public class MainActivity extends AppCompatActivity  {

    TextView tvMensaje;
    private static final long MIN_TIME = 10000;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset=0;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startServiceButton = (Button)findViewById(R.id.buttonIngresar);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        Button historialButton = (Button)findViewById(R.id.buttonHistorial);
        historialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });

    }
}