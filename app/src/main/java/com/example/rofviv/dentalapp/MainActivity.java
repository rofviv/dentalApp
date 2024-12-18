package com.example.rofviv.dentalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String id;
    private String paciente;
    TextView txtSaludo;
    TextView tConsejo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSaludo = (TextView) findViewById(R.id.txtSaludoMenu);
        tConsejo = (TextView) findViewById(R.id.txtConsejos);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = (String) extras.get("id");
            paciente = (String) extras.get("paciente");
            txtSaludo.setText(txtSaludo.getText() + " " + paciente);
        }
    }

    public void actividadMisCitas(View v) {
        Intent i = new Intent(this, CitasActivity.class);
        i.putExtra("id", this.id);
        startActivityForResult(i, 0);
    }

    public void actividadReservarCita(View v) {
        Intent i = new Intent(this, ReservaActivity.class);
        i.putExtra("id", this.id);
        startActivityForResult(i, 0);
    }

    public void actividadDiagnostico(View v) {
        Intent i = new Intent(this, DiagnosticoActivity.class);
        i.putExtra("id", this.id);
        startActivityForResult(i, 0);
    }

    public void actividadConsejos(View v) {
        Intent i = new Intent(this, ConsejosActivity.class);
        startActivityForResult(i, 0);
    }

    public void logout(View v) throws InterruptedException {
        Toast.makeText(getApplicationContext(), "Saliendo ...", Toast.LENGTH_SHORT);
    }
}
