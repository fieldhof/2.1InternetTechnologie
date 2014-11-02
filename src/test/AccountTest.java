package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.Account;

public class AccountTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		Account account = new Account("Piet","Klaas");
	}

}
