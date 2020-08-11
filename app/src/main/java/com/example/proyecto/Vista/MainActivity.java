package com.example.proyecto.Vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.Controller.Login;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;


public class MainActivity extends AppCompatActivity {

    Button botonLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase.inicializarFirebase();

        botonLista = (Button) findViewById(R.id.buttonComenzar);
        botonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getFromSharedPreferences("id");
                if(id.equals("")){
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, Inicio.class);
                    startActivity(intent);
                }

            }
        });
    }

    private String getFromSharedPreferences(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        return sharedPreferences.getString(id,"");
    }

}