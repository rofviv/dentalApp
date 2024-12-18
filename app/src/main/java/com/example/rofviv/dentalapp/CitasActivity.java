package com.example.rofviv.dentalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CitasActivity extends AppCompatActivity {

    private String id;
    ListView lsCitas;
    ArrayList<Datos> lista;
    AlertDialog.Builder dialogo;
    String[] id_horarios;
    int auxPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        this.id = extras.getString("id");
        lsCitas = (ListView) findViewById(R.id.lsCitas);
        dialogo = new AlertDialog.Builder(this);
        new cargarDatos().execute(Conexion.ip + "/citas.php?sw=consulta&id=" + this.id);

        lsCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (lista.get(i).getEstado().split(":")[1].toString().trim().equals("pendiente")) {
                    auxPos = i;
                    dialogo.setTitle("Cancelar");
                    dialogo.setMessage("Desea realmente cancelar la cita? ");
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Si, Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Toast.makeText(getApplicationContext(), "Cancelado correctamente ID: " + id_horarios[auxPos], Toast.LENGTH_SHORT).show();
                            new CancelarCita().execute(Conexion.ip + "/citas.php?sw=cancelar&id_c=" + lista.get(auxPos).getId() + "&estado=cancelado%20por%20el%20paciente&id_horario=" + id_horarios[auxPos]);
                        }
                    });
                    dialogo.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //
                        }
                    });
                    dialogo.show();
                }
            }
        });
    }

    private class CancelarCita extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext() ,"cargando datos...", Toast.LENGTH_SHORT).show();
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
                ja = new JSONArray(result);
                Toast.makeText(getApplicationContext(), "Cita Cancelada Correctamente", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un problema al cancelar la cita", Toast.LENGTH_LONG).show();
            }
        }
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
            JSONArray ja;
            try {
                ja = new JSONArray(result);
                lista = new ArrayList<Datos>();
                id_horarios = new String[ja.length()];
                Adaptador adaptador;
                JSONObject jo;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if (!jo.getString("estado").equals("pendiente")) {
                        lista.add(new Datos(jo.getString("id"), "Doctor/a: " + jo.getString("nombre") + " " + jo.getString("apellido"),
                                "Fecha y Hora: " + jo.getString("fecha") + " " + jo.getString("hora"), "Estado: " + jo.getString("estado"),
                                "", R.drawable.citas));
                    } else {
                        lista.add(new Datos(jo.getString("id"), "Doctor/a: " + jo.getString("nombre") + " " + jo.getString("apellido"),
                                "Fecha y Hora: " + jo.getString("fecha") + " " + jo.getString("hora"), "Estado: " + jo.getString("estado"),
                                "Toca para cancelar la cita", R.drawable.citas));
                    }

                    id_horarios[i] = jo.getString("id_horario");
                }
                adaptador = new Adaptador(getApplicationContext(), lista);
                lsCitas.setAdapter(adaptador);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No has realizado ninguna cita", Toast.LENGTH_LONG).show();
            }
        }
    }
}
