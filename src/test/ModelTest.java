package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.Model;

/**
 * Unit test for model
 */
public class ModelTest {
	Model model;

	@Before
	public void setUp() throws Exception {
		model = new Model();
	}

	@Test
	public void testModel() {
		Model model = new Model();
		assertNotNull(model);
		assertEquals(4, model.getAuctions().size());
		assertEquals(4, model.getActiveAuctions().size());
		assertEquals(4, model.getAccounts().size());
		assertEquals(0, model.getEndedAuctions().size());
		
		//test get auction by id (ids are static so must test it here)
		//set up takes ids 1-4. so 5-8 are auctions in this case
		assertNotNull(model.getAuction(5));
		assertNull(model.getAuction(0));
		assertTrue(model.isAuction(5));
		assertFalse(model.isAuction(0));
	}
	
	@Test
	public void testGetAccount() throws Exception {
		assertNotNull(model.getAccount("marco", "jansen"));
		assertNull(model.getAccount("piet", "klaas"));
	}

}
