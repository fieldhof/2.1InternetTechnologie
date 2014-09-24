package server;

public class Auction {
	
	private String item;
	private long expirationDate;
	private int lowestPrice;
	private int highestBid;
	private Account highestBidder;
	
	public Auction(String item, long expirationDate, int lowestPrice) {
		this(item, expirationDate);
		this.lowestPrice = lowestPrice;
		this.highestBid = lowestPrice;
	}
	
	public Auction(String item, long expirationDate) {
		this.item = item;
		this.expirationDate = expirationDate;
		this.highestBid = 0;
	}
	
	public String getItem() {
		return item;
	}
	
	public long getExpirationDate() {
		return expirationDate;
	}
	
	public int getLowestPrice() {
		return lowestPrice;
	}
	
	public int getHighestBid() {
		return highestBid;
	}
	
	public boolean setHighestBid(int highestBid, Account highestBidder) {
		if(highestBid > this.highestBid){
			this.highestBid = highestBid;
			this.highestBidder = highestBidder;
			return true;
		}
		return false;
	}
	
	public Account getHighestBidder() {
		return highestBidder;
	}

}
