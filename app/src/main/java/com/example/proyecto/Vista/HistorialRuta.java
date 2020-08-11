package com.example.proyecto.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyecto.Controller.historialTrazarRuta;
import com.example.proyecto.Modelo.Ruta;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistorialRuta extends AppCompatActivity {

    private List<String> listRuta = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapterRuta;
    ListView listViewRuta;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ruta);
        listViewRuta = (ListView) findViewById(R.id.listaRuta);
        id = cargarUsuario("id");
        listarDatos();
        listViewRuta.setClickable(true);
        listViewRuta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long iden) {

                Intent intent = new Intent(HistorialRuta.this, historialTrazarRuta.class);
                String nomRutaSelec = listRuta.get(position);
                intent.putExtra("rutaSelec",nomRutaSelec);
                startActivity(intent);
            }
        });
    }

    private String cargarUsuario(String valor) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        return sharedPreferences.getString(valor,"");
    }


    private void listarDatos() {

        firebase.mDatabase.child("users").child(id).child("ruta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listRuta.clear();
                for(DataSnapshot objSnaptshot: dataSnapshot.getChildren()){
                    Ruta aux = objSnaptshot.getValue(Ruta.class);
                    listRuta.add(aux.getNombre());
                    arrayAdapterRuta = new ArrayAdapter<String>(HistorialRuta.this,android.R.layout.simple_list_item_1,listRuta);
                    listViewRuta.setAdapter(arrayAdapterRuta);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}