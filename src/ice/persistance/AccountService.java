package ice.persistance;

import ice.persistance.model.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountService {
	
	
	private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:accounts.db";
	
    private Connection conn;
    private Statement stat;
    private static AccountService service = new AccountService();
    
	private AccountService() {
		try {
			conn = DriverManager.getConnection(DB_URL);
			stat = conn.createStatement();
			conn.setAutoCommit(false);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		createTables();
	}
	
	private void createTables() {
		try {
			String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS Accounts (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName varchar(255), lastName varchar(255), nationalIDNumber varchar(255), balance int, accountNumber varchar(255))";
			stat.execute(CREATE_ACCOUNT_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static AccountService getService() {
		return service;
	}
	
	public Account getAccountByNumber(String accountNumber) {
		try {
			Account account = null;
			ResultSet result = stat.executeQuery("SELECT * FROM Accounts WHERE accountNumber='"+accountNumber+"'");
			while (result.next()) {
				account = new Account();
				account.setBalance(result.getInt("balance"));
				account.setFirstName(result.getString("firstName"));
				account.setLastName(result.getString("lastName"));
				account.setAccountNumber(result.getString("accountNumber"));
				account.setNationalIDNumber(result.getString("nationalIDNumber"));
			}
			return account;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addAccount(Account acc) {
		try {
			
			PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO Accounts VALUES (NULL,?,?,?,?,?)");
			prepStmt.setString(1, acc.getFirstName());
			prepStmt.setString(2, acc.getLastName());
			prepStmt.setString(3, acc.getNationalIDNumber());
			prepStmt.setInt(4, acc.getBalance());
			prepStmt.setString(5, acc.getAccountNumber());
			prepStmt.execute();
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void transferMoney(Account from, Account to, int balance) {
		
		try {
			from.setBalance(from.getBalance() - balance);
			to.setBalance(to.getBalance() + balance);
			saveBalance(from);
			saveBalance(to);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception e1) {
				//
			}
		}
		
		
	}
	
	public void saveBalance(Account acc) {
		try {
			String UPDATE_ACCOUNT_SQL = "UPDATE Accounts SET balance="+acc.getBalance()+" WHERE accountNumber='"+acc.getAccountNumber()+"'";
			stat.execute(UPDATE_ACCOUNT_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeAccount(Account acc) {
		
		try {
			String DELETE_ACCOUNT_SQL = "DELETE FROM Accounts WHERE accountId='"+acc.getAccountNumber()+"'";
			stat.execute(DELETE_ACCOUNT_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void removeAccountById(String accountID) {
		
		try {
			String DELETE_ACCOUNT_SQL = "DELETE FROM Accounts WHERE accountId='"+accountID+"'";
			stat.execute(DELETE_ACCOUNT_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
