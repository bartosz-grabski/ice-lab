package ice.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Bank.AccountPrx;
import Bank.AccountPrxHelper;
import Bank.BankManagerPrx;
import Bank.BankManagerPrxHelper;
import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank.IncorrectData;
import Bank.PersonalData;
import Bank.RequestRejected;
import Bank.accountType;
import Ice.StringHolder;

public class Client {
	
	public static final String SERVER_IP = "127.0.0.1";
	
	public static void main(String[] args) 
	{
		int status = 0;
		Ice.Communicator ic = null;

		try {

			ic = Ice.Util.initialize(args);
			
			Ice.ObjectPrx base = ic.propertyToProxy("SR.Proxy");
			BankManagerPrx bankManager = BankManagerPrxHelper.checkedCast(base);
			if (bankManager == null)
				throw new Error("Invalid proxy");

			String line = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			do
			{
				try
				{
					System.out.print("==> ");
					System.out.flush();
					line = in.readLine();
					if (line == null) {
						break;
					}
					if (line.equals("new")) {
						newAccount(in,ic,bankManager);
					}
                    else if (line.equals("info")) {
                    	getBalance(in, ic);
                    }
					else if (line.equals("transfer")) {
						transfer(in, ic);
					}
				}
				catch (java.io.IOException ex)
				{
					System.err.println(ex);
				}
			}
			while (!line.equals("x"));


		} catch (Ice.LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (ic != null) {
			try {
				ic.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}
	
	
	private static void getBalance(BufferedReader in, Ice.Communicator ic) throws IOException {
		
		System.out.println("Account #");
		String acc = in.readLine();
		Ice.ObjectPrx obj = ic.stringToProxy(acc+":ssl -h " + SERVER_IP + " -p 10001");
		AccountPrx account = AccountPrxHelper.checkedCast(obj);
		
		System.out.println(account.getBalance());
		
	}
	
	private static void transfer(BufferedReader in, Ice.Communicator ic) throws IncorrectAccountNumber, IncorrectAmount, IOException {
		
		System.out.println("From acc #");
		String from = in.readLine();
		System.out.println("To acc #");
		String to = in.readLine();
		System.out.println("Amount");
		int amount = Integer.parseInt(in.readLine());
		
		Ice.ObjectPrx obj = ic.stringToProxy(from+":ssl -h " + SERVER_IP + " -p 10001");
		AccountPrx account = AccountPrxHelper.checkedCast(obj);
		
		account.transfer(to, amount);
	}
	
	private static void newAccount(BufferedReader in, Ice.Communicator ic, BankManagerPrx bankManager) throws IOException, IncorrectData, RequestRejected {
		String firstName = in.readLine();
		String lastName = in.readLine();
		String NationalIDNumber = in.readLine();
		
		PersonalData data = new PersonalData(firstName, lastName, NationalIDNumber);
		
		accountType type = accountType.valueOf(in.readLine());
		
		StringHolder accountID = new StringHolder();
		
		bankManager.createAccount(data, type, accountID);
		
		System.out.println("Created account #: "+accountID.value);
	}
	

}
