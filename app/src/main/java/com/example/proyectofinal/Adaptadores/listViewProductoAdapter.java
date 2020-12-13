package com.example.proyectofinal.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectofinal.Models.Producto;
import com.example.proyectofinal.R;

import java.util.ArrayList;

public class listViewProductoAdapter extends BaseAdapter {

    Context context;
    ArrayList<Producto> productoData;
    LayoutInflater layoutInflater;
    Producto productoModal;

    public listViewProductoAdapter(Context context, ArrayList<Producto> productoData) {
        this.context = context;
        this.productoData = productoData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return productoData.size();
    }

    @Override
    public Object getItem(int i) {
        return productoData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_producto,
                    null,
                    true);
        }
        //enlaza la vista
        TextView descript = (TextView) rowView.findViewById(R.id.descrip);
        TextView prec = (TextView) rowView.findViewById(R.id.precio);
        TextView fechaRegis = (TextView) rowView.findViewById(R.id.fecha);

        productoModal = productoData.get(i);
        descript.setText(productoModal.getDescripction());
        prec.setText(productoModal.getPrecio());
        fechaRegis.setText(productoModal.getFechaRegistro());



        return rowView;
    }
}
