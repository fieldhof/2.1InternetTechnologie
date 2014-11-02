package test;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import server.Account;

/**
 * Unit test for Account class
 */
public class AccountTest {
	Account account1;
	Account account2;

	@Before
	public void setUp() throws Exception {
		account1 = new Account("testEen","testEen");
		account2 = new Account("testTwee","testTwee");
	}

	@Test
	public void testMakeAccount() {
		Account account = new Account("Piet","Klaas");
		assertEquals("Piet", account.getUsername());
		assertEquals("Klaas", account.getPassword());
		assertNull(account.getSocket());
		
		assertNotEquals("Klaas", account.getUsername());
		assertNotEquals("Piet", account.getPassword());
	}
	
	@Test
	public void testIsThisAccount() throws Exception {
		assertTrue(account1.isThisAccount("testEen", "testEen"));
		assertFalse(account1.isThisAccount("testTwee", "testTwee"));
		assertNull(account1.getSocket());
		
		assertTrue(account2.isThisAccount("testTwee", "testTwee"));
		assertFalse(account2.isThisAccount("testTwee", "testEen"));
	}
	
	

}
