package test;

import static org.junit.Assert.*;
import model.Account;
import model.Auction;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for Auction class
 */
public class AuctionTest {
	Auction auctionOne, auctionTwo,auctionThree;
	Account account;

	@Before
	public void setUp() throws Exception {
		auctionOne = new Auction("macbook", "laptop", 3000000);
		auctionTwo = new Auction("imac", "desktop", 3000000,20);
		auctionThree = new Auction("imac", "desktop", 1000,20);
		account = new Account("marco","jansen");
	}

	@Test
	public void testMakeAuction() {
		Auction auction = new Auction("macbook", "laptop", 3000000);
		assertEquals("macbook", auction.getItem());
		assertEquals("laptop", auction.getDesc());
		assertEquals(3000000, auction.getExpirationDate() - System.currentTimeMillis());
		assertEquals(0,auction.getHighestBid());
		assertNull(auction.getHighestBidder());
		
		assertNotEquals("windows", auction.getItem());
		assertNotEquals("desktop", auction.getDesc());
		assertNotEquals(2000000, auction.getExpirationDate() - System.currentTimeMillis());
		assertNotEquals(1, auction.getHighestBid());

	}
	
	@Test
	public void testMakeAuctionWithLowestPrice() throws Exception {
		Auction auction2 = new Auction("imac", "computer", 3000000, 20);
		assertEquals("imac", auction2.getItem());
		assertEquals("computer", auction2.getDesc());
		assertEquals(3000000, auction2.getExpirationDate() - System.currentTimeMillis());
		assertEquals(20,auction2.getHighestBid());
		assertNull(auction2.getHighestBidder());
		
		assertNotEquals("windows", auction2.getItem());
		assertNotEquals("laptop", auction2.getDesc());
		assertNotEquals(2000000, auction2.getExpirationDate() - System.currentTimeMillis());
		assertNotEquals(15, auction2.getHighestBid());
	}
	
	@Test
	public void testSetHighestBid() throws Exception {
		//Test without minimum bid
		assertNull(auctionOne.getHighestBidder());
		assertEquals(0, auctionOne.getHighestBid());
		
		assertTrue(auctionOne.setHighestBid(15, account));
		
		assertEquals(account, auctionOne.getHighestBidder());
		assertEquals(15, auctionOne.getHighestBid());
		
		//Test with minimum bid
		assertNull(auctionTwo.getHighestBidder());
		assertEquals(20, auctionTwo.getHighestBid());
		
		assertTrue(auctionTwo.setHighestBid(25, account));
		
		assertEquals(account, auctionTwo.getHighestBidder());
		assertEquals(25, auctionTwo.getHighestBid());
	}
	
	@Test
	public void testContains() throws Exception {
		assertTrue(auctionOne.contains("macbook"));
		assertTrue(auctionOne.contains("laptop"));
		assertFalse(auctionOne.contains("desktop"));
	}
	
	@Test
	public void testHasEnded() throws Exception {
		assertFalse(auctionOne.hasEnded());
		assertFalse(auctionThree.hasEnded());
		
		Thread.sleep(1000);
		assertFalse(auctionOne.hasEnded());
		assertTrue(auctionThree.hasEnded());
	}

}
