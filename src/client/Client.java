package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8080;
	
	public Socket socket;
	private boolean connected = false;
	private PrintWriter writer;
	public String username; 
	private String password;
	private Scanner sc;
	private boolean loggedIn = false;

	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		while(!loggedIn) {
			sc = new Scanner(System.in);
			System.out.println("Username:");
			username = sc.nextLine();
			System.out.println("Password:");
			password = sc.nextLine();
			
			System.out.println("Client connecting to server...");
			try {
				socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Client is connected to server");
			writer.println(username);
			writer.println(password);
			writer.flush();
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String message;
				message = reader.readLine();
				Scanner sc1 = new Scanner(message);
				if (sc1.next().equals("Hello")) {
					loggedIn=true;
				}
				sc1.close();
			} catch (IOException e1) {
				connected = false;
			}
			
			sc.close();
			
		}
		System.out.println("Connection is working");
		connected = true;
		
		ServerListener listener = new ServerListener();
		listener.start();
		
		while (connected) {
			String message = sc.nextLine();
			String request = "";
			switch (message) {
			case "1": request = "getAuctions"; break;
			case "3": request = searchAuctions() ; break;
			case "2": request = getAuctionInfo(); break;
			case "4": request = addAuction(); break;
			case "5": request = doOffer(); break;
			case "6": request = highestOffer(); break;
			case "7": request = auctionEnds(); break;
			
			case "x": connected = false; break;
			default:  wrongInput(); break;
			}
			
			if(!request.isEmpty()){
				writer.println(request);
				writer.flush();
			}
		}
	}


	private String searchAuctions() {
		System.out.println("Keyword for search:");
		return "searchAuctions " + sc.nextLine();
	}
	
	private String getAuctionInfo() {
		System.out.println("Auction ID:");
		return "getAuctionInfo " + sc.nextLine();
	}
	
	private String addAuction() {
		String result = "addAuction ";
		System.out.println("Item name:");
		result += sc.nextLine() + "<>";
		System.out.println("Short item description:");
		result += sc.nextLine();
		return result;
	}
	
	private String doOffer() {
		String result = "";
		System.out.println("Auction ID:");
		result += sc.nextLine() + "|";
		System.out.println("New offer:");
		String offer = sc.nextLine();
		while(!isInteger(offer)){
			System.out.println("Not a number, please try again");
			offer = sc.nextLine();
		}
		return result;
	}
	
	private String highestOffer() {
		System.out.println("Auction ID:");
		return "highestOffer " + sc.nextLine();
	}
	
	private String auctionEnds() {
		System.out.println("Auction ID:");
		return "auctionEnds " + sc.nextLine();		
	}
	
	private void wrongInput() {
		System.out.println("Wrong input, please try again");
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

	public class ServerListener extends Thread {
		public void run() {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String message;
				while (connected) {
					message = reader.readLine();
					Scanner sc1 = new Scanner(message);
					
					switch(sc1.next()){
					case "Hello" : System.out.println("Connection is working");break;
					case "badLogin" : handleBadLogin();break;
					case "getAuctions" : handleGetAuctions(sc1) ; break;
					case "searchAuctions" : handleSearchAuctions(sc1); break;
					case "getAuctionInfo" : handleGetAuctionInfo(sc1); break;
					case "addAuction"	  : handleAddAuction(sc1); break;
					case "doOffer"  : handleDoOffer(sc1); break;
					case "highestOffer" : handleHighestOffer(sc1); break;
					case "auctionEnds"	: handleAuctionEnds(sc1); break;
					case "error"	    : handleError(sc1); break;
					}
					sc1.close();
				}
			} catch (IOException e1) {
				connected = false;
			}
		}
		
		private void handleBadLogin() {
			sc = new Scanner(System.in);
			System.out.println("Username:");
			username = sc.nextLine();
			System.out.println("Password:");
			password = sc.nextLine();
			
			System.out.println("Client connecting to server...");
			try {
				socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Client is connected to server");
			writer.println(username);
			writer.println(password);
			writer.flush();
		}

		private void handleGetAuctions(Scanner sc1) {
			while(sc1.hasNext()){
				sc1.useDelimiter("<>");
				String product = sc1.next();
				Scanner sc2 = new Scanner(product);
				sc2.useDelimiter(",");
				String itemName = sc2.next();
				System.out.println("Item: " + itemName);
				String auctionDesc = sc2.next();
				System.out.println("Description: " + auctionDesc + "\n");
				sc2.close();
			}
		}
		
		private void handleSearchAuctions(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		private void handleGetAuctionInfo(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		private void handleAddAuction(Scanner sc1) {
			if(sc1.next().equals("true")){
				System.out.println("Item toegevoegd");
			}
		}
		
		private void handleDoOffer(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		private void handleHighestOffer(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		private void handleAuctionEnds(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		private void handleError(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}



		
	}

}
