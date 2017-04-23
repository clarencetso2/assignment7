package assignment7;

public class User {
	
	private String userName=null;
	private String password=null;
	private int userId;
	public User(String username,String password,int userId){
		this.userName=username;
		this.password=password;
		this.userId=userId;
	}
	public String getUsername(){
		return userName;
	}
	public int getUserId(){
		return userId;
		
	}
	public String getPassword(){
		return password;
	}

	
	

}
