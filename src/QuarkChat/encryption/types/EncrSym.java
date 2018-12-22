package QuarkChat.encryption.types;

import QuarkChat.encryption.AES;

/** 
 * Vom memora datele despre encryptia simetrica
 * -: AES = 0 
 * -: DES = 1
 * 
 * @author sandu_cristian
 */

public class EncrSym {
	public final static int numberEncr = 1; // how many modules of encryption we have implemented
	public final static String key[] = new String[numberEncr]; // memorize of the used password for encryption and decryption procedures
	public final static byte inUse[] = new byte[numberEncr]; // memorize of the available encryption modules
	
	public static int getID(String name) /** getID will return the ID of the encryption method **/
	{
		if(name.equals("AES"))
		{
			return 0; 
		}
		else
		{
			return -1;
		}
	}
	
	public final static boolean isEnable(String encryName)
	{
		return inUse[getID(encryName)]==1? true:false;
	}
	
	/** To know which of the encryption types are enable **/
	public final static byte[] whatEnable() {
		return inUse;
	}
	
	public static void enable(String encryName, String encryPassword) // active a module of encryption
	{
		int ID = getID(encryName);
		
		key[ID] = encryPassword;
		inUse[ID] = 1; // in use
	}
	
	public static void disable(String encryName) // disable a module of encryption
	{
		int ID = getID(encryName);
		
		key[ID] = null;
		inUse[ID] = 0; // in not in use anymore
	}
	
	public static byte[] encrypt(byte[] ArrayToEncrypt) {
		byte data[] = ArrayToEncrypt; // if something happens
		
		if(inUse[0] == 1) { // doar una singura pentru moment
			data = AES.encrypt(ArrayToEncrypt, key[0]);
		}
		
		return data;
	}
	
	public static byte[] decrypt(byte[] ArrayToDencrypt, byte[] encryptionUsage, int messageSize) {
		// right padded data
		byte padded[] = new byte[messageSize];
		
		System.arraycopy(ArrayToDencrypt, 0, padded, 0, messageSize);
		
		byte data[] = padded;
		
		if(encryptionUsage[0] == 1) { // doar una singura pentru moment
			if(inUse[0] == 0) {
				return null;
			}
			else {
				data = AES.decrypt(padded, key[0]);
			}
		}
	
		return data;
	}
	
	
}
