package model;

import java.net.Socket;

/**
 * Account
 * Used to log in on the system
 */
public class Account {
	private String username, password;
	private Socket socket;
	
	/**
	 * Constructor to create an account
	 * @param username	Username of the account you want to create
	 * @param password	Password of the account you want to create
	 */
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Checks if the given username and password match the account his username and password
	 * @param username	Username you want to check
	 * @param password	Password you want to check
	 * @return	True if account has the same username and password as the arguments. Else false
	 */
	public boolean isThisAccount(String username, String password){
		return this.username.equals(username) && this.password.equals(password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setSocket(Socket threadSocket) {
		this.socket = threadSocket;
	}
	
	public Socket getSocket() {
		return socket;
	}

}
