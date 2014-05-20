package ice.impl;

import ice.persistance.AccountService;
import ice.persistance.model.Account;
import ice.security.CertificateAccountVerifier;
import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Ice.Current;


public class SilverAccountImpl extends Bank._AccountDisp {
	
	
	private AccountService service;
	
	public SilverAccountImpl() {
		service = AccountService.getService();
	}

	@Override
	public int getBalance(Current __current) {
		
		Account acc = service.getAccountByNumber(__current.id.name);
		
		if (acc == null || !CertificateAccountVerifier.verify(__current, acc)) {
			return Integer.MIN_VALUE;
		}
		
		return acc.getBalance();
	}

	@Override
	public String getAccountNumber(Current __current) {
		Account acc = service.getAccountByNumber(__current.id.name);
		
		if (acc == null || !CertificateAccountVerifier.verify(__current, acc)) {
			return "";
		}
		
		
		return acc.getAccountNumber();
	}

	@Override
	public void transfer(String accountNumber, int amount, Current __current)
			throws IncorrectAccountNumber, IncorrectAmount {
		
		Account accountFrom = service.getAccountByNumber(__current.id.name);
		Account accountTo = service.getAccountByNumber(accountNumber);
		
		
		if (accountFrom == null || accountTo == null || !CertificateAccountVerifier.verify(__current, accountFrom)) {
			//TODO some exception should be thrown
		}
		
		if (accountFrom == null) throw new IncorrectAccountNumber();
		if (accountFrom.getBalance() < amount ) throw new IncorrectAmount();
		
		service.transferMoney(accountFrom, accountTo, amount);
		
	}

}
