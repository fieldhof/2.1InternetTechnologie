package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientListener extends Thread {
	private Socket threadSocket;
	private Account account;
	private PrintWriter writer;
	private boolean connected = false;
	private Model model;

	
	public ClientListener(Socket socket, Model model) {
		this.threadSocket = socket;
		this.model = model;
	}

	public void run() {
		               
		try {
			//first contact
			BufferedReader reader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
			writer = new PrintWriter(threadSocket.getOutputStream());
			while(!connected){
				String username = reader.readLine();
				String password = reader.readLine();
				this.account = model.getAccount(username, password);
				if (account == null) {
					writer.println("badLogin");
					writer.flush();
				} else {
					account.setSocket(threadSocket);
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

	//Done
	private String doOffer(Scanner sc) {
		String result = "doOffer ";
		sc.useDelimiter("<>");
		sc.skip(" ");
		if(sc.hasNextInt()){
			int auctionId = sc.nextInt();
			if(model.isAuction(auctionId)){
				Auction auction = model.getAuction(auctionId);
				if(!auction.hasEnded()){
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
				return "error auction " + auction.getId() + " has ended";
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
			if(model.isAuction(auctionId)){
				return result + model.getAuction(auctionId).getExpirationDate();
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
			if(model.isAuction(auctionId)){
				Auction auction = model.getAuction(auctionId);
				if(!auction.hasEnded()){
					return result + model.getAuction(auctionId).getHighestBid();
				}
				return "error auction " + auctionId + " has ended";
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
			if(model.isAuction(auctionId)){
				Auction auction = model.getAuction(auctionId);
				return result + auction.getItem() + "," + auction.getDesc() + "," + auction.getHighestBid() + "," + auction.hasEnded();
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
			for(Auction auction : model.getActiveAuctions()){
				if(auction.contains(keyword) && !auction.hasEnded()){
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
						model.getActiveAuctions().add(new Auction(itemName, itemDesc, itemDuration, lowestPrice));
					}else{
						model.getActiveAuctions().add(new Auction(itemName, itemDesc, itemDuration));
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
		for(Auction auction : model.getActiveAuctions()){
			if(!auction.hasEnded()){
				result += auction.getId() + "," + auction.getItem() + "," + auction.getDesc() + "<>";
			}
		}
		return result;
	}
	
	
}
