package com.example.proyecto.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.Vista.Inicio;
import com.example.proyecto.Modelo.Usuario;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button botonLogearse;
    Button botonRegistrase;

    EditText nombreUsuario;
    EditText nombrePassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botonLogearse = (Button) findViewById(R.id.btnIngresar);
        botonRegistrase = (Button) findViewById(R.id.btnRegistrarse);

        nombreUsuario = (EditText) findViewById(R.id.editTextUsuario);
        nombrePassword = (EditText) findViewById(R.id.editTextContra);

        botonLogearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nombre = nombreUsuario.getText().toString();
                        String password = nombrePassword.getText().toString();

                        for(DataSnapshot objSnaptshot: dataSnapshot.getChildren()){
                            Usuario user = objSnaptshot.getValue(Usuario.class);
                            if(nombre.equals(user.getEmail()) && password.equals(user.getPassword())){
                                String id = objSnaptshot.getKey().toString();
                                saveLoginSharedPreferences(nombre,id);
                                firebase.mAuth.signInWithEmailAndPassword(nombre,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(Login.this, Inicio.class);
                                            startActivity(intent);
                                            Toast.makeText(Login.this,"Iniciando sesión",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(Login.this,"No se pudo logearse este usuario",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            }
                            else{
                                Toast.makeText(Login.this,"Usuario no registrado ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        botonRegistrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistroUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void saveLoginSharedPreferences(String usuario,String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("id", id);
        editor.apply();
    }

}
