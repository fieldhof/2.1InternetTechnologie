package server;

public class Auction {
	
	private String item;
	private String desc;
	private long expirationDate;
	private int lowestPrice;
	private int highestBid;
	private Account highestBidder;
	private static int lastID = 0;
	private int id;
	
	public Auction(String item, String desc, int lowestPrice) {
		this(item, desc);
		this.lowestPrice = lowestPrice;
		this.highestBid = lowestPrice;
	}
	
	public Auction(String item, String desc) {
		this.item = item;
		this.desc = desc;
		this.highestBid = 0;
		id = ++lastID;
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
	
	public String getDesc() {
		return desc;
	}
	
	public int getId() {
		return id;
	}

}
