package ice.security;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import IceSSL.NativeConnectionInfo;

public class CertificateVerifier implements IceSSL.CertificateVerifier {

	@Override
	public boolean verify(NativeConnectionInfo info) {
		
		/*if (info.nativeCerts[0] != null)
        {
            X509Certificate cert = (X509Certificate) info.nativeCerts[0];
         
            	try {
					cert.checkValidity();
				} catch (CertificateExpiredException e) {
					e.printStackTrace();
					return false;
				} catch (CertificateNotYetValidException e) {
					e.printStackTrace();
					return false;
				}
            	X500Principal p = cert.getIssuerX500Principal();
            	if (!p.getName().contains("CN=Laboratorium Systemów Rozproszonych 2014")) return false;
            	
            	return true;
        }
        return true;*/
		return true;
		
	}

}
