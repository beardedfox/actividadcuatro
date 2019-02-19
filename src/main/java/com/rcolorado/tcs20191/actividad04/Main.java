/*
 * 
 * UNIVERSIDAD VERACRUZANA
 * rcolorado@uv.mx
 * Ejercicio 04: Tecnologías para la construcción de Software
 * Versión 1.0.0
 */
package com.rcolorado.tcs20191.actividad04;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;



/**
 *
 * @author rpimentel
 */
public class Main {

    public static void main(String[] args) throws IOException  {

        Scanner scanner = new Scanner(System.in);

        System.out.println("********************");
        System.out.println("¿Cuánto vale un Bitcoin en mi divisa?");
        System.out.println("********************");
        System.out.println("Introduce el nombre de tu divisa (MXN, USD, CAD, EUR, ARS, VES):");

        String divisa = scanner.next();
        System.out.println("... espere un momento");

        List<DivisaJsonClass> lista = ConsultaBitCoinMarket();

        for (int i  = 0; i < lista.size()-2; i++) {
            if (lista.get(i).symbol.equals("localbtc" + divisa.toUpperCase())) {
                System.out.println(lista.get(i+1).currency + " : " + lista.get(i+1).ask);
            }
        }
        

    }
    
    
    private static List<DivisaJsonClass> ConsultaBitCoinMarket() throws IOException {
        URL url = new URL("http://api.bitcoincharts.com/v1/markets.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Fallo : HTTP error code : " + conn.getResponseCode());
        }

        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        TypeToken<List<DivisaJsonClass>> token = new TypeToken<List<DivisaJsonClass>>() {
        };
        List<DivisaJsonClass> lista = new Gson().fromJson(isr, token.getType());
        
        return lista;
    }

            
    /*
     * Clase privada para obtener los datos de las divisas
     *
     */
            
    class DivisaJsonClass {
        // Precio a la venta
        public double bid;
        public String currency;
        // Precio a la compra
        public double ask;
        public String symbol;
    }
}
