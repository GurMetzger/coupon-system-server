package app.core.login;

public class ClientDetails {

	// The Details sent to the Front-End after a successful login/register
	
	// Attributes
	private int id;
	private String name;
	private String email;
	private ClientType clientType;
	private String token;
	
	
	// CTOR
	private ClientDetails(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.email = builder.email;
		this.clientType = builder.clientType;
		this.token = builder.token;
	}


	// Getters	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public ClientType getClientType() {
		return clientType;
	}

	public String getToken() {
		return token;
	}
	
	
	// Builder
	public static class Builder {
		
		// Attributes
		private int id;
		private String name;
		private String email;
		private ClientType clientType;
		private String token;
		
		
		// CTOR
		public Builder() {
			super();
		}
		

		// Setters
		public Builder setId(int id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setClientType(ClientType clientType) {
			this.clientType = clientType;
			return this;
		}

		public Builder setToken(String token) {
			this.token = token;
			return this;
		}
		
		public ClientDetails build() {
			return new ClientDetails(this);
		}
		
	}
	
}
