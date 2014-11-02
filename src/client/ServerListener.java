package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class for ServerListener
 * This class listens to the server
 *
 */
public class ServerListener extends Thread {
	
	private Socket socket;

	/**
	 * Constructor
	 * Sets the socket variable to the given socket
	 * @param socket
	 */
	public ServerListener(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * Method that runs the listener
	 */
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				//read incoming message
				String message = reader.readLine();
				Scanner sc1 = new Scanner(message);
				
				//call the right method
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

	/**
	 * Method for handling the server response that starts with 'winner'
	 * @param sc1
	 */
	private void handleWinner(Scanner sc1) {
		System.out.println("Winnaar van de volgende veiling:");
		if(sc1.hasNextInt()){
			//get the auction id of the auction you have won
			int auctionId = sc1.nextInt();
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//get the information of the won auction
			writer.println("getAuctionInfo " + auctionId);
			writer.flush();
		}
		
	}

	/**
	 * Method for handling the server response that starts with 'getAuctions'
	 * @param sc1
	 */
	private void handleGetAuctions(Scanner sc1) {
		String result = "";
		while(sc1.hasNext()){
			sc1.useDelimiter("<>");
			//get the full product
			String product = sc1.next();
			Scanner sc2 = new Scanner(product);
			sc2.useDelimiter(",");
			//get the item id of the auction
			String itemId = sc2.next().replaceFirst(" ", "");
			result += "\n\nid: " + itemId;
			//get the name of the auction
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			result += "\nItem: " + itemName;
			//get the description of the auction
			String auctionDesc = sc2.next();
			result += "\nDescription: " + auctionDesc;
			sc2.close();
		}
		if(result.isEmpty()){//if there are nog active auctions
			System.out.println("There are no active auctions");
			return;
		}
		System.out.println(result);
	}
	
	/**
	 * Method for handling the server response that starts with 'searchAuctions'
	 * @param sc1
	 */
	private void handleSearchAuctions(Scanner sc1) {
		String result = "";
		while(sc1.hasNext()){
			sc1.useDelimiter("<>");
			//get the full auction
			String product = sc1.next();
			Scanner sc2 = new Scanner(product);
			sc2.useDelimiter(",");
			//get the auction id
			String itemId = sc2.next().replaceFirst(" ", "");
			result += "\n\nId: " + itemId;
			//get the name of the auction
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			result += "\nItem: " + itemName;
			//get the description of the auction
			String auctionDesc = sc2.next();
			result += "\nDescription: " + auctionDesc;
			//get the highest bid of the auction
			String highestBid = sc2.next();
			result += "\nHighest bid: " + highestBid;
			sc2.close();
		}
		if(result.isEmpty()){ // if there are no auctions found
			System.out.println("There were no auctions found with this keyword");
			return;
		}
		System.out.println(result);
	}
	
	/**
	 * Method for handling the server response that starts with 'getAuctionInfo'
	 * @param sc1
	 */
	private void handleGetAuctionInfo(Scanner sc1) {
		if(sc1.hasNext()){
			//get full auction
			String auction = sc1.nextLine();
			Scanner sc2 = new Scanner(auction);
			sc2.useDelimiter(",");
			//get name of auction
			String itemName = sc2.next();
			itemName = itemName.replaceFirst(" ", "");
			System.out.println("\nItem: " + itemName);
			//get description of auction
			String auctionDesc = sc2.next();
			System.out.println("Description: " + auctionDesc);
			//get highest bid of auction
			String highestBid = sc2.next();
			System.out.println("Highest bid: " + highestBid);
			//get ended, true if auction has ended, else false
			String ended = sc2.next();
			System.out.println("Ended: " + ended);
			sc2.close();
		}else{
			System.out.println("No auction found with this ID");
		}
		
	}
	
	/**
	 * Method for handling the server response that starts with 'addAuction'
	 * @param sc1
	 */
	private void handleAddAuction(Scanner sc1) {
		if(sc1.next().equals("true")){//if message is true, print 'item added'
			System.out.println("Item added");
		}else{//else print 'item not added'
			System.out.println("Item not added");
		}
	}
	
	/**
	 * Method for handling the server response that starts with 'doOffer'
	 * @param sc1
	 */
	private void handleDoOffer(Scanner sc1) {
		if(sc1.hasNext()){
			if(sc1.next().equals("true")){//if message is true, print 'bid has been placed'
				System.out.println("Bid has been placed");
			}else{//else print 'bid wasn't high enough'
				System.out.println("Bid wasn't high enough");
			}
		}
	}
	
	/**
	 * Method for handling the server response that starts with 'highestOffer'
	 * @param sc1
	 */
	private void handleHighestOffer(Scanner sc1) {
		if(sc1.hasNextInt()){//print the highest bid of the auction
			System.out.println("Highest bid: " + sc1.nextInt());
		}else{
			System.out.println("Auction doesn't exist");
		}
		
	}
	
	/**
	 * Method for handling the server response that starts with 'auctionEnds'
	 * @param sc1
	 */
	private void handleAuctionEnds(Scanner sc1) {
		if(sc1.hasNextLong()){//print the time that's left of the auction
			System.out.println(Client.longToReadableTimeLeft(sc1.nextLong()));
		}else{
			System.out.println("Auction doesn't exist");
		}
		
	}
	
	/**
	 * Method for handling the server response that starts with 'error'
	 * @param sc1
	 */
	private void handleError(Scanner sc1) {
		if(sc1.hasNextLine()){//Print the error and the message that's included
			System.out.println("Error!" + sc1.nextLine());
		}
	}
}
