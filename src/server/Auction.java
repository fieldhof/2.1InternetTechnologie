package server;

public class Auction {
	
	private String item;
	private String desc;
	private long expirationDate;
	private int highestBid;
	private Account highestBidder;
	private static int lastID = 0;
	private int id;
	
	public Auction(String item, String desc, long duration, int lowestPrice) {
		this(item, desc, duration);
		this.highestBid = lowestPrice;
	}
	
	public Auction(String item, String desc, long duration) {
		this.item = item;
		this.desc = desc;
		this.expirationDate = System.currentTimeMillis() + duration;
		this.highestBid = 0;
		id = ++lastID;
	}
	
	public String getItem() {
		return item;
	}
	
	public long getExpirationDate() {
		return expirationDate;
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

	public boolean contains(String keyword) {
		if(item.contains(keyword) || desc.contains(keyword)){
			return true;
		}
		return false;
	}

}
