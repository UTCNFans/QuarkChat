package QuarkChat.messageformats;

import java.nio.ByteBuffer;

import QuarkChat.encryption.types.EncrSym;

public class MessageFormat implements Formats {
	/*
	 * MESSAGE FORMAT:
	 * MARKER ENCRYPTION (SIZE SIZE SIZE SIZE) MESSAGE
	 */
	
	// digerarea mesajului
	private String mesaj = null;
	private byte[] byte_mesaj = null;
	private byte[] messageSize = null;

	// temporary buffer for the message
	private byte[] buffer_message = null;

	public MessageFormat(String message) {
		this.mesaj = message;
		this.buffer_message = mesaj.getBytes();
	}
	
	@Override
	public byte[] getData() {
		this.digestInfo();
		
		return this.byte_mesaj;
	}

	@Override
	public void digestInfo() {
		final byte[] encryptionsMark = QuarkChat.encryption.types.EncrSym.whatEnable();
		
		// encrypt message adecvatly
		byte[] encryptedMessage = EncrSym.encrypt(this.buffer_message);
		this.messageSize = ByteBuffer.allocate(4).putInt(encryptedMessage.length).array();
		
		// allocate memory for the message
		this.byte_mesaj = new byte[1 + 4 + encryptedMessage.length + 
		                           encryptionsMark.length];
				
		// add markers
		this.byte_mesaj[0] = MARKS[0];
		
		
		// add the message the encryption types for each message
		System.arraycopy(encryptionsMark, 0, this.byte_mesaj, 1, 
				encryptionsMark.length);
		
		// add the size of the message
		System.arraycopy(messageSize, 0, this.byte_mesaj, encryptionsMark.length + 1, 4);
		
		// add the string to the byte array
		System.arraycopy(encryptedMessage, 0, this.byte_mesaj, 
				1 + 4 + encryptionsMark.length, encryptedMessage.length);
	}

	@Override
	public boolean isFinish() {
		if(this.byte_mesaj == null) {
			// nu au fost inca procesate datele
			return true;
		}
		return false;
	}
}
