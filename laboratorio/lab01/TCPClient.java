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

public class TCPClient {
    public static void main(String[] args) {
        // Dirección del servidor y puerto en el que escucha
        final String SERVER_ADDRESS = "localhost"; // Dirección del servidor (misma máquina)
        final int SERVER_PORT = 9876; // Puerto en el que escucha el servidor

        try {
            // Crear un socket y conectarse al servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor: " + socket);

            // Crear un lector para la entrada del usuario desde la consola
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Crear un flujo de salida para enviar datos al servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Crear un flujo de entrada para recibir datos del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Bucle para enviar y recibir mensajes continuamente
            while (true) {
                System.out.print("Ingrese un mensaje para enviar al servidor: ");

                // Leer el mensaje ingresado por el usuario
                String message = userInput.readLine();

                // Enviar el mensaje al servidor
                out.println(message);

                // Leer la respuesta del servidor
                String response = in.readLine();
                System.out.println("Respuesta del servidor: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
