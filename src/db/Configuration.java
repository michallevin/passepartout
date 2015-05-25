package db;

public class Configuration {
	private static final String type = "remote";
	
	public String hostName;
	public String port;
	public String dbName;
	public String protocol = "jdbc:mysql";
	private String userName;
	private String password;
	
	public static Configuration get() {
		Configuration config = new Configuration();

		if (type.equals("remote")) {
			config.hostName = "127.0.0.1";
			config.port = "3305";
			config.dbName = "DbMysql08";
			config.setUserName("DbMysql08");
			config.setPassword("DbMysql08");
		}
		else if (type.equals("local")) {
			config.hostName = "127.0.0.1";
			config.port = "3306";
			config.dbName = "DbMysql08";
			config.setUserName("DbMysql08");
			config.setPassword("DbMysql08");
		}
		
		return config;
	}
	
	public String getConnectionURL(){
		return String.format("%s://%s:%s/%s", protocol, hostName, port, dbName);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
