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
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rpimentel
 */
public class Main {

    public static void main(String[] args) {

        Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );   
        Scanner scanner = new Scanner(System.in);
        boolean existencia = false;
        prepararLogger(logger); 
        
        String divisa = "";
        while (!divisa.equalsIgnoreCase("NINGUNA")) {
        logger.info("*********************************************************************");
        logger.info("Fecha actual del sistema:");
        obtieneFechaAccesso();
        logger.info("*********************************************************************");
        logger.info("¿Cuánto vale un Bitcoin en mi divisa?");
        logger.info("*********************************************************************");
        logger.info("Introduce el nombre de tu divisa o mostrar divisas disponibles (DIVI)");
        

        divisa = scanner.next();
        logger.info(";... espere un momento");

        List<DivisaJsonClass> lista = consultaBitCoinMarket();

        if (divisa.equalsIgnoreCase("DIVI")) {
            for (int i = 0; i < lista.size(); i++) {
                Pattern pat = Pattern.compile("^localbtc.*");
                Matcher mat = pat.matcher(lista.get(i).symbol);
                if (mat.matches()) {
                    String ultimosCaracteres = lista.get(i).symbol.substring(lista.get(i).symbol.length() -3);
                    logger.info(ultimosCaracteres);                   
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
              logger.info("La divisa no existe");
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
    
  private static void prepararLogger(Logger loggerErrores) {
    LogManager.getLogManager().reset();
    loggerErrores.setLevel(Level.ALL);
    ConsoleHandler manejadorConsola = new ConsoleHandler();
    manejadorConsola.setLevel(Level.ALL);
    loggerErrores.addHandler(manejadorConsola);
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
        private double bid;
        private String currency;
        // Precio a la compra
        private double ask;
        private String symbol;

        public double getBid() {
            return bid;
        }

        public void setBid(double bid) {
            this.bid = bid;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getAsk() {
            return ask;
        }

        public void setAsk(double ask) {
            this.ask = ask;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
        
    }
}
