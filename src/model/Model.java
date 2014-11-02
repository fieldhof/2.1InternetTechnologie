package model;

import java.util.ArrayList;

/**
 * Model
 * Has a list of auctions and accounts
 */
public class Model {
	
	private ArrayList<Auction> activeAuctions, endedAuctions;
	private ArrayList<Account> accounts;
	
	/**
	 * Constructor, creates the arraylists and model with hardcoded accounts and auctions.
	 */
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
	
	/**
	 * Returns all auctions (Active and ended)
	 * @return	All auctions
	 */
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
	
	/**
	 * Gets auction that matches the auction id(Parameter)
	 * @param auctionId		Auction id
	 * @return				Auction with the given auctionid or null if it doesn't exist.
	 */
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
	
	/**
	 * Checks if an auction with given auction id (parameter) exists.
	 * @param auctionId		Auction id
	 * @return				True if auction exists, null if auction doesn't exist.
	 */
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
	
	
	/**
	 * Gets account with given username and password uses Account.isThisAccount method
	 * @param username		Username
	 * @param password		Password
	 * @return				Account if account exists. Null if account doesn't exist.
	 */
	public Account getAccount(String username, String password){
		for(Account account : accounts){
			if(account.isThisAccount(username, password)){
				return account;
			}
		}
		return null;
	}

}
