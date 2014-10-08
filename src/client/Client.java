package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
			case "2": request = getAuctionInfo(); break;
			case "3": request = searchAuctions() ; break;
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

	//Done
	private String getAuctionInfo() {
		System.out.println("Auction ID:");
		return "getAuctionInfo " + sc.nextLine();
	}
	
	//Done
	private String searchAuctions() {
		System.out.println("Keyword for search:");
		return "searchAuctions " + sc.nextLine();
	}
	
	//Done
	private String addAuction() {
		String result = "addAuction ";
		System.out.println("Item name:");
		result += sc.nextLine() + "<>";
		System.out.println("Short item description:");
		result += sc.nextLine() + "<>";
		System.out.println("Duration in minutes:");
		String input = sc.nextLine();
		while(!isInteger(input)){
			System.out.println("Not a number, try again: ");
			input = sc.nextLine();
		}
		result += (Long.parseLong(input) * 60000L);
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
	
	//Done
	private String highestOffer() {
		System.out.println("Auction ID:");
		return "highestOffer " + sc.nextLine();
	}
	
	//Done
	private String auctionEnds() {
		System.out.println("Auction ID:");
		return "auctionEnds " + sc.nextLine();		
	}
	
	//Done
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

	public static String longToDate(long date){
		long dif = date - System.currentTimeMillis();
		int hours = (int) (dif / 360000);
		dif = dif % 360000;
		int min = (int) (dif / 60000);
		dif = dif % 60000;
		int sec = (int) (dif / 1000);
		String result = "";
		if(hours > 0)	{result += "Hours: " + hours;}
		if(min > 0)		{result += "\nMinutes: " + min;}
		if(sec > 0)		{result += "\nSeconds: " + sec;}
		return 	result;
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
					case "getAuctions" 		: handleGetAuctions(sc1) ; break;
					case "searchAuctions" 	: handleSearchAuctions(sc1); break;
					case "getAuctionInfo" 	: handleGetAuctionInfo(sc1); break;
					case "addAuction"	  	: handleAddAuction(sc1); break;
					case "doOffer"			: handleDoOffer(sc1); break;
					case "highestOffer" 	: handleHighestOffer(sc1); break;
					case "auctionEnds"		: handleAuctionEnds(sc1); break;
					case "error"	    	: handleError(sc1); break;
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

		//Done
		private void handleGetAuctions(Scanner sc1) {
			String result = "";
			while(sc1.hasNext()){
				sc1.useDelimiter("<>");
				String product = sc1.next();
				product = product.replaceFirst(" ", "");
				Scanner sc2 = new Scanner(product);
				sc2.useDelimiter(",");
				String itemName = sc2.next();
				itemName = itemName.replaceFirst(" ", "");
				result += "\n\nItem: " + itemName;
				String auctionDesc = sc2.next();
				result += "\nDescription: " + auctionDesc;
				sc2.close();
			}
			if(result.isEmpty()){
				System.out.println("There are no active auctions");
				return;
			}
			System.out.println(result);
		}
		
		//Done
		private void handleSearchAuctions(Scanner sc1) {
			String result = "";
			while(sc1.hasNext()){
				sc1.useDelimiter("<>");
				String product = sc1.next();
				product = product.replaceFirst(" ", "");
				Scanner sc2 = new Scanner(product);
				sc2.useDelimiter(",");
				String itemName = sc2.next();
				itemName = itemName.replaceFirst(" ", "");
				result += "\n\nItem: " + itemName;
				String auctionDesc = sc2.next();
				result += "\nDescription: " + auctionDesc;
				String highestBid = sc2.next();
				result += "\nHighest bid: " + highestBid;
				sc2.close();
			}
			if(result.isEmpty()){
				System.out.println("There were no auctions found with this keyword");
				return;
			}
			System.out.println(result);
		}
		
		//Done
		private void handleGetAuctionInfo(Scanner sc1) {
			if(sc1.hasNext()){
				String auction = sc1.next();
				auction = auction.replace(" ", "");
				Scanner sc2 = new Scanner(auction);
				sc2.useDelimiter(",");
				String itemName = sc2.next();
				itemName = itemName.replaceFirst(" ", "");
				System.out.println("\nItem: " + itemName);
				String auctionDesc = sc2.next();
				System.out.println("Description: " + auctionDesc);
				String highestBid = sc2.next();
				System.out.println("Highest bid: " + highestBid);
				sc2.close();
			}else{
				System.out.println("No auction found with this ID");
			}
			
		}
		
		//Done
		private void handleAddAuction(Scanner sc1) {
			if(sc1.next().equals("true")){
				System.out.println("Item added");
			}else{
				System.out.println("Item not added");
			}
		}
		
		private void handleDoOffer(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}
		
		//Done
		private void handleHighestOffer(Scanner sc1) {
			if(sc1.hasNextInt()){
				System.out.println("Highest bid: " + sc1.nextInt());
			}else{
				System.out.println("Auction doesn't exist");
			}
			
		}
		
		//Done
		private void handleAuctionEnds(Scanner sc1) {
			if(sc1.hasNextLong()){
				System.out.println(longToDate(sc1.nextLong()));
			}else{
				System.out.println("Auction doesn't exist");
			}
			
		}
		
		private void handleError(Scanner sc1) {
			// TODO Auto-generated method stub
			
		}



		
	}

}
