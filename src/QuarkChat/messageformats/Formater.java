package QuarkChat.messageformats;

import java.io.IOException;

public interface Formater {
	public final static byte[] MARKS = {
			0x34, // mark for MESSAGE FORMAT
			0x35 // mark for FILE FORMAT
	};
	
	/** Get the data necessary **/
	public abstract byte[] getData();
	
	/** This will transform the message into an encrypted or non-encrypted one 
	 * @throws IOException **/
	public abstract void digestInfo() throws IOException;
	
	/** If the message read / file read has come to an end **/
	public abstract boolean isFinish();
}
