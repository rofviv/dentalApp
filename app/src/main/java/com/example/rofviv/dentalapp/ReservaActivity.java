package com.example.rofviv.dentalapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReservaActivity extends AppCompatActivity {

    RadioButton rbDoctor;
    RadioButton rbFecha;
    Spinner spDoctores;
    Spinner spHora;
    Spinner spFecha;
    String[] IDdoctores;
    RadioGroup rbg;
    String posIDdoctor = "-1";
    String id_p;
    Calendar fecha = new GregorianCalendar();
    int año;
    int mes;
    int dia;
    int hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reserva);
        rbg = (RadioGroup) findViewById(R.id.radiogroup);
        rbDoctor = (RadioButton) findViewById(R.id.rbDoctor);
        rbFecha = (RadioButton) findViewById(R.id.rbFechaHora);
        spDoctores = (Spinner) findViewById(R.id.spDoctores);
        spHora = (Spinner) findViewById(R.id.spHora);
        spFecha = (Spinner) findViewById(R.id.spFecha);
        año = fecha.get(Calendar.YEAR);
        mes = fecha.get(Calendar.MONTH);
        dia = fecha.get(Calendar.DAY_OF_MONTH);
        hora = fecha.get(Calendar.HOUR_OF_DAY);

        new CargarDatos().execute(Conexion.ip + "/listadoctores.php");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id_p = (String) extras.get("id");
        habilitarOpciones();


        spDoctores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                posIDdoctor = IDdoctores[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spFecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actualizarHora();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rbg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                habilitarOpciones();
            }
        });
    }

    private void actualizarHora() {
        ArrayList horarios = new ArrayList();
        if (spFecha.getSelectedItemPosition() == 0) {
            int auxHora = hora + 1;
            int cant = 14;
            if (auxHora > 7) {
                cant = 21 - hora;
            } else {
                auxHora = 7;
            }
            for (int i = 0; i < cant; i++) {
                if (auxHora < 10) {
                    horarios.add("0" + auxHora + ":00");
                } else {
                    horarios.add(auxHora + ":00");
                }
                auxHora++;
            }
        } else {
            int auxHora = 7;
            for (int i = 0; i < 14; i++) {
                if (auxHora < 10) {
                    horarios.add("0" + auxHora + ":00");
                } else {
                    horarios.add(auxHora + ":00");
                }
                auxHora++;
            }
        }
        spHora.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, horarios));
    }

    private void habilitarFechaHora() {
        int auxAño = año;
        int auxMes = mes + 1;
        int auxDia = dia;
        ArrayList fechas = new ArrayList();
        for (int i = 0; i < 5; i++) {
            fechas.add(auxAño + "-" + auxMes + "-" + auxDia);
            if(auxDia == 30) {
                auxMes++;
                auxDia = 0;
            }
            auxDia++;
        }
        actualizarHora();
        spFecha.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fechas));
    }

    private void habilitarOpciones() {
        if (rbDoctor.isChecked()) {
            spDoctores.setEnabled(true);
            spFecha.setEnabled(false);
            spHora.setEnabled(false);
        } else {
            spDoctores.setEnabled(false);
            spFecha.setEnabled(true);
            spHora.setEnabled(true);
        }
    }

    public void buscarHorarios(View v) {

        if (rbDoctor.isChecked()) {
            if (spDoctores.getSelectedItemPosition() > 0) {
                Intent i = new Intent(this, HorariosActivity.class);
                i.putExtra("filtro", "doctor");
                i.putExtra("fecha", año + "-" + mes + "-" + dia);
                i.putExtra("hora", hora + ":00");
                i.putExtra("id_d", posIDdoctor);
                i.putExtra("id_p", id_p);
                startActivityForResult(i, 0);
            } else {
                Toast.makeText(getApplicationContext(), "Debes seleccionar un doctor", Toast.LENGTH_LONG).show();
            }

        } else {
            String tFecha = spFecha.getSelectedItem().toString();
            String tHora = spHora.getSelectedItem().toString();
            if (!tFecha.equals("") && !tHora.equals("")) {
                Intent i = new Intent(this, HorariosActivity.class);
                i.putExtra("filtro", "fecha");
                i.putExtra("fecha", tFecha);
                i.putExtra("hora", tHora);
                i.putExtra("id_p", id_p);
                startActivityForResult(i, 0);
            } else {
                Toast.makeText(getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cargarSpinner(String[] lista) {
        spDoctores.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista));

    }

    private class CargarDatos extends AsyncTask<String, Void, String> {

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
                JSONObject jo;
                String[] lsDoctores = new String[ja.length() + 1];
                IDdoctores = new String[ja.length() + 1];
                lsDoctores[0] = "Selecciona un doctor";
                IDdoctores[0] = "-1";
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    lsDoctores[i + 1] = "Dr/a " + jo.getString("nombre") + " " + jo.getString("apellido");
                    IDdoctores[i + 1] = jo.getString("id");
                }
                cargarSpinner(lsDoctores);
                habilitarFechaHora();
            } catch (JSONException e) {
            }
        }
    }
}
