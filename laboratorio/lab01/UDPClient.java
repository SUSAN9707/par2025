/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Notas:
    - Este código implementa un cliente UDP que se comunica con un servidor UDP.
    - Envía un mensaje al servidor, el servidor lo convierte a mayúsculas y lo devuelve.
    - Para ejecutar este código, primero inicia el servidor UDP y luego el cliente.
    - Asegúrate de que el puerto 9877 esté disponible y no esté bloqueado por otro programa.
*/

import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; // Dirección IP del servidor (localhost para pruebas locales)
        final int SERVER_PORT = 9877; // Puerto en el que el servidor UDP está escuchando

        try {
            // Crear un socket UDP sin necesidad de especificar un puerto
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS); // Obtener dirección IP del servidor

            // Buffer para leer la entrada del usuario
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) { // Bucle infinito para enviar múltiples mensajes
                System.out.print("Ingrese un mensaje para enviar al servidor: ");
                String message = userInput.readLine(); // Leer el mensaje del usuario

                // Convertir el mensaje a bytes y enviarlo al servidor
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                clientSocket.send(sendPacket); // Enviar el paquete UDP

                // Preparar buffer para recibir la respuesta del servidor
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket); // Esperar respuesta del servidor

                // Convertir los datos recibidos a String y mostrar la respuesta
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Respuesta del servidor: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejar excepciones de entrada/salida
        }
    }
}
