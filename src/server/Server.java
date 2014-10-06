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
	ArrayList<Auction> auctions = new ArrayList<Auction>();

	public Server() {
		auctions.add(new Auction("Macbook", "laptop"));
		auctions.add(new Auction("Lenovo", "laptop"));
		auctions.add(new Auction("iMac", "scherm + computer"));
		auctions.add(new Auction("Mac", "prullenbak"));
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

	private boolean isInteger(String input){
		try{
			Integer.parseInt(input);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
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
					String function = sc.next();
					String response = function + " ";
					switch(function){
					case "getAuctions": response += getAuctions(); break;
					case "getAuctionInfo": response += getAuctionInfo(sc); break;
					case "searchAuctions": response += searchAuctions(sc) ; break;
					case "addAuction": response += addAuction(sc); break;
//					case "5": response = doOffer(); break;
//					case "6": response = highestOffer(); break;
//					case "7": response = auctionEnds(); break;
					
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

		//Done
		private String getAuctionInfo(Scanner sc) {
			String auctionID = sc.next();
			String result = "";
			if(isInteger(auctionID)){
				int id = Integer.parseInt(auctionID);
				for(Auction auction : auctions){
					if(auction.getId() == id){
						result += auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid();
						break;
					}
				}
			}
			return result;
		}

		//Done
		private String searchAuctions(Scanner sc) {
			String keyword = sc.next();
			String result = "";
			for(Auction auction : auctions){
				if(auction.contains(keyword)){
					result += auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid() + "<>";
				}
			}
			return result;
		}

		//Done
		private String addAuction(Scanner sc) {
			sc.useDelimiter("<>");
			String itemName = sc.next();
			String itemDesc = sc.next();
			auctions.add(new Auction(itemName, itemDesc));
			return "true";
		}

		//Done
		private String getAuctions() {
			String result = "";
			for(Auction auction : auctions){
				result += auction.getItem() + "," + auction.getDesc() + "<>";
			}
			return result;
		}
		
		
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
