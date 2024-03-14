package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private static final String dir = "127.0.0.1"; // 
    private static final int port = 9999;

    public static void main(String[] args) {
        try (Socket socket = new Socket(dir, port);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
        	
            System.out.println("Conectado al servidor.");
            Boolean fin = true;
            while (fin) {
                System.out.println("Ingrese su acción (nueva:nombre, repartir#hash, pedir#hash, plantarse#hash, fin#hash):");
                String userInputLine = userInput.readLine();
                if (userInputLine == null || userInputLine.isEmpty()) {
                	System.out.println("Finalizado");
                	break;
                }
                out.println(userInputLine);

                String serverResponse = in.readLine();
                System.out.println("Respuesta del servidor:\n");
                System.out.println(serverResponse);
                if(serverResponse.equals("SE HA DESCONECTADO DEL SERVIDOR"))
                	fin=false;
                	
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
