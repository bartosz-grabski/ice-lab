
#ifndef SR_ICE
#define SR_ICE

#include <BuiltinSequences.ice>

//opcja kompilacji: -I .....slice\Ice CertSigner.ice

module SR
{

	exception DataTooLong {};
	exception IncorrectCSRFile {};
	exception IncorrectName {};
	
	interface CertSigner
	{
		Ice::ByteSeq signCSR(string name, string surname, Ice::ByteSeq csrFile) throws DataTooLong, IncorrectCSRFile;
	};

};

#endif
