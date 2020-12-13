package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinal.Adaptadores.listViewProductoAdapter;
import com.example.proyectofinal.Models.Producto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private ArrayList<Producto> listProductos = new ArrayList<Producto>();
    ArrayAdapter<Producto> arrayAdapterProducto;
    listViewProductoAdapter ListViewProductoAdapter;
    LinearLayout linearEditar;
    ListView listViewProducto;

    EditText description, preci;
    Button btnCancelar;

    Producto productoSeleccionado;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        description = findViewById(R.id.productoNombre);
        preci = findViewById(R.id.productoPrecio);
        btnCancelar = findViewById(R.id.btnCancelar);

        listViewProducto = findViewById(R.id.listaProducto);
        linearEditar = findViewById(R.id.linearEditar);

        listViewProducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productoSeleccionado = (Producto) adapterView.getItemAtPosition(i);

                description.setText(productoSeleccionado.getDescripction());
                preci.setText(productoSeleccionado.getPrecio());

                //Hace visible el linear layout
                linearEditar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearEditar.setVisibility(View.GONE);
                productoSeleccionado = null;
            }
        });

        insertarDataFirebase();
        listarProducto();

        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton btnCerrar = findViewById(R.id.btnCerarSesionFloat);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
                finish();
            }
        });

        FloatingActionButton btnNoti = findViewById(R.id.btnNotificacionBut);
        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrincipalActivity.this, PruebaNotificacionActivity.class));
                finish();
            }
        });
    }

    public  void insertarDataFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void listarProducto(){
        databaseReference.child("Productos").orderByChild("timeOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProductos.clear();
                for(DataSnapshot objSnaptsHost : snapshot.getChildren()){
                    Producto p = objSnaptsHost.getValue(Producto.class);
                    listProductos.add(p);
                }

                //inicia el adaptador
                ListViewProductoAdapter = new listViewProductoAdapter(PrincipalActivity.this, listProductos);

                listViewProducto.setAdapter(ListViewProductoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String descripcion = description.getText().toString();
        String precio = preci.getText().toString();

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_modificar:
                if (productoSeleccionado != null){
                    if (validarInputs()==false){
                        Producto p = new Producto();
                        p.setIdProducto(productoSeleccionado.getIdProducto());
                        p.setDescripction(descripcion);
                        p.setPrecio(precio);
                        p.setFechaRegistro(productoSeleccionado.getFechaRegistro());
                        p.setTimeOrder(productoSeleccionado.getTimeOrder());

                        databaseReference.child("Productos").child(p.getIdProducto()).setValue(p);
                        Toast.makeText(PrincipalActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        description.setText("");
                        preci.setText("");
                        linearEditar.setVisibility(View.GONE);
                        productoSeleccionado = null;
                    }
                }else{
                    Toast.makeText(PrincipalActivity.this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.menu_eliminar:
                if (productoSeleccionado != null){
                    Producto pro = new Producto();
                    pro.setIdProducto(productoSeleccionado.getIdProducto());

                    databaseReference.child("Productos").child(pro.getIdProducto()).removeValue();
                    Toast.makeText(PrincipalActivity.this, "Registro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    description.setText("");
                    preci.setText("");
                    linearEditar.setVisibility(View.GONE);
                    productoSeleccionado = null;
                    Toast.makeText(PrincipalActivity.this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PrincipalActivity.this, "Seleccione un producto para eliminar", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertar(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                PrincipalActivity.this
        );

        View mView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsert = (Button) mView.findViewById(R.id.insertar);
        final EditText txtdescr = (EditText)mView.findViewById(R.id.txtDescrip);
        final EditText txtPrec = (EditText)mView.findViewById(R.id.txtPrecioo);

        mBuilder.setView(mView);
        final  AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String descripcion = txtdescr.getText().toString();
                String precio = txtPrec.getText().toString();

                if (descripcion.isEmpty() || descripcion.length() < 3){
                    showError(txtdescr, "Descripcion no es validad 'Min. 3 letras '");

                }else if (precio.isEmpty() || precio.length() < 1){
                    showError(txtPrec, "Precio no es validad 'Min. 1 numero '");

                }else {
                    Producto p = new Producto();
                    p.setIdProducto(UUID.randomUUID().toString());
                    p.setDescripction(descripcion);
                    p.setPrecio(precio);
                    p.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                    p.setTimeOrder(getFechaMilisegundos() * -1);

                    databaseReference.child("Productos").child(p.getIdProducto()).setValue(p);
                    Toast.makeText(PrincipalActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    txtdescr.setText("");
                    txtPrec.setText("");

                    dialog.dismiss();
                }
            }
        });
    }

    public void showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }

    public String getFechaNormal(long getFechaMilisegundos){
        SimpleDateFormat date = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        String fecha = date.format(getFechaMilisegundos);
        return fecha;
    }

    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long tiempo = calendar.getTimeInMillis();
        return tiempo;
    }


    public boolean validarInputs() {

        String descripcion = description.getText().toString();
        String precio = preci.getText().toString();

        if (descripcion.isEmpty() || descripcion.length() < 3) {
            showError(description, "Descripcion no es validad 'Min. 3 letras '");
            return true;
        } else if (precio.isEmpty() || precio.length() < 1) {
            showError(preci, "Precio no es validad 'Min. 1 numero '");
            return true;
        }else   {
            return false;
        }
    }

}