package ice.persistance.model;

public class Account {
	
	private int id;
	private String firstName;
	private String lastName;
	private String accountNumber;
	private String NationalIDNumber;
	private int balance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getNationalIDNumber() {
		return NationalIDNumber;
	}
	public void setNationalIDNumber(String nationalIDNumber) {
		NationalIDNumber = nationalIDNumber;
	}
	

}
