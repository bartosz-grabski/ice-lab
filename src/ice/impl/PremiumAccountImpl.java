package ice.impl;

import java.util.logging.Logger;

import ice.persistance.AccountService;
import ice.persistance.model.Account;
import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank.IncorrectData;
import Bank.currency;
import FinancialNews.Currency;
import Ice.Current;
import Ice.FloatHolder;
import Ice.IntHolder;

public class PremiumAccountImpl extends Bank._PremiumAccountDisp {

	private static final Logger logger = Logger.getLogger(PremiumAccountImpl.class.getName());
	
	private AccountService service;

	public PremiumAccountImpl() {
		this.service = AccountService.getService();
	}

	@Override
	public void calculateLoan(int amount, currency curr, int period,
			IntHolder totalCost, FloatHolder interestRate, Current __current)
			throws IncorrectData {
		
		NewsReceiver receiver = NewsReceiver.getReceiver();
		
		interestRate.value = receiver.getInterestRate(switchCurrency(curr));
		totalCost.value = (int) (interestRate.value * amount);

	}

	@Override
	public int getBalance(Current __current) {
		logger.info("Retrieving balance for "+__current.id.name);
		return service.getAccountByNumber(__current.id.name).getBalance();
	}

	@Override
	public String getAccountNumber(Current __current) {
		return service.getAccountByNumber(__current.id.name).getAccountNumber();
	}

	@Override
	public void transfer(String accountNumber, int amount, Current __current)
			throws IncorrectAccountNumber, IncorrectAmount {
		
		Account from = service.getAccountByNumber(__current.id.name);
		Account to = service.getAccountByNumber(accountNumber);
		if (to == null) throw new IncorrectAccountNumber();
		if (from.getBalance() < amount ) throw new IncorrectAccountNumber();

		service.transferMoney(from, to, amount);
	}

	private Currency switchCurrency(currency c1) {

		switch (c1) {
		case CHF:
			return Currency.CHF;
		case USD:
			return Currency.USD;
		case EUR:
			return Currency.EUR;
		default:
			return Currency.PLN;
		}
	}

}
