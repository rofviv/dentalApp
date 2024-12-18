package com.example.rofviv.dentalapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DiagnosticoActivity extends AppCompatActivity {

    private String id;
    ListView lsDiagnostico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        this.id = (String) extras.get("id");
        new cargarDatos().execute(Conexion.ip + "/diagnostico.php?id=" + this.id);
        lsDiagnostico = (ListView) findViewById(R.id.lsDiagnostico);
    }

    private class cargarDatos extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext() ,"cargando datos...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return Funciones.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Pagina web deshabilitada. URL Invalida";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            JSONArray ja = null;
            try {
                ArrayList<String> arrayLista = new ArrayList<>();
                ArrayAdapter adaptador;
                ja = new JSONArray(result);
                JSONObject jo;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    arrayLista.add("Doctor/a: " + jo.getString("nombre") + " " + jo.getString("apellido")
                        + "\nTipo: " + jo.getString("tipo")
                        + "\nDescripcion: " + jo.getString("descripcion")
                        + "\nEstado: " + jo.getString("estado")
                        + "\nFecha: " + jo.getString("fecha"));
                }
                adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayLista);
                lsDiagnostico.setAdapter(adaptador);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext() ,"Aun no tienes nigun diagnostico", Toast.LENGTH_LONG).show();
            }
        }
    }
}
