package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.time.chrono.MinguoChronology;

public class PrincipalActivity extends AppCompatActivity {

    private Button btnCerrar, btnPruebaNoti;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCerrar = (Button) findViewById(R.id.btnCerrar);
        btnPruebaNoti = (Button) findViewById(R.id.btnPrueba);

        btnPruebaNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrincipalActivity.this, PruebaNotificacionActivity.class));
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
                finish();
            }
        });



    }
}