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
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rpimentel
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean existencia = false;

        
        String divisa = "";
        while (!divisa.equalsIgnoreCase("NINGUNA")) {
        System.out.println("********************");
        System.out.println("Fecha actual del sistema:"+ obtieneFechaAccesso());
        System.out.println("********************");
        System.out.println("*********************************************************************");
        System.out.println("¿Cuánto vale un Bitcoin en mi divisa?");
        System.out.println("*********************************************************************");
        System.out.println("Introduce el nombre de tu divisa o mostrar divisas disponibles (DIVI)");

        divisa = scanner.next();
        System.out.println("... espere un momento");

        List<DivisaJsonClass> lista = consultaBitCoinMarket();

        if (divisa.equalsIgnoreCase("DIVI")) {
            for (int i = 0; i < lista.size(); i++) {
                Pattern pat = Pattern.compile("^localbtc.*");
                Matcher mat = pat.matcher(lista.get(i).symbol);
                if (mat.matches()) {
                    String ultimosCaracteres = lista.get(i).symbol.substring(lista.get(i).symbol.length() -3);
                    System.out.print(ultimosCaracteres + ", ");
                    
                } 
            }
        } else {
            for (int i = 0; i < lista.size() -1; i++) {
                if (lista.get(i).symbol.equals("localbtc" + divisa.toUpperCase())) {
                    System.out.println(lista.get(i).currency + " : " + lista.get(i).ask);
                    existencia = true;
                }
            }

            if (!existencia) {
                System.out.println("La divisa no existe");
            }

        }
        
        }

    }

    private static List<DivisaJsonClass> consultaBitCoinMarket() {
        
        
        URL url;
        try {
            
            url= new URL("http://api.bitcoincharts.com/v1/markets.json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            return new ArrayList();
        }

        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        TypeToken<List<DivisaJsonClass>> token = new TypeToken<List<DivisaJsonClass>>() {
        };
        return new Gson().fromJson(isr, token.getType());    
        } catch (IOException e) {
          return new ArrayList();  
        }
        

        
    }
    
    private static String obtieneFechaAccesso(){
      Date fecha = new Date();
      DateFormat horaFechaFormato = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
      return horaFechaFormato.format(fecha);
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
