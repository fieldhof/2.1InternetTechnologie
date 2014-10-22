package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerListener extends Thread {
	
	private Socket socket;

	public ServerListener(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String message = reader.readLine();
				Scanner sc1 = new Scanner(message);
				
				switch(sc1.next()){
				case "getAuctions" 		: handleGetAuctions(sc1) ; break;
				case "searchAuctions" 	: handleSearchAuctions(sc1); break;
				case "getAuctionInfo" 	: handleGetAuctionInfo(sc1); break;
				case "addAuction"	  	: handleAddAuction(sc1); break;
				case "doOffer"			: handleDoOffer(sc1); break;
				case "highestOffer" 	: handleHighestOffer(sc1); break;
				case "auctionEnds"		: handleAuctionEnds(sc1); break;
				case "error"	    	: handleError(sc1); break;
				case "winner"			: handleWinner(sc1); break;
				}
				sc1.close();
			}
		} catch (IOException e1) {
			
		}
	}

	private void handleWinner(Scanner sc1) {
		System.out.println("Winnaar van de volgende veiling:");
		if(sc1.hasNextInt()){
			int auctionId = sc1.nextInt();
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.println("getAuctionInfo " + auctionId);
			writer.flush();
		}
		
	}

	//Done
	private void handleGetAuctions(Scanner sc1) {
		String result = "";
		while(sc1.hasNext()){
			sc1.useDelimiter("<>");
			String product = sc1.next();
			Scanner sc2 = new Scanner(product);
			sc2.useDelimiter(",");
			String itemId = sc2.next().replaceFirst(" ", "");
			result += "\n\nid: " + itemId;
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			result += "\nItem: " + itemName;
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
			Scanner sc2 = new Scanner(product);
			sc2.useDelimiter(",");
			String itemId = sc2.next().replaceFirst(" ", "");
			result += "\n\nId: " + itemId;
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			result += "\nItem: " + itemName;
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
			String auction = sc1.nextLine();
			Scanner sc2 = new Scanner(auction);
			sc2.useDelimiter(",");
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			System.out.println("\nItem: " + itemName);
			String auctionDesc = sc2.next();
			System.out.println("Description: " + auctionDesc);
			String highestBid = sc2.next();
			System.out.println("Highest bid: " + highestBid);
			String ended = sc2.next();
			System.out.println("Ended: " + ended);
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
		if(sc1.hasNext()){
			if(sc1.next().equals("true")){
				System.out.println("Bid has been placed");
			}else{
				System.out.println("Bid wasn't high enough");
			}
		}
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
			System.out.println(Client.longToReadableTimeLeft(sc1.nextLong()));
		}else{
			System.out.println("Auction doesn't exist");
		}
		
	}
	
	private void handleError(Scanner sc1) {
		if(sc1.hasNextLine()){
			System.out.println("Error!" + sc1.nextLine());
		}
	}
}
