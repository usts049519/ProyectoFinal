package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView user, pass;
    private Button btnIniciarSesion;
    private String User, Pass;

    private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnRegistrarse);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistrarActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        user = (TextView) findViewById(R.id.txtUser);
        pass = (TextView) findViewById(R.id.txtPassword);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciar);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                User = user.getText().toString();
                Pass = pass.getText().toString();

                if (!User.isEmpty() && !Pass.isEmpty()) {
                    loginInic();
                } else {
                    Toast.makeText(MainActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Validacion de Login
    private void loginInic(){
        firebaseAuth.signInWithEmailAndPassword(User, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, PruebaNotificacionActivity.class));
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, PruebaNotificacionActivity.class));
            finish();
        }
    }
}