import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BankDAO {

	private final static String DBURL = "jdbc:mysql://127.0.0.1:3306/bank?autoReconnect=true&useSSL=false";
	private final static String DBUSER = "root";
	private final static String DBPASS = "admin";
	private final static String DBDRIVER = "com.mysql.jdbc.Driver";

	private Connection connection;
	private Statement statement;
	private String query;
	private ResultSet result;

	public boolean checkCredentials(String user, String password) {

		boolean exists = false;
		query = String.format("select * from users where user = '%s' and password = '%s'", user, password);

		try {
			Class.forName(DBDRIVER);
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			if (result.next()) {
				exists = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	public boolean checkUser(String user) {

		boolean exists = true;
		query = String.format("select * from users where user = '%s'", user);

		try {
			Class.forName(DBDRIVER);
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			if (!result.next()) {
				exists = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	public boolean registerUser(String user, String password) {

		boolean success = false;
		query = String.format("insert into users (user, password, balance) values ('%s', '%s', 0)", user, password);

		try {
			Class.forName(DBDRIVER);
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			statement.executeUpdate(query);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean deposit(String user, double sum) {

		String finalBalance = String.valueOf(this.checkBalance(user) + sum);
		boolean success = false;
		query = String.format("update users set balance = %s where user = '%s'", finalBalance, user);

		try {
			Class.forName(DBDRIVER);
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			statement.executeUpdate(query);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public boolean withdraw(String user, double sum) {

		double finalBalance = this.checkBalance(user) - sum;
		boolean success = false;

		if (finalBalance > 0) {
			query = String.format("update users set balance = %s where user = '%s'", String.valueOf(finalBalance),
					user);

			try {
				Class.forName(DBDRIVER);
				connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
				statement = connection.createStatement();
				statement.executeUpdate(query);
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public double checkBalance(String user) {

		double balance = -1.0;
		query = String.format("select balance from users where user = '%s'", user);

		try {
			Class.forName(DBDRIVER);
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			result = statement.executeQuery(query);

			if (result.next()) {
				balance = result.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

}
