package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;



import org.fp.dam.naipes.blackjack.BlackjackPedirException;
import org.fp.dam.naipes.blackjack.BlackjackPlantarseException;
import org.fp.dam.naipes.blackjack.BlackjackRepartirException;

public class ServiceTask implements Runnable {
	
	private Socket socket;
	private Handler partidas;
	private String logtime;

	public ServiceTask(Socket socket, Handler p) throws SocketException {;
		this.socket = socket;
		this.partidas = p;
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");
		logtime = form.format(new Date());
	}

	@Override
	public void run() {
		
		long startTime = System.currentTimeMillis();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {
			String line;
			System.out.println("-----------[+]" + socket.getInetAddress() + "[+]--------------\n");
					
			line = in.readLine().toLowerCase();
			System.out.println("");
			System.out.println("[  " + logtime +" | " + socket.getInetAddress().getHostAddress() +" | Peticion Recibida ] > " + line);
			peticiones(line, out);
		} catch (SocketTimeoutException e) {
			System.err.println("[!] TIMEOUT: " + e.getLocalizedMessage() + "(" + socket.getInetAddress() + ")");
		} catch (IOException e) {
			System.err.println("[!] ERROR: " + e.getLocalizedMessage() + "(" + socket.getInetAddress() + ")");
		}

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        System.out.println("[Tiempo de respuesta: " + responseTime + " ms]\n");
	}

	
	public void peticiones(String peticion, PrintWriter out) throws SocketException {

	    if (peticion.startsWith("nueva:")) {
	        String hash = partidas.crear();
	        enviar(out, "OK#" + hash);
	    }
	    
	    else if (peticion.startsWith("repartir#")) {
	        try {
	            String hash = peticion.split("#")[1];
	            partidas.get(hash).repartir();
	            enviar(out, partidas.get(hash).toString());
	        } catch (BlackjackRepartirException e) {
	            enviar(out, "ERROR:Error al repartir");
	        }
	    }

	    else if (peticion.startsWith("pedir#")) {
	        try {
	            String hash = peticion.split("#")[1];
	            partidas.get(hash).pedir();
	            enviar(out, partidas.get(hash).toString());
	        } catch (BlackjackPedirException e) {
	            enviar(out, "ERROR:Error al pedir");
	        }
	    }

	    else if (peticion.startsWith("plantarse#")) {
	        try {
	            String hash = peticion.split("#")[1];
	            partidas.get(hash).plantarse();
	            enviar(out, partidas.get(hash).toString());
	        } catch (BlackjackPlantarseException e) {
	            enviar(out, "ERROR:Error al plantarse");
	        }
	    }

	    else if (peticion.startsWith("fin#")) {
	        String hash = peticion.split("#")[1];
	        partidas.borrar(hash);
	        enviar(out, "SE HA DESCONECTADO DEL SERVIDOR");
	    } else {
	        enviar(out, "Error : Sintaxis Incorrecta");
	    }
	}


	private void enviar(PrintWriter out, String msg) {
		System.out.println("[  " + logtime +" | " + socket.getInetAddress().getHostAddress() +" | Respuesta Servidor ] > " + msg + "\n");
		out.println(msg);
		out.flush();
	}
}
