package com.example.rofviv.dentalapp;

public class Consejos {

    private static String[] lsConsejos = {"Cepillarse los dientes al menos tres veces al dia", "Enjuagarse la boca despues de comer", "Tomar mucha agua es bueno", "No comer mucho antes de dormid", "etc"};


    public static String getConsejo(int i) {
        return lsConsejos[i];
    }

    public static int longitud() {
        return lsConsejos.length;
    }

}
