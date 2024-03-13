package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Cambia la dirección IP si es necesario
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor.");

            while (true) {
                System.out.println("Ingrese su acción (nueva:nombre, repartir#hash, pedir#hash, plantarse#hash, fin#hash):");
                String userInputLine = userInput.readLine();
                if (userInputLine == null || userInputLine.isEmpty()) {
                	System.out.println("Finalizado");
                	break;
                }
                out.println(userInputLine);

                String serverResponse = in.readLine();
                System.out.println("Respuesta del servidor:");
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
