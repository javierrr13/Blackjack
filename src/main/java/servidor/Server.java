package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	public static void main(String args[]) throws IOException {
		ExecutorService es = Executors.newFixedThreadPool(100);
		ServerSocket ss = new ServerSocket(9999);
		
		ss.setSoTimeout(60000);
		
		System.out.println("Server escuchando puerto 9999\n");
		Handler p = new Handler();
		while (true) {
			es.submit(new ServiceTask(ss.accept(),p));
		}

	}
}