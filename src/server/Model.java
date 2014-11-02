package server;

import java.util.ArrayList;

public class Model {
	
	private ArrayList<Auction> activeAuctions, endedAuctions;
	private ArrayList<Account> accounts;
	
	public Model() {
		activeAuctions = new ArrayList<Auction>();
		endedAuctions = new ArrayList<Auction>();
		accounts = new ArrayList<Account>();
		
		activeAuctions.add(new Auction("Macbook", "laptop", 60000));
		activeAuctions.add(new Auction("Lenovo", "laptop", 60000));
		activeAuctions.add(new Auction("iMac", "scherm + computer", 60000));
		activeAuctions.add(new Auction("Mac", "prullenbak", 60000));
		accounts.add(new Account("marco", "jansen"));
		accounts.add(new Account("daan", "veldhof"));
		accounts.add(new Account("paul", "degroot"));
		accounts.add(new Account("test", "server"));
	}
	
	public ArrayList<Auction> getAuctions(){
		ArrayList<Auction> result = new ArrayList<Auction>();
		result.addAll(endedAuctions);
		result.addAll(activeAuctions);
		return result;
	}
	
	public ArrayList<Auction> getEndedAuctions() {
		return endedAuctions;
	}
	
	public ArrayList<Auction> getActiveAuctions() {
		return activeAuctions;
	}
	
	public Auction getAuction(int auctionId){
		for(Auction auction : activeAuctions){
			if(auction.getId() == auctionId){
				return auction;
			}
		}
		for(Auction auction : endedAuctions){
			if(auction.getId() == auctionId){
				return auction;
			}
		}
		return null;
	}
	
	public boolean isAuction(int auctionId){
		for(Auction auction : activeAuctions){
			if(auction.getId() == auctionId){
				return true;
			}
		}
		for(Auction auction : endedAuctions){
			if(auction.getId() == auctionId){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public Account getAccount(String username, String password){
		for(Account account : accounts){
			if(account.isThisAccount(username, password)){
				return account;
			}
		}
		return null;
	}

}
