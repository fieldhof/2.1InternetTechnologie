package server;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Auctionwatcher
 * Checks every second if an auction has expired.
 */
public class AuctionWatcher extends Thread {
	
	private Model model;

	/**
	 * Constructor to create an Auction Watcher
	 * @param model		Model
	 */
	public AuctionWatcher(Model model) {
		this.model = model;
	}
	
	/**
	 * Checks every second if an auction has ended. 
	 * If an auction has ended it moves the auction from the active auction list to ended auction list.
	 * Also sends winner to server with the auction id that was ended.
	 */
	@Override
	public void run() {
		while(true){
			for(int i = 0; i < model.getActiveAuctions().size(); i++){
				Auction auction = model.getActiveAuctions().get(i);
				if(auction.hasEnded()){
					model.getEndedAuctions().add(model.getActiveAuctions().remove(i));
					i--;
					if(auction.getHighestBidder()!= null){
						PrintWriter writer = null;
						try {
							writer = new PrintWriter(auction.getHighestBidder().getSocket().getOutputStream());
						} catch (IOException e) {
							e.printStackTrace();
						}
						writer.println("winner " + auction.getId());
						writer.flush();
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
