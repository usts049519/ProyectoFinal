package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;

public class PruebaNotificacionActivity extends AppCompatActivity {

    private Button btnPrueba, btnVolver;
    private EditText txtTitu, txtDet;

    private String txtTitulo, txtDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_notificacion);

        txtTitu = (EditText) findViewById(R.id.txtTitulo);
        txtDet = (EditText) findViewById(R.id.txtDetalle);

        btnPrueba = (Button) findViewById(R.id.btnNotificatiPush);

        FloatingActionButton btnAgregarNuevaComputadora = findViewById(R.id.btnVolverPrin);
        btnAgregarNuevaComputadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PruebaNotificacionActivity.this, PrincipalActivity.class));
                finish();
            }
        });

        //notificacion
        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitulo = txtTitu.getText().toString();
                txtDetalle = txtDet.getText().toString();
                notificar();

                txtTitu.setText("");
                txtDet.setText("");
            }


        });

        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodoos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //Toast.makeText(PruebaNotificacionActivity.this,"se mando la notificacion",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notificar() {



        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try {
            //String token = "";

            json.put("to", "/topics/"+"enviaratodoos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", txtTitulo);
            notificacion.put("detalle", txtDetalle);

            json.put("data", notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String ,String > header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAqsKDztI:APA91bGDobdREfNZn92aLtgNsxEbHM--YPV-BXDTU6kxY31CK6r59vBUnP3TAg5eHicQo4qoXQ1k5b6KryQd6-INJ2IhBkEUxmgbeTfmpdu81Up9edJZyOr3_dJJRR1SpR4j6OwD6GgD");
                    return header;
                }
            };

            myrequest.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}