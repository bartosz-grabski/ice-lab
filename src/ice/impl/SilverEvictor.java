package ice.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;

public class SilverEvictor implements ServantLocator {

	private static final Logger logger = Logger.getLogger(SilverEvictor.class.getName());
	
	private int size;
	private LinkedHashMap<String,SilverAccountImpl> servants;
	
	public SilverEvictor(int size) {
		
		logger.info("Evictor created with size "+size);
		this.size = size;
		this.servants = new LinkedHashMap<>(size);
		
	}
	
	@Override
	public synchronized Object locate(Current curr, LocalObjectHolder cookie)
			throws UserException {
		
		logger.info("Evictor is locating object "+curr.id.name);
		
		if (servants.containsKey(curr.id.name)) {
			logger.info("Evictor has located object "+curr.id.name+" in cache");
			return servants.get(curr.id.name);
		}
		
		if (servants.size() == size) {
			logger.info("Evictor is replacing servant in cache for "+ curr.id.name);
			for (String id : servants.keySet()) {
				servants.remove(id);
				break;
			}
			SilverAccountImpl impl = new SilverAccountImpl();
			servants.put(curr.id.name, impl);
			return impl;
		} else if (servants.size() < size){
			logger.info("Evictor is putting new servant, cache is not filled yet");
			SilverAccountImpl impl = new SilverAccountImpl();
			servants.put(curr.id.name, impl);
			return impl;
		}
		
		return null;
		
	}

	@Override
	public void finished(Current curr, Object servant, java.lang.Object cookie)
			throws UserException {
		logger.info("Evictor finished");
		
	}

	@Override
	public void deactivate(String category) {
		logger.info("Evictor deactivate");
		
	}

}
