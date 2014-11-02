package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

public class ServerTest {
	
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8080;
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public ServerTest() {
		try {
			System.out.println("Connecting to server...");
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connection working");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writer.println("test");
		writer.println("server");
		writer.flush();
		System.out.println(getMessage());
	}

	@Test
	public void getAuctionsTest() {
		sendMessage("getAuctions");
		assertEquals("getAuctions 1,Macbook,laptop<>2,Lenovo,laptop<>3,iMac,scherm + computer<>4,Mac,prullenbak<>", getMessage());
	}
	
	@Test
	public void searchAuctionsTest() {
		sendMessage("searchAuctions Macbook");
		assertEquals("searchAuctions 1,Macbook,laptop,0<>", getMessage());
	}
	
	@Test
	public void doOfferTest(){
		sendMessage("doOffer 1<>15");
		assertEquals("doOffer true", getMessage());
	}
	
	@Test
	public void highestOfferTest(){
		sendMessage("highestOffer 1");
		System.out.println();
		assertTrue(getMessage().startsWith("highestOffer"));
	}
	
	@Test
	public void auctionEndsTest() {
		sendMessage("auctionEnds 1");
		assertTrue(getMessage().startsWith("auctionEnds"));
	}
	
	private void sendMessage(String message){
		writer.println(message);
		writer.flush();
	}
	
	private String getMessage(){
		try{
			return reader.readLine();
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
}
