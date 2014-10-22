package server;

import java.net.Socket;

public class Account {
	
	private String username, password;
	private Socket socket;
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean isThisAccount(String username, String password){
		return this.username.equals(username) && this.password.equals(password);
	}
	
	public String getUsername() {
		return username;
	}

	public void setSocket(Socket threadSocket) {
		this.socket = threadSocket;
	}
	
	public Socket getSocket() {
		return socket;
	}

}
