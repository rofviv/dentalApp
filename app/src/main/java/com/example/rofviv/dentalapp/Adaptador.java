package com.example.rofviv.dentalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Adaptador extends BaseAdapter {

    Context contexto;
    List<Datos> lista;

    public Adaptador(Context contexto, List<Datos> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(lista.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflater = LayoutInflater.from(contexto);
        vista = inflater.inflate(R.layout.layout_lista, null);

        TextView doctor = (TextView) vista.findViewById(R.id.txtDoctor);
        TextView fecha = (TextView) vista.findViewById(R.id.txtFechaHora);
        TextView estado = (TextView) vista.findViewById(R.id.txtEstado);
        TextView boton = (TextView) vista.findViewById(R.id.txtBoton);
        ImageView img = (ImageView) vista.findViewById(R.id.imgImagen);

        doctor.setText(lista.get(i).getDoctor().toString());
        fecha.setText(lista.get(i).getFechaHora().toString());
        estado.setText(lista.get(i).getEstado().toString());
        boton.setText(lista.get(i).getBoton().toString());
        img.setImageResource(lista.get(i).getImagen());

        return vista;
    }
}
