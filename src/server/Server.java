package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.Model;

/**
 * 
 * Server class
 * Creates a server that listens to incoming messages
 *
 */
public class Server {

	private ServerSocket serverSocket;
	private static final int SERVER_PORT = 8080;
	private Model model;

	/**
	 * Constructor
	 * Creates a new Model object which contains all the auctions and accounts
	 */
	public Server() {
		model = new Model();
		initServer();
	}
	
	/**
	 * Method for initialising the server
	 */
	public void initServer(){
		System.out.println("Server starting up...");
		try {
			//Sets up the serversocket which will be used to listen to incoming clients
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Server Socket couldn't be made");
			e.printStackTrace();
		}
		System.out.println("Server is ready...");
		//Start a new thread that watches the auctions
		new AuctionWatcher(model).start();
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				//Start a new thread with the right socket that listens to the client
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
