package app.core.login;

public class Credentials {
	
	// Credentials the Front-End user sends to the Back-end in order to login
	
	// Attributes
	public String email;
	public String password;
	public ClientType clientType;
	
	
	// CTOR
	public Credentials(String email, String password, ClientType clientType) {
		super();
		this.email = email;
		this.password = password;
		this.clientType = clientType;
	}


	// Overriden Method
	@Override
	public String toString() {
		return "Credentials [email=" + email + ", password=" + password + ", clientType=" + clientType + "]";
	}
	
}
