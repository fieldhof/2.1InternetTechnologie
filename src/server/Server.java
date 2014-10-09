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
	ArrayList<Account> accounts = new ArrayList<Account>();

	public Server() {
		auctions.add(new Auction("Macbook", "laptop", 60000));
		auctions.add(new Auction("Lenovo", "laptop", 60000));
		auctions.add(new Auction("iMac", "scherm + computer", 60000));
		auctions.add(new Auction("Mac", "prullenbak", 60000));
		accounts.add(new Account("marco", "jansen"));
		accounts.add(new Account("daan", "veldhof"));
		accounts.add(new Account("paul", "degroot"));
		
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
	
	private Auction getAuction(int auctionId){
		for(Auction auction : auctions){
			if(auction.getId() == auctionId){
				return auction;
			}
		}
		return null;
	}
	
	private boolean isAuction(int auctionId){
		for(Auction auction : auctions){
			if(auction.getId() == auctionId){
				return true;
			}
		}
		return false;
	}
	
	// Als er een verbinding tot stand is gebracht, start een nieuwe thread.
	public class ClientThread extends Thread {
		private Socket threadSocket;
		private Account account;
		private PrintWriter writer;
		private boolean connected = false;

		
		public ClientThread(Socket socket) {
			this.threadSocket = socket;
		}

		public void run() {
			               
			try {
				//first contact
				BufferedReader reader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
				writer = new PrintWriter(threadSocket.getOutputStream());
				while(!connected){
					String username = reader.readLine();
					String password = reader.readLine();
					for (Account account : accounts) {
						if (account.isThisAccount(username, password)) {
							this.account = account;
						}
					}
					if (account == null) {
						writer.println("badLogin");
						writer.flush();
					} else {
						connected = true;
						writer.println("Hello");
						writer.flush();
						System.out.println(username + " connected");
					}
				}
				
				while(connected){
					String message = "";
					try{
						message = reader.readLine();
					}catch(SocketException e){
						System.out.println(account.getUsername() + " left the auction");
						return;
					}
					System.out.println(message);
					Scanner sc = new Scanner(message);
					String function = sc.next();
					String response = "";
					switch(function){
					case "getAuctions": 	response = getAuctions(); break;
					case "getAuctionInfo": 	response = getAuctionInfo(sc); break;
					case "searchAuctions": 	response = searchAuctions(sc) ; break;
					case "addAuction": 		response = addAuction(sc); break;
					case "doOffer": 		response = doOffer(sc); break;
					case "highestOffer": 	response = highestOffer(sc); break;
					case "auctionEnds": 	response = auctionEnds(sc); break;
					default: 				response = "error no valid command";
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

		
		private String doOffer(Scanner sc) {
			String result = "doOffer ";
			sc.useDelimiter("<>");
			if(sc.hasNextInt()){
				int auctionId = sc.nextInt();
				if(isAuction(auctionId)){
					Auction auction = getAuction(auctionId);
					if(sc.hasNextInt()){
						int price = sc.nextInt();
						if(price > auction.getHighestBid()){
							auction.setHighestBid(price, account);
							return result + "true";
						}
						return result + "false";
					}
					return "error No valid price given";
				}
				return "error Auction doesn't exist";
			}
			return "error No valid arguments were given";
		}

		//Done
		private String auctionEnds(Scanner sc) {
			String result = "auctionEnds ";
			if(sc.hasNextInt()){
				int auctionId = sc.nextInt();
				if(isAuction(auctionId)){
					return result + getAuction(auctionId).getExpirationDate();
				}
				return "error auction " + auctionId + " doesn't exist";
			}
			return "error no valid auction id";
		}

		//Done
		private String highestOffer(Scanner sc) {
			String result = "highestOffer ";
			if(sc.hasNextInt()){
				int auctionId = sc.nextInt();
				if(isAuction(auctionId)){
					return result + getAuction(auctionId).getHighestBid();
				}
				return "error auction " + auctionId + " doesn't exist";
			}
			return "error No valid auction id";
		}

		//Done
		private String getAuctionInfo(Scanner sc) {
			String result = "getAuctionInfo ";
			if(sc.hasNextInt()){
				int auctionId = sc.nextInt();
				if(isAuction(auctionId)){
					Auction auction = getAuction(auctionId);
					return result + auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid();
				}
				return "error auction " + auctionId + " doesn't exist";
			}
			return "error No valid auction id";
		}

		//Done
		private String searchAuctions(Scanner sc) {
			if(sc.hasNext()){
				String keyword = sc.next();
				String result = "searchAuctions ";
				for(Auction auction : auctions){
					if(auction.contains(keyword)){
						result += auction.getId() + "," + auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid() + "<>";
					}
				}
				return result;
			}
			return "error No keyword";
		}

		//Done
		private String addAuction(Scanner sc) {
			sc.useDelimiter("<>");
			if(sc.hasNext()){
				String itemName = sc.next();
				if(sc.hasNext()){
					String itemDesc = sc.next();
					if(sc.hasNextLong()){
						long itemDuration = sc.nextLong();
						if(sc.hasNextInt()){
							int lowestPrice = sc.nextInt();
							auctions.add(new Auction(itemName, itemDesc, itemDuration, lowestPrice));
						}else{
							auctions.add(new Auction(itemName, itemDesc, itemDuration));
						}
						return "addAuction true";
					}
				}
			}
			return "error no valid parameters for addAuction";
		}
		//Done
		private String getAuctions() {
			String result = "getAuctions ";
			for(Auction auction : auctions){
				result += auction.getId() + "," + auction.getItem() + "," + auction.getDesc() + "<>";
			}
			return result;
		}
		
		
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
