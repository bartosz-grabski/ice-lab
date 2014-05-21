package ice.impl;

import ice.persistance.AccountService;
import ice.persistance.model.Account;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Logger;

import Bank.IncorrectData;
import Bank.NoSuchAccount;
import Bank.PersonalData;
import Bank.RequestRejected;
import Bank.accountType;
import Ice.Current;
import Ice.ObjectAdapter;
import Ice.StringHolder;


public class BankManager extends Bank._BankManagerDisp {
	
	private static final Logger logger = Logger.getLogger(BankManager.class.getName());
	private static final int ACCOUNT_NR_LENGTH = 26;
	private static final int DEFAULT_BALANCE = 10000;
	
	private ObjectAdapter adapter;
	private Ice.Communicator ic;
	
	
	public BankManager(ObjectAdapter adapter, Ice.Communicator ic) {
		this.adapter = adapter;
		this.ic = ic;
	}

	@Override
	public void createAccount(PersonalData data, accountType type,
			StringHolder accountID, Current __current) throws IncorrectData,
			RequestRejected {
		
		logger.info("Create account request for "+data.firstName + " " + data.lastName);

		String accountNumber = getUniqueAccountNumber();
		Account newAccount = new Account();
		newAccount.setAccountNumber(accountNumber);
		newAccount.setBalance(DEFAULT_BALANCE);
		newAccount.setFirstName(data.firstName);
		newAccount.setLastName(data.lastName);
		newAccount.setNationalIDNumber(data.NationalIDNumber);
		
		AccountService.getService().addAccount(newAccount);
		
		if(type == accountType.PREMIUM) {
			adapter.add(new PremiumAccountImpl(), ic.stringToIdentity(accountNumber));
		}
		
		accountID.value = accountNumber;
	}

	@Override
	public void removeAccount(String accountID, Current __current)
			throws IncorrectData, NoSuchAccount {
		
		String currentAccount = __current.id.name;
		AccountService.getService().removeAccountById(currentAccount);
		
		
		
	}
	
	private String getUniqueAccountNumber() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130,random).toString(ACCOUNT_NR_LENGTH);
	}

}
