package ice.server;

import ice.impl.BankManager;
import ice.impl.NewsReceiver;
import ice.impl.SilverEvictor;
import ice.security.CertificateVerifier;
import FinancialNews.NewsReceiverPrx;
import FinancialNews.NewsReceiverPrxHelper;
import FinancialNews.NewsServerPrx;
import FinancialNews.NewsServerPrxHelper;
import Ice.ObjectAdapter;

public class Server {
	
	public static final String MANAGER_ID = "manager";
	public static final String OBJECT_ADAPTER_ID = "objectAdapter";
	public static final int EVICTOR_SIZE = 2;
	private static final String EMPTY = "";
	
	public void start(String[] args) {
		
		int status = 0;
		Ice.Communicator ic = null;
		
		try {
			
			ic = Ice.Util.initialize(args);
			Ice.ObjectAdapter adapter = ic.createObjectAdapter(OBJECT_ADAPTER_ID);
			Ice.ObjectPrx newsServerPrx = ic.propertyToProxy("SR.News");
			Ice.PluginManager pluginMgr = ic.getPluginManager();
			Ice.Plugin plugin = pluginMgr.getPlugin("IceSSL");
			IceSSL.Plugin sslPlugin = (IceSSL.Plugin)plugin;
			sslPlugin.setCertificateVerifier(new CertificateVerifier());
			
			NewsServerPrx newsServer = NewsServerPrxHelper.checkedCast(newsServerPrx);
			new Receiver(ic, newsServer).start();
			
			this.configureBankManager(adapter,ic);
			this.configureSilverAccountEvictor(adapter);
			
            adapter.activate();

            System.out.println("Entering event processing loop...");

            ic.waitForShutdown();
			
		} catch (Exception e) {
			System.err.println(e);
			status = 1;
		}
		
		if (ic != null)
        {
            try
            {
                ic.destroy();
            }
            catch (Exception e)
            {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);
		
	}
	
	
	private void configureBankManager(ObjectAdapter adapter, Ice.Communicator ic) {
		Ice.Object bankManager = new BankManager(adapter,ic);
		adapter.add(bankManager, ic.stringToIdentity(MANAGER_ID));
	}
	
	private void configureSilverAccountEvictor(ObjectAdapter adapter) {
		Ice.ServantLocator evictor = new SilverEvictor(EVICTOR_SIZE);
		adapter.addServantLocator(evictor, EMPTY);
	}
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start(args);
	}

}

class Receiver extends Thread {

	private static final String RECEIVER_ADAPTER_ID = "ReceiverAdapter";
	private static final String RECEIVER_ID = "NewsReceiver";
	
	Ice.Communicator ic = null;
	NewsServerPrx newsServer;
	
	public Receiver(Ice.Communicator ic, NewsServerPrx newsServer) {
	
		this.ic = ic;
		this.newsServer = newsServer;
		
	}
	
	@Override
	public void run() {
		
		Ice.ObjectAdapter receiverAdapter = ic.createObjectAdapter("");
		NewsReceiver receiver = NewsReceiver.getReceiver();
		
		NewsReceiverPrx receiverProxy = NewsReceiverPrxHelper.uncheckedCast(receiverAdapter.add(receiver, ic.stringToIdentity(RECEIVER_ID)));
		
		
	}
	

}
