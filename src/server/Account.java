package server;

public class Account {
	
	private String username, password;
	
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

}
