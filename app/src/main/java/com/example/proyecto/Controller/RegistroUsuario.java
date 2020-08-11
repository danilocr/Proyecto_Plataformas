package com.example.proyecto.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto.Vista.Inicio;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {

    private EditText EditUsuario;
    private EditText EditContra;
    private EditText EditCorreo;
    private Button BotonRegistrarse;

    //Variables de los datos a registrase
    private String name = "";
    private String email = "";
    private String password = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        
        EditUsuario = (EditText) findViewById(R.id.editUsuario);
        EditContra = (EditText) findViewById(R.id.editContra);
        EditCorreo = (EditText) findViewById(R.id.editCorreo);
        BotonRegistrarse = (Button) findViewById(R.id.buttonRegistrase);

        BotonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = EditUsuario.getText().toString();
                password = EditContra.getText().toString();
                email = EditCorreo.getText().toString();

                if(!name.isEmpty() && !password.isEmpty() && !email.isEmpty()){
                    if(password.length() >= 6){
                        registerUser();
                    }
                    else {
                        Toast.makeText(RegistroUsuario.this, "La contraseña debe de ser de más de 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegistroUsuario.this,"Debe Completar los campos ",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerUser(){
        firebase.mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String,Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    final String id = firebase.mAuth.getCurrentUser().getUid();
                    firebase.mDatabase.child("users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                SharedPreferences sharedPreferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("usuario", name);
                                editor.putString("id", id);
                                editor.apply();

                                startActivity(new Intent(RegistroUsuario.this, Inicio.class));
                                onBackPressed();
                            }
                            else{
                                Toast.makeText(RegistroUsuario.this,"No se pudieron crear los datos correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegistroUsuario.this,"No se pudo registrar este usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}