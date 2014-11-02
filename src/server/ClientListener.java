package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import model.Account;
import model.Auction;
import model.Model;

/**
 * ClientListener class
 * Creates a thread that handles the incoming messages of the client
 *
 */
public class ClientListener extends Thread {
	private Socket threadSocket;
	private Account account;
	private PrintWriter writer;
	private boolean connected = false;
	private Model model;

	
	/**
	 * Constructor
	 * Sets the socket and model of the object
	 * @param socket
	 * @param model
	 */
	public ClientListener(Socket socket, Model model) {
		this.threadSocket = socket;
		this.model = model;
	}

	/**
	 * Method for running the thread
	 */
	public void run() {
		               
		try {
			//first contact
			BufferedReader reader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
			writer = new PrintWriter(threadSocket.getOutputStream());
			//while the client is not logged in, the user can try to log in
			while(!connected){
				//read username
				String username = reader.readLine();
				//read password
				String password = reader.readLine();
				this.account = model.getAccount(username, password);
				if (account == null) {//username password combination is wrong
					writer.println("badLogin");
					writer.flush();
				} else {//user is logged in
					account.setSocket(threadSocket);
					connected = true;
					writer.println("Hello");
					writer.flush();
					System.out.println(username + " connected");
				}
			}
			
			while(connected){//while the client is logged in and connected, read the messages
				String message = "";
				try{//read message
					message = reader.readLine();
				}catch(SocketException e){
					System.out.println(account.getUsername() + " left the auction");
					return;
				}
				System.out.println(message);
				Scanner sc = new Scanner(message);
				String function = sc.next();
				String response = "";
				switch(function){//call the method that corresponds with the message
				case "getAuctions": 	response = getAuctions(); break;
				case "getAuctionInfo": 	response = getAuctionInfo(sc); break;
				case "searchAuctions": 	response = searchAuctions(sc) ; break;
				case "addAuction": 		response = addAuction(sc); break;
				case "doOffer": 		response = doOffer(sc); break;
				case "highestOffer": 	response = highestOffer(sc); break;
				case "auctionEnds": 	response = auctionEnds(sc); break;
				default: 				response = "error no valid command";
				}
				
				if(!response.isEmpty()){//send the reply for the message 
					writer.println(response);
					writer.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for handling the client request that starts with 'doOffer'
	 * @param sc
	 * @return String with response message
	 */
	private String doOffer(Scanner sc) {
		String result = "doOffer ";
		sc.useDelimiter("<>");
		sc.skip(" ");
		if(sc.hasNextInt()){
			//get auction id
			int auctionId = sc.nextInt();
			if(model.isAuction(auctionId)){
				//get full auction
				Auction auction = model.getAuction(auctionId);
				if(!auction.hasEnded()){
					if(sc.hasNextInt()){
						//get bid
						int price = sc.nextInt();
						if(price > auction.getHighestBid()){//if bid is higher than highest bid
							//set the highest bid
							auction.setHighestBid(price, account);
							return result + "true";
						}
						//if bid isn't higher than highest bid return false
						return result + "false";
					}
					//if bid isn't a integer
					return "error No valid price given";
				}
				//if auction has already ended
				return "error auction " + auction.getId() + " has ended";
			}
			//if auction doesn't exist
			return "error Auction doesn't exist";
		}
		//if there are no right arguments
		return "error No valid arguments were given";
	}

	/**
	 * Method for handling the client request that starts with 'auctionEnds'
	 * @param sc
	 * @return String with response message
	 */
	private String auctionEnds(Scanner sc) {
		String result = "auctionEnds ";
		if(sc.hasNextInt()){
			//get auction id
			int auctionId = sc.nextInt();
			if(model.isAuction(auctionId)){//if auction with the given auction id exists
				//return the remaining time of the auction
				return result + model.getAuction(auctionId).getExpirationDate();
			}
			//if auction doesn't exist return error
			return "error auction " + auctionId + " doesn't exist";
		}
		//if auction id is wrong
		return "error no valid auction id";
	}

	/**
	 * Method for handling the client request that starts with 'highestOffer'
	 * @param sc 
	 * @return String with response message	
	 */
	private String highestOffer(Scanner sc) {
		String result = "highestOffer ";
		if(sc.hasNextInt()){
			//get auction id
			int auctionId = sc.nextInt();
			if(model.isAuction(auctionId)){//if auction with auction id exists
				//save auction
				Auction auction = model.getAuction(auctionId);
				if(!auction.hasEnded()){//if auction hasn't ended
					//return highest bid of auction
					return result + model.getAuction(auctionId).getHighestBid();
				}
				//if auction has ended, return error
				return "error auction " + auctionId + " has ended";
			}
			//if auction with auction id doesn't exist, return error
			return "error auction " + auctionId + " doesn't exist";
		}
		//if auction id is wrong, return error
		return "error No valid auction id";
	}

	/**
	 * Method for handling the client request that starts with 'getAuctionInfo'
	 * @param sc
	 * @return String with response message
	 */
	private String getAuctionInfo(Scanner sc) {
		String result = "getAuctionInfo ";
		if(sc.hasNextInt()){
			//get auction id
			int auctionId = sc.nextInt();
			if(model.isAuction(auctionId)){
				//save auction
				Auction auction = model.getAuction(auctionId);
				//return full auction
				return result + auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid() + "," + auction.hasEnded();
			}
			//if auction with auction id doesn't exist, return error
			return "error auction " + auctionId + " doesn't exist";
		}
		//if auction id isn't valid, return error
		return "error No valid auction id";
	}

	/**
	 * Method for handling the client request that starts with 'searchAuctions'
	 * @param sc
	 * @return String with response message
	 */
	private String searchAuctions(Scanner sc) {
		if(sc.hasNext()){
			//get keyword
			String keyword = sc.next();
			String result = "searchAuctions ";
			for(Auction auction : model.getActiveAuctions()){//run trough all auctions
				if(auction.contains(keyword) && !auction.hasEnded()){//if auction contains keyword and hasn't ended
					//add auction to result
					result += auction.getId() + "," + auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid() + "<>";
				}
			}
			return result;
		}
		//if there isn't a keyword, return error
		return "error No keyword";
	}

	/**
	 * Method for handling the client request that starts with 'addAuction'
	 * @param sc
	 * @return String with response message
	 */
	private String addAuction(Scanner sc) {
		sc.useDelimiter("<>");
		if(sc.hasNext()){
			//get name
			String itemName = sc.next();
			if(sc.hasNext()){
				//get description
				String itemDesc = sc.next();
				if(sc.hasNextLong()){
					//get duration of auction
					long itemDuration = sc.nextLong();
					if(sc.hasNextInt()){//if has startprice
						//get start price
						int lowestPrice = sc.nextInt();
						//add new auction to model
						model.getActiveAuctions().add(new Auction(itemName, itemDesc, itemDuration, lowestPrice));
					}else{
						//add new auction to model
						model.getActiveAuctions().add(new Auction(itemName, itemDesc, itemDuration));
					}
					//return true
					return "addAuction true";
				}
			}
		}
		//if there are no valid parameter, return error
		return "error no valid parameters for addAuction";
	}
	
	/**
	 * Method for handling the client request that starts with 'getAuctions'
	 * @return String with response message
	 */
	private String getAuctions() {
		String result = "getAuctions ";
		for(Auction auction : model.getActiveAuctions()){//run trough all auctions
			if(!auction.hasEnded()){//if auction hasn't ended
				//add auction to result
				result += auction.getId() + "," + auction.getItem() + "," + auction.getDesc() + "<>";
			}
		}
		return result;
	}
	
	
}
