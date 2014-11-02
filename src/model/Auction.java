package model;


/**
 * Auction
 * Represents an auction.
 */
public class Auction {
	
	private String item, desc;
	private long expirationDate;
	private int highestBid, id;
	private Account highestBidder;
	private static int lastID = 0;
	
	
	/**
	 * Constructor to make an auction with minimum bid.
	 * @param item				Item name
	 * @param desc				Item description
	 * @param duration			Duration of the auction
	 * @param lowestPrice		Minimum bid of the auction
	 */
	public Auction(String item, String desc, long duration, int lowestPrice) {
		this(item, desc, duration);
		this.highestBid = lowestPrice;
	}
	
	/**
	 * Constructor to make an auction without minimum bid
	 * @param item			Item name
	 * @param desc			Item description
	 * @param duration		Duration of the auction
	 */
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
	
	/**
	 * Replaces highest bid if it is higher than the current
	 * @param highestBid		Bid
	 * @param highestBidder		Account that did the bid
	 * @return		True if the bid was higher than current, false if it was not.
	 */
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

	/**
	 * Checks if the auction name or description contains the keyword ( parameter )
	 * @param keyword	keyword you want to check
	 * @return		True if the itemname or description contains the keyword, false if not.
	 */
	public boolean contains(String keyword) {
		if(item.contains(keyword) || desc.contains(keyword)){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the auction has ended
	 * @return	True if auction has ended, false if not.
	 */
	public boolean hasEnded() {
		return expirationDate <= System.currentTimeMillis();
	}
	
	

}
