/**
 * Autor: Claudia Palacios
 * Fecha: Febrero 2025
 * Descripción: Este programa realiza una petición HTTP GET a una URL específica
 * y muestra la respuesta en consola.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Test01 {
    public static void main(String[] args) {
        // URL de prueba para hacer la solicitud HTTP
        String url = "https://www.datos.gov.py/dataset/proyectos-adjudicados-hackathon";
        String respuesta = "";

        try {
            // Realiza la petición HTTP GET y almacena la respuesta
            respuesta = peticionHttpGet(url);

            // Imprime la respuesta en la consola
            System.out.println("La respuesta es:\n" + respuesta);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error en la solicitud
            e.printStackTrace();
        }
    }

    /**
     * Método que realiza una petición HTTP GET a una URL específica.
     *
     * @param urlParaVisitar La URL a la que se realizará la petición.
     * @return La respuesta del servidor en formato String.
     * @throws Exception Si ocurre un error en la conexión o lectura de datos.
     */
    public static String peticionHttpGet(String urlParaVisitar) throws Exception {
        StringBuilder resultado = new StringBuilder(); // Almacena la respuesta del servidor

        // Crea un objeto URL a partir de la cadena proporcionada
        URL url = new URL(urlParaVisitar);

        // Abre una conexión HTTP con la URL
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

        // Especifica que la solicitud es de tipo GET
        conexion.setRequestMethod("GET");

        // Lee la respuesta del servidor utilizando BufferedReader
        BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String linea;

        // Lee cada línea de la respuesta y la agrega al resultado
        while ((linea = rd.readLine()) != null) {
            resultado.append(linea);
        }

        // Cierra el BufferedReader para liberar recursos
        rd.close();

        // Retorna la respuesta obtenida del servidor
        return resultado.toString();
    }
}
