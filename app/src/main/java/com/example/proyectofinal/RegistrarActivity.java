package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;

public class RegistrarActivity extends AppCompatActivity {

    private EditText nombre, email, password;
    private Button btnRegistrar;

    //Variables para los datos a registrar
    private String Nombre = "", Email = "", Password = "";

    FirebaseAuth firebaseAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Instancia de firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        //Volver a login
        Button btn = (Button) findViewById(R.id.btnVolver);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //Declaracion de variable
        nombre = (EditText) findViewById(R.id.txtNombre);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPass);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        //Validacion de datos y registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombre = nombre.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();

                if(!Nombre.isEmpty() && !Email.isEmpty() && !Password.isEmpty()){
                    if (Password.length() >= 6) {
                        registrarUser();
                    }else {
                        Toast.makeText(RegistrarActivity.this,"Debes elegir una contraseña de al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegistrarActivity.this,"Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Registrar usuario
    private void  registrarUser(){
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("Nombre",Nombre);
                    datos.put("Email", Email);
                    datos.put("Password", Password);

                    String id = firebaseAuth.getCurrentUser().getUid();

                    Toast.makeText(RegistrarActivity.this,"Te has registrado  exitosamente", Toast.LENGTH_SHORT).show();

                    database.child("Users").child(id).push().setValue(datos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(RegistrarActivity.this,"Tus datos se guardaron exitosamente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrarActivity.this, MainActivity.class));
                                finish();
                            }else {
                                Toast.makeText(RegistrarActivity.this,"Tus datos no se enviaron correctamente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrarActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegistrarActivity.this,"Tu correo ya esta registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}