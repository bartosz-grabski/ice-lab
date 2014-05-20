package ice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import FinancialNews.Currency;
import FinancialNews._NewsReceiverDisp;
import Ice.Current;

public class NewsReceiver extends _NewsReceiverDisp {
	
	private static final Logger logger = Logger.getLogger(NewsReceiver.class.getName());
	private static final NewsReceiver receiver = new NewsReceiver();
	
	private Map<Currency, HashMap<Currency, Float>> exchangeMap = new HashMap<Currency, HashMap<Currency, Float>>();
	private Map<Currency, Float> interestMap = new HashMap<Currency, Float>();
	
	private NewsReceiver() { }
	

	@Override
	public void exchangeRate(Currency curr1, Currency curr2, float rate,
			Current __current) {
			
			logger.info("Exchange rate " + curr1 + " " + curr2 + " " + rate);
			
			HashMap<Currency, Float> exMap = exchangeMap.get(curr1);
			if (exMap == null) {
				exMap = new HashMap<Currency, Float>();
				exMap.put(curr2, rate);
				exchangeMap.put(curr1, exMap);
				} else {
				exMap.put(curr2, rate);
			}
	}

	@Override
	public void interestRate(Currency curr, float rate, Current __current) {
		
			logger.info("Interest rate " + curr + " " + rate);
			interestMap.put(curr, rate);
		
		
	}
	
	public static NewsReceiver getReceiver() {
		return receiver;
	};
	
	public float getInterestRate(Currency curr1) {
		return interestMap.get(curr1);
	}
	
	public float getExchangeRate(Currency curr1, Currency curr2) {
		return exchangeMap.get(curr1).get(curr2);
	}

}
