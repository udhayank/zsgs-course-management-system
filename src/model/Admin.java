package model;

public class Admin {

	final String name = "admin";
	final String id = "admin";
	private String password = "password";
	
	public String getPassword() {
		return password;
	}
	public boolean setPassword(String oldPassword, String newPassword) {
		if (this.password == oldPassword) {
			this.password = newPassword;
			return true;
		}
		return false;
	}
	
		
}
