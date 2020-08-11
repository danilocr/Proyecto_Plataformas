package com.example.proyecto.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto.Controller.MapActivity;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;

public class Inicio extends AppCompatActivity {

    Button buttonTrazarRuta;
    Button buttonMirarRuta;
    Button buttoncerrarSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        buttonTrazarRuta = (Button) findViewById(R.id.buttonTrazarRuta);
        buttonMirarRuta = (Button) findViewById(R.id.buttonHistorialRuta);
        buttoncerrarSesion = (Button) findViewById(R.id.buttoncerrarSesion);

        buttonTrazarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, MapActivity.class);
                startActivity(intent);
            }
        });

        buttonMirarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, HistorialRuta.class);
                startActivity(intent);
            }
        });

        buttoncerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("usuario", "");
                editor.putString("id", "");
                editor.apply();
                Toast.makeText(Inicio.this,"Cerrando Sesión",Toast.LENGTH_LONG).show();
                firebase.mAuth.signOut();
                onBackPressed();

            }
        });


    }
}