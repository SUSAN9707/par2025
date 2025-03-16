/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Descripción: Este programa realiza solicitudes HTTP y HTTPS a URLs específicas
    y muestra la respuesta en la consola.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Test02 {
    public static void main(String[] args) throws IOException {
        // URL para probar HTTP
        String httpUrl = "http://grado.pol.una.py";
        // URL para probar HTTPS
        String httpsUrl = "https://grado.pol.una.py";

        // Realiza una solicitud HTTP y muestra el resultado
        System.out.println("Haciendo solicitud HTTP:");
        hacerHttpRequest(httpUrl);

        // Realiza una solicitud HTTPS y muestra el resultado
        System.out.println("\nHaciendo solicitud HTTPS:");
        hacerHttpsRequest(httpsUrl);
    }

    /**
     * Método para hacer una llamada HTTP a una URL.
     *
     * @param urlString La URL a la que se enviará la solicitud HTTP.
     * @throws IOException Si ocurre un error en la conexión o lectura de la respuesta.
     */
    private static void hacerHttpRequest(String urlString) throws IOException {
        // Crear un objeto URL a partir de la cadena proporcionada
        URL url = new URL(urlString);

        // Establecer conexión HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configurar el método de la solicitud (GET)
        connection.setRequestMethod("GET");

        // Leer la respuesta del servidor
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Imprimir la respuesta en la consola
            System.out.println(response.toString());
        } finally {
            // Cerrar la conexión para liberar recursos
            connection.disconnect();
        }
    }

    /**
     * Método para hacer una llamada HTTPS a una URL.
     *
     * @param urlString La URL a la que se enviará la solicitud HTTPS.
     * @throws IOException Si ocurre un error en la conexión o lectura de la respuesta.
     */
    private static void hacerHttpsRequest(String urlString) throws IOException {
        // Crear un objeto URL a partir de la cadena proporcionada
        URL url = new URL(urlString);

        // Establecer conexión HTTPS
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // Configurar el método de la solicitud (GET)
        connection.setRequestMethod("GET");

        // Leer la respuesta del servidor
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Imprimir la respuesta en la consola
            System.out.println(response.toString());
        } finally {
            // Cerrar la conexión para liberar recursos
            connection.disconnect();
        }
    }
}
