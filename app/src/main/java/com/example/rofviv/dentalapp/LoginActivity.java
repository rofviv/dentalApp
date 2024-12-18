package com.example.rofviv.dentalapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Button btnIngresar;
    private EditText etUsuario;
    private EditText etClave;
    private AlertDialog alertDialog;
    private String id = "0";
    private String paciente = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.alertDialog = new AlertDialog.Builder(this).create();
        this.etUsuario = (EditText) findViewById(R.id.etxtCI);
        this.etClave = (EditText) findViewById(R.id.etxtClave);
        this.btnIngresar = (Button) findViewById(R.id.btnIngresar);
        this.btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = etUsuario.getText().toString().trim();
                String c = etClave.getText().toString().trim();
                String m = validarDatos(u, c);
                if (m.equals("")) {
                    new IniciarSesion().execute(Conexion.ip + "/login.php?ci=" + u + "&clave=" + c);
                } else {
                    mensajeAlerta("Iniciar Sesion", m);
                }
            }
        });
    }

    private String validarDatos(String u, String p) {
        if (!u.equals("") && !p.equals("")) {
            return "";
        } else {
            return "Por favor, debes rellenar todos los campos";
        }
    }

    private class IniciarSesion extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext() ,"Conectando...", Toast.LENGTH_LONG).show();
            etUsuario.setEnabled(false);
            etClave.setEnabled(false);
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
                JSONObject jo = ja.getJSONObject(0);
                if (!jo.getString("estado").equals("activo")) {
                    mensajeAlerta("Usuario Bloqueado", "Tu cuenta ha sido bloqueada, comunicate con el administrador");
                } else {
                    id = jo.getString("id");
                    paciente = jo.getString("nombre");
                    logearse();
                }
            } catch (JSONException e) {
                mensajeAlerta("Iniciar Sesion", "Usuario o contrasena incorrectos");
                etClave.setText("");
            }
            etUsuario.setEnabled(true);
            etClave.setEnabled(true);
        }
    }

    private void mensajeAlerta(String titulo, String mensaje) {
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensaje);
        alertDialog.show();
    }

    private void logearse() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id", this.id);
        i.putExtra("paciente", this.paciente);
        etClave.setText("");
        etUsuario.setText("");
        startActivityForResult(i, 0);
        finish();
    }
}
