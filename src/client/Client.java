package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class
 * Creates a client that tries to connect to a server
 *
 */
public class Client {

	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8080;
	
	public Socket socket;
	private boolean connected = false;
	private PrintWriter writer;
	private BufferedReader reader;
	private Scanner sc;
	private boolean loggedIn = false;

	public static void main(String[] args) {
		new Client();
	}

	/**
	 * Constructor
	 * Creates a Client object
	 */
	public Client() {
		//Try to connect to a server
		try {
			System.out.println("Connecting to server...");
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connection working");
		} catch (IOException e) {
			//If connection couldn't be made
			System.out.println("Connection refused");
			return;
		}
		
		//while the user is not logged in, try to log in
		while(!loggedIn) {
			sc = new Scanner(System.in);
			//Ask for username
			System.out.println("Username:");
			String username = sc.nextLine();
			//Ask for password
			System.out.println("Password:");
			String password = sc.nextLine();
			
			//Send username and password to server
			writer.println(username);
			writer.println(password);
			writer.flush();
			
			//Read response from server
			try {
				String message = reader.readLine();
				Scanner sc1 = new Scanner(message);
				if (sc1.next().equals("Hello")) {
					System.out.println("Login successful");
					loggedIn = true;
				}else{
					System.out.println("Wrong username or password");
				}
				sc1.close();
			} catch (IOException e1) {
				connected = false;
			}			
		}
		System.out.println("Press h for info of all commands");
		connected = true;
		
		//start new ServerListener, which listens to the server
		ServerListener listener = new ServerListener(socket);
		listener.start();
		
		//while connected, read command from user and send the right message
		while (connected) {
			String message = sc.nextLine();
			String request = "";
			switch (message) {
			case "1": request = "getAuctions"; break;
			case "2": request = getAuctionInfo(); break;
			case "3": request = searchAuctions() ; break;
			case "4": request = addAuction(); break;
			case "5": request = doOffer(); break;
			case "6": request = highestOffer(); break;
			case "7": request = auctionEnds(); break;
			
			case "h": 
			case "help": showInfo(); break;
			case "x": connected = false; break;
			default:  wrongInput(); break;
			}
			
			if(!request.isEmpty()){
				writer.println(request);
				writer.flush();
			}
		}
	}

	/**
	 * Method for retrieving the auction info of one auction
	 * @return String with right command for server
	 */
	private String getAuctionInfo() {
		System.out.println("Auction ID:");
		return "getAuctionInfo " + sc.nextLine();
	}
	
	/**
	 * Method for retrieving all auction that contain the given keyword
	 * @return String with right command for server
	 */
	private String searchAuctions() {
		System.out.println("Keyword for search:");
		return "searchAuctions " + sc.nextLine();
	}
	
	/**
	 * Method for adding an auction to the server with the given specs
	 * @return String with right command for server
	 */
	private String addAuction() {
		String result = "addAuction ";
		//ask for item name
		System.out.println("Item name:");
		result += sc.nextLine() + "<>";
		//ask for short description
		System.out.println("Short item description:");
		result += sc.nextLine() + "<>";
		//ask for duration of auction
		System.out.println("Duration in minutes:");
		String input = sc.nextLine();
		//check if the given number is a number, if not ask again
		while(!isInteger(input)){
			System.out.println("Not a number, try again: ");
			input = sc.nextLine();
		}
		result += (Long.parseLong(input) * 60000L);
		//optional, ask for a startprice
		System.out.println("(Optional) Give a startprice");
		String startPrice = sc.nextLine();
		//if startprice is not empty, check if it's a number
		if(!startPrice.isEmpty()){
			while(!isInteger(startPrice)){
				System.out.println("Not a number, try again: ");
				startPrice = sc.nextLine();
			}
			result += "<>" + startPrice;
		}
		return result;
	}
	
	/**
	 * Method for doing an offer to the given auction
	 * @return String with right command for server
	 */
	private String doOffer() {
		String result = "doOffer ";
		//ask for auction id
		System.out.println("Auction ID:");
		result += sc.nextLine() + "<>";
		//ask for offer
		System.out.println("New offer:");
		String offer = sc.nextLine();
		//while offer is not a number, ask again
		while(!isInteger(offer)){
			System.out.println("Not a number, please try again");
			offer = sc.nextLine();
		}
		return result + offer;
	}
	
	/**
	 * Method for asking the highest offer of the given auction
	 * @return String with right command for server
	 */
	private String highestOffer() {
		//ask for auction id
		System.out.println("Auction ID:");
		return "highestOffer " + sc.nextLine();
	}
	
	/**
	 * Method for asking the remaining time of the given auction
	 * @return String with right command for server
	 */
	private String auctionEnds() {
		//ask for auction id
		System.out.println("Auction ID:");
		return "auctionEnds " + sc.nextLine();		
	}
	
	/**
	 * Method for displaying the sentence: Wrong input, please try again
	 */
	private void wrongInput() {
		System.out.println("Wrong input, please try again");
	}

	/**
	 * Checks if the given String is an Integer
	 * @param input
	 * @return true if input is Integer else false
	 */
	private static boolean isInteger(String input){
		try{
			Integer.parseInt(input);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}

	/**
	 * Converts a long number to a readable time left string
	 * @param date
	 * @return String with the readable time
	 */
	public static String longToReadableTimeLeft(long date){
		long dif = date - System.currentTimeMillis();
		if(dif > 0){
			int hours = (int) (dif / 360000);
			dif = dif % 360000;
			int min = (int) (dif / 60000);
			dif = dif % 60000;
			int sec = (int) (dif / 1000);
			String result = "";
			if(hours > 0)	{result += "Hours: " + hours;}
			if(min > 0)		{result += "\nMinutes: " + min;}
			if(sec > 0)		{result += "\nSeconds: " + sec;}
			return 	result;
		}
		return "Auction ended";
	}
	
	/**
	 * Shows all the commands that can be send to the server
	 */
	public void showInfo(){
		System.out.println(	"1: Get info of all auctions\n"
						  + "2: Get info of one auction\n"
						  + "3: Search auctions by keyword\n"
						  + "4: Add auction\n"
						  + "5: Do an offer\n"
						  + "6: Get highest bid of auction\n"
						  + "7: Get end date of auction");
	}
	

}
