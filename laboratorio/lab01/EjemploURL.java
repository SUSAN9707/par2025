/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Descripción: Este programa demuestra el uso de la clase URL en Java.
                 Se crean dos instancias de URL y se imprimen sus atributos.
    Referencia: https://docs.oracle.com/javase/10/docs/api/java/net/URL.html
*/

import java.io.IOException;
import java.net.URL;

public class EjemploURL {
    public static void main(String[] args) throws IOException {

        // Definir una URL en forma de cadena
        String test_url = "http://www.pol.una.py";

        // Crear un objeto URL a partir de la cadena
        URL pagina1 = new URL(test_url);

        // Crear un objeto URL con diferentes parámetros (protocolo, host, puerto, archivo)
        URL pagina2 = new URL("http", "grado.pol.una.py", 80, "index.html");

        // Primera Prueba: Obtener y mostrar información de la primera URL
        System.out.println("***** Primera página *****");
        System.out.println("Protocolo: " + pagina1.getProtocol());   // Protocolo (HTTP/HTTPS)
        System.out.println("Puerto: " + pagina1.getPort());         // Puerto especificado (o -1 si no se especificó)
        System.out.println("Host: " + pagina1.getHost());           // Nombre del host o IP
        System.out.println("Archivo: " + pagina1.getFile());        // Parte de la URL después del dominio
        System.out.println("External form: " + pagina1.toExternalForm()); // Representación completa de la URL
        System.out.println();

        // Segunda Prueba: Obtener y mostrar información de la segunda URL
        System.out.println("***** Segunda página *****");
        System.out.println("Protocolo: " + pagina2.getProtocol());  // Protocolo usado (HTTP/HTTPS)

        // Verificar si el puerto fue definido, si no, usar el puerto por defecto del protocolo
        System.out.println("Puerto: " + (pagina2.getPort() != -1 ? pagina2.getPort() : pagina2.getDefaultPort()));

        System.out.println("Host: " + pagina2.getHost());          // Nombre del host
        System.out.println("Archivo: " + pagina2.getFile());       // Ruta del archivo en el servidor
        System.out.println("External form: " + pagina2.toExternalForm()); // Representación en forma de cadena de la URL
        System.out.println();

    }

}
