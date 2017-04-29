package assignment7;

public class User {
	
	private String userName = null;
	private String password = null;
	private int userId;
	
	/**
	 * Constructor
	 * @param username
	 * @param password
	 * @param userId
	 */
	public User(String username,String password,int userId){
		this.userName=username;
		this.password=password;
		this.userId=userId;
	}
	
	/**
	 * 
	 * @return The username of the user
	 */
	public String getUsername(){
		return userName;
	}
	
	/**
	 * 
	 * @return The user ID of the user
	 */
	public int getUserId(){
		return userId;
	}
	
	/**
	 * 
	 * @return The password of the user
	 */
	public String getPassword(){
		return password;
	}
}