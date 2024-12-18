package com.example.rofviv.dentalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ConsejosActivity extends AppCompatActivity {

    ListView lsLista;
    ArrayList<String> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);
        lsLista = (ListView) findViewById(R.id.lsConsejos);
        cargarLista();
    }

    private void cargarLista() {
        ArrayAdapter adaptador;
        datos = new ArrayList<>();
        for (int i = 0; i < Consejos.longitud(); i++) {
            datos.add(Consejos.getConsejo(i));
        }
        adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, datos);
        lsLista.setAdapter(adaptador);
    }
}
