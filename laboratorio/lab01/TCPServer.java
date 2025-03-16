/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Notas:
    - En este ejemplo, el servidor TCP espera a que el cliente se conecte.
    - Luego, recibe un mensaje del cliente, lo convierte a mayúsculas y lo envía de vuelta.
    - El cliente se conecta al servidor, envía un mensaje, recibe la respuesta y la muestra en la consola.
    - Para ejecutar este código, primero ejecuta el servidor y luego el cliente.
    - Asegúrate de que el puerto 9876 esté disponible y no bloqueado por otro programa.
*/

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        final int SERVER_PORT = 9876; // Puerto en el que el servidor escuchará conexiones entrantes

        try {
            // Crear un socket de servidor en el puerto especificado
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Servidor TCP escuchando en el puerto " + SERVER_PORT + "...");

            while (true) { // Bucle infinito para aceptar múltiples conexiones de clientes
                // Espera hasta que un cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket);

                // Flujo de entrada para recibir datos del cliente
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Flujo de salida para enviar datos al cliente
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer el mensaje del cliente
                String message = in.readLine();
                System.out.println("Mensaje recibido del cliente: " + message);

                // Convertir el mensaje a mayúsculas y enviarlo de vuelta al cliente
                String response = "Respuesta del servidor: " + message.toUpperCase();
                out.println(response);

                // Cerrar la conexión con el cliente
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores en caso de problemas de entrada/salida
        }
    }
}
