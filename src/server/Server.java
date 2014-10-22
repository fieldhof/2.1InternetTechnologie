package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket;
	private static final int SERVER_PORT = 8080;
	private Model model;

	public Server() {
		model = new Model();
		initServer();
	}
	
	public void initServer(){
		System.out.println("Server starting up...");
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Server Socket couldn't be made");
			e.printStackTrace();
		}
		System.out.println("Server is ready...");
		new AuctionWatcher(model).start();
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new ClientListener(socket, model).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
