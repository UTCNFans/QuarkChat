package QuarkChat.messageformats;

public interface Reassamble {
	// marcaje pentru tipurile de informatii care pot sa fie primite
	public final static byte[] MARKS = {
			0x34, // mark for MESSAGE FORMAT
			0x35 // mark for FILE FORMAT
	};
	
	public abstract byte[] indigest();
}
