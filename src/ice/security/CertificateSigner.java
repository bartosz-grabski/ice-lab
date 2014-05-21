package ice.security;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import SR.CertSignerPrx;
import SR.CertSignerPrxHelper;

public class CertificateSigner {
	
	
	private static final String FIRSTNAME = "Bartosz";
	private static final String LASTNAME = "Grabski";
	private static final String CSR_FILE = "cs.csr";
	private static final String CRT_FILE = "cs.crt";
	
	public static String readFile(String filename) {
		   String content = null;
		   File file = new File(filename); 
		   try {
		       FileReader reader = new FileReader(file);
		       char[] chars = new char[(int) file.length()];
		       reader.read(chars);
		       content = new String(chars);
		       reader.close();
		   } catch (IOException e) {
		       e.printStackTrace();
		   }
		   return content;
	}
	
	
	public static void writeFile(String content, String filename) {
		
		try {
			
			FileWriter writer = new FileWriter(filename);
			writer.write(content);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String args[]) {
		
		int status = 0;
		Ice.Communicator ic = null;

		try {

			ic = Ice.Util.initialize(args);

			Ice.ObjectPrx cert = ic.propertyToProxy("SR.Cert");
			
			if (cert == null) {
				throw new Exception("Object not found!");
			}
			
			CertSignerPrx proxy = CertSignerPrxHelper.checkedCast(cert);
			
			String content = readFile(CSR_FILE);
			System.out.println(content);
			byte[] certificate = proxy.signCSR(FIRSTNAME, LASTNAME, content.getBytes());
			
			System.out.println(new String(certificate));
			writeFile(new String(certificate), CRT_FILE);


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

}
