package ice.security;

import java.util.logging.Logger;

import ice.persistance.model.Account;
import Ice.Current;

public class CertificateAccountVerifier {

	private static final Logger logger = Logger.getLogger(CertificateAccountVerifier.class.getName());

	public static boolean verify(Current curr, Account acc) {

		logger.info("veryfiying");
		Ice.ConnectionInfo info = curr.con.getInfo();
		IceSSL.NativeConnectionInfo sslinfo = (IceSSL.NativeConnectionInfo) info;
		if (!sslinfo.nativeCerts[0].toString().contains(
				"CN=" + acc.getFirstName() + " " + acc.getLastName())) {
			return false;
		}

		return true;
	}

}
