package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

	private ServerSocket serverSocket;
	private static final int SERVER_PORT = 8080;
	ArrayList<Socket> clients = new ArrayList<Socket>();

	public Server() {
		System.out.println("Server starting up...");
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			System.out.println("Server Socket couldn't be made");
			e.printStackTrace();
		}
		System.out.println("Server is ready...");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				clients.add(socket);
				new ClientThread(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Als er een verbinding tot stand is gebracht, start een nieuwe thread.
	public class ClientThread extends Thread {
		private Socket threadSocket;
		private String name;
		private PrintWriter writer;
		private boolean connected = true;

		
		public ClientThread(Socket socket) {
			this.threadSocket = socket;
		}

		public void run() {
			               
			try {
				//first contact
				BufferedReader reader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
				writer = new PrintWriter(threadSocket.getOutputStream());
				name = reader.readLine();
				writer.println("Hello");
				writer.flush();
				System.out.println(name + " connected");
				
				
				while(connected){
					String message = "";
					try{
						message = reader.readLine();
					}catch(SocketException e){
						System.out.println(name + " left the auction");
						return;
					}
					Scanner sc = new Scanner(message);
					String response = "";
					switch(sc.next()){
					case "1": response = getAuctions(); break;
					case "3": response = searchAuctions() ; break;
					case "2": response = getAuctionInfo(); break;
					case "4": response = addAuction(); break;
					case "5": response = doOffer(); break;
					case "6": response = highestOffer(); break;
					case "7": response = auctionEnds(); break;
					}
					
					if(!response.isEmpty()){
						writer.println(response);
						writer.flush();
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
