/*
    Autor: Claudia Palacios
    Fecha: Febrero 2025
    Notas:
    - Este código implementa un servidor UDP que escucha en el puerto 9877.
    - Recibe mensajes de los clientes, los convierte a mayúsculas y los envía de vuelta.
    - Para ejecutar este código, primero inicia el servidor y luego el cliente UDP.
    - Asegúrate de que el puerto 9877 esté disponible y no esté bloqueado por otro programa.
*/

import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        final int SERVER_PORT = 9877; // Puerto en el que el servidor escuchará

        try {
            // Crear un socket UDP y enlazarlo al puerto especificado
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("Servidor UDP escuchando en el puerto " + SERVER_PORT + "...");

            // Buffers para recibir y enviar datos
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) { // Bucle infinito para manejar múltiples clientes
                // Crear un paquete para recibir datos
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket); // Esperar y recibir un paquete

                // Obtener la dirección y el puerto del cliente
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Convertir los datos recibidos en un String
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Mensaje recibido del cliente: " + message);

                // Convertir el mensaje a mayúsculas y preparar la respuesta
                String response = "Respuesta del servidor: " + message.toUpperCase();
                sendData = response.getBytes();

                // Enviar la respuesta al cliente
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }
}
