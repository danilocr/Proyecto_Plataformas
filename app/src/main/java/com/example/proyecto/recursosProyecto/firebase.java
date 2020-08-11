package com.example.proyecto.recursosProyecto;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebase {


    public static FirebaseAuth mAuth;
    public static FirebaseDatabase fDatabase;
    public static DatabaseReference mDatabase;

    public static void inicializarFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
//        fDatabase.setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
