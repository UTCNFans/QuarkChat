package QuarkChat.encryption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import javax.crypto.*; /** for crypto functions **/
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import QuarkChat.errorhandle.LogFile;

/** Need password for operation of decryption -> Symmetric algorithm **/

public final class AES {
	private static boolean ErrorProcess = false;
	private static boolean WrongKey = false;
	
	public static boolean isError()
	{
		return ErrorProcess;
	}
	
	public static boolean isWrong()
	{
		return WrongKey;
	}
	
	
	public static byte[] encrypt(byte[] DataInputText, String Password)
	{
	    byte[] iv = {93, 125, -77, 12, 81, -5, -128, 127, 0, 74, 2, 74, 96, 120, -56, 86 };
	    IvParameterSpec ivspec = new IvParameterSpec(iv);
		Cipher cipher;
	    SecretKeySpec SecretKey = new SecretKeySpec(SHA_1.fhash(Password).substring(0, 16).getBytes(), "AES");
	    
	    try {
	    	cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");  /** AES 128 **/
			cipher.init(Cipher.ENCRYPT_MODE, SecretKey, ivspec);

		} catch (NoSuchAlgorithmException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			return null;
		} catch (NoSuchPaddingException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			return null;
		} catch(InvalidKeyException error_msg){
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			return null;
	    } catch (InvalidAlgorithmParameterException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
	    	return null;
		}
		    
	    try {
			return cipher.doFinal(DataInputText);
		} catch (IllegalBlockSizeException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
	    	ErrorProcess = true;
			return null;
		} catch (BadPaddingException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
	    	ErrorProcess = true;
			return null;
		}
	}
	
	public static byte[] decrypt(byte[] DataInputText, String Password)
	{
	    byte[] iv = {93, 125, -77, 12, 81, -5, -128, 127, 0, 74, 2, 74, 96, 120, -56, 86 };
	    IvParameterSpec ivspec = new IvParameterSpec(iv);
	    
		Cipher cipher;
	    SecretKeySpec SecretKey = new SecretKeySpec(SHA_1.fhash(Password).substring(0, 16).getBytes(), "AES");
	    
	    try {
	    	cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");  /** AES 128 **/
			cipher.init(Cipher.DECRYPT_MODE, SecretKey, ivspec);
		} catch (NoSuchAlgorithmException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
	    	ErrorProcess = true;
			return null;
		} catch (NoSuchPaddingException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
	    	ErrorProcess = true;
			return null;
		}
	    catch(InvalidKeyException error_msg){
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			return null;
	    } catch (InvalidAlgorithmParameterException error_msg) {
	    	ErrorProcess = true;
			return null;
		}
		    
	    try {
			return cipher.doFinal(DataInputText);
		} catch (IllegalBlockSizeException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			WrongKey = true;
			return null;
		} catch (BadPaddingException error_msg) {
	    	LogFile.logger.log(Level.WARNING, "Error at chatproject.encryption.AES", error_msg);
			ErrorProcess = true;
			WrongKey = true;
			return null;
		}
	}
}
