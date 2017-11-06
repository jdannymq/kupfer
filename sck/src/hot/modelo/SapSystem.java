package modelo;

public class SapSystem implements java.lang.Cloneable {
	private final String name;
	private final String host;
	private final String client;
	private final String systemNumber; 
	private final String user;
	private final String password;
	private final String language ="es"; 
	
	
	
	public SapSystem(String name, String host, String client
			 , String systemNumber, String user, String password) {
				this.name = name;
				this.client = client;
				this.user = user;
				this.password = password;
				this.host = host;
				this.systemNumber = systemNumber;
	}
	

	public String getName() {
		return name;
	}

	public String getClient() {
		return client;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getLanguage() {
		return language;
	}

	public String getHost() {
		return host;
	}

	public String getSystemNumber() {
		return systemNumber;
	}

	@Override
	public String toString() {
		return "Client " + client + " User " + user + " PW " + password
				+ " Language " + language + " Host " + host + " SysID "
				+ systemNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((systemNumber == null) ? 0 : systemNumber.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SapSystem other = (SapSystem) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (systemNumber == null) {
			if (other.systemNumber != null)
				return false;
		} else if (!systemNumber.equals(other.systemNumber))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

}