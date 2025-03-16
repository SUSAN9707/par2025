/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Descripción: Este programa realiza una solicitud HTTP GET a una URL y
    muestra el contenido de la respuesta en la consola.
*/

import java.io.*;
import java.net.*;

public class PeticionGET {

    public static void main(String[] args) {
        try {
            // Definir la URL a la que se realizará la solicitud
            String var_url = "https://www.google.com/";
            URL url = new URL(var_url);

            // Abrir una conexión a la URL
            URLConnection conexion = url.openConnection();

            // Crear un flujo de entrada para leer la respuesta de la URL
            InputStreamReader input_str = new InputStreamReader(conexion.getInputStream());
            BufferedReader reader = new BufferedReader(input_str);

            // Variable para almacenar cada línea leída
            String linea;

            // Leer la respuesta línea por línea e imprimirla en la consola
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }

            // Cerrar el flujo de lectura
            reader.close();

        } catch (MalformedURLException me) {
            // Captura y manejo de error si la URL es inválida
            System.err.println("Error: La URL es mal formada. Detalle: " + me);

        } catch (IOException ioe) {
            // Captura y manejo de error si ocurre un problema de entrada/salida
            System.err.println("Error: Problema de conexión o lectura de datos. Detalle: " + ioe);
        }
    }
}
