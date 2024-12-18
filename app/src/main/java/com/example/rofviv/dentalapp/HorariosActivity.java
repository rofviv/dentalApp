package com.example.rofviv.dentalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HorariosActivity extends AppCompatActivity {

    String id_p;
    String id_d;
    String fecha;
    String hora;
    String filtro;
    Intent intent;
    Bundle extras;
    String[] horarios;
    String[] ids_doctores;
    ListView lsHorarios;
    ArrayList<Datos> lista;
    AlertDialog.Builder dialogo;
    int auxPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        intent = getIntent();
        extras = intent.getExtras();
        this.id_p = (String) extras.get("id_p");
        this.filtro = (String) extras.get("filtro");
        filtrarBusqueda(this.filtro);
        lsHorarios = (ListView) findViewById(R.id.lsHorarios);
        dialogo = new AlertDialog.Builder(this);
        lsHorarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                auxPos = pos;
                dialogo.setTitle("Reservar");
                dialogo.setMessage("Desea reservar la cita en " + horarios[pos]
                        + " con el " + lista.get(pos).getDoctor());
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("Reservar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        new ReservarCita().execute(Conexion.ip + "/citas.php?sw=registro&id_p=" + id_p + "&id_d=" + ids_doctores[auxPos] + "&fecha=" + horarios[auxPos].split(" ")[0] + "&hora=" + horarios[auxPos].split(" ")[1] + "&id_horario=" + lista.get(auxPos).getId());
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //
                    }
                });
                dialogo.show();
            }
        });
    }

    public void filtrarBusqueda(String f) {
        if (f.equals("doctor")) {
            fecha = (String) extras.get("fecha");
            hora = (String) extras.get("hora");
            id_d = (String) extras.get("id_d");
            new cargarDatosDoctor().execute(Conexion.ip + "/citas.php?sw=reservar&filtro=doctor&id_d=" + id_d + "&fecha=" + fecha + "&hora=" + hora);
        } else if (f.equals("fecha")) {
            fecha = (String) extras.get("fecha");
            hora = (String) extras.get("hora");
            new cargarDatosFecha().execute(Conexion.ip + "/citas.php?sw=reservar&filtro=fecha&fecha=" + fecha + "&hora=" + hora);
        }
    }

    private class ReservarCita extends AsyncTask<String, Void, String> {

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
                Toast.makeText(getApplicationContext(), "Registrado correctamente", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un problema al registrar la cita", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class cargarDatosDoctor extends AsyncTask<String, Void, String> {

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
                lista = new ArrayList<Datos>();
                Adaptador adaptador;
                ja = new JSONArray(result);
                JSONObject jo;
                ids_doctores = new String[ja.length()];
                horarios = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    lista.add(new Datos(jo.getString("id_horario"), "Doctor/a: " + jo.getString("nombre") + " " + jo.getString("apellido"),
                            "Fecha y Hora: " + jo.getString("fecha") + " " + jo.getString("hora"), "Estado: " + jo.getString("estado"),
                            "Toca para Reservar esta cita", R.drawable.citas));
                    ids_doctores[i] = jo.getString("id_doctor");
                    horarios[i] = jo.getString("fecha") + " " + jo.getString("hora");
                }
                adaptador = new Adaptador(getApplicationContext(), lista);
                lsHorarios.setAdapter(adaptador);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "El doctor/a no tiene horarios disponibles", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class cargarDatosFecha extends AsyncTask<String, Void, String> {

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
                lista = new ArrayList<Datos>();
                Adaptador adaptador;
                ja = new JSONArray(result);
                JSONObject jo;
                ids_doctores = new String[ja.length()];
                horarios = new String[ja.length()];
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    lista.add(new Datos(jo.getString("id_horario"), "Doctor/a: " + jo.getString("nombre") + " " + jo.getString("apellido"),
                            "Fecha y Hora: " + jo.getString("fecha") + " " + jo.getString("hora"), "Estado: " + jo.getString("estado"),
                            "Toca para Reservar esta cita", R.drawable.citas));
                    ids_doctores[i] = jo.getString("id_doctor");
                    horarios[i] = jo.getString("fecha") + " " + jo.getString("hora");
                }
                adaptador = new Adaptador(getApplicationContext(), lista);
                lsHorarios.setAdapter(adaptador);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No hay doctores disponibles para este horario", Toast.LENGTH_LONG).show();
            }
        }
    }
}
