package QuarkChat.messageformats;

import QuarkChat.encryption.types.EncrSym;

public class MessageFormatR implements Reassamble {
	// original text before digestion
	private byte[] message_byte = new byte[2048];
	private byte[] encryptionUsage = new byte[5];
	private int messageSize = 0;
	
	public MessageFormatR(byte[] InputData) {
		final byte[] encryptionsMark = QuarkChat.encryption.types.EncrSym.whatEnable();
		
		// message size
		int startPos = encryptionsMark.length + 1;
		this.messageSize = InputData[startPos] << 24 | (InputData[startPos + 1] & 0xFF) << 16 | (InputData[startPos + 2] & 0xFF) << 8 | (InputData[startPos + 3] & 0xFF);
		
		// encryption usage
		System.arraycopy(InputData, 1, encryptionUsage, 0, 1);
		
		// start decomposing it
		System.arraycopy(InputData, 1 + 4 + encryptionsMark.length, 
				this.message_byte, 0, this.messageSize);
	}
	
	@Override
	public byte[] indigest(byte[] InputData) {
		return EncrSym.decrypt(this.message_byte, this.encryptionUsage, this.messageSize);
	}

}
