package QuarkChat.encryption.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import QuarkChat.gui.ChatGUI;
import QuarkChat.messageformats.FileFormatR;

public class FileDecryptor extends Thread{
	// general informations
	public static ChatGUI gui = null;
	public static boolean forceStop = false; // to terminate the thread
	
	// usefull data
	private final int MAX_read = 304;
	
	// usefull informations
	private FileFormatR fisier_primit = null;
	
	public FileDecryptor(FileFormatR file) {
		super();
		this.fisier_primit = file;
	}
	
	private static int getMultiple16(int X) {
		if (X%16 == 0) {
			return X;
		}
		else {
			return ((int)X/16 + 1) * 16;
		}
	}
	
	@Override
	public void run() {
		File fisier = new File(this.fisier_primit.fileName);
		File fisierTemp = new File(this.fisier_primit.fileName + "_unecd");
		
		FileInputStream fin = null;
		FileOutputStream fout = null;
		
		try {
			fin = new FileInputStream(fisier);
			fout = new FileOutputStream(fisierTemp);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		byte[] buffer = new byte[320];
		byte[] bufferDe = null;
		
		byte[] encryptType = new byte[5];
		encryptType[0] = 1;
		
		int remaining;
		
		try {
			DECRYPTION_EXIT: while(fin.available() != 0 && FileDecryptor.forceStop == false) {
				remaining = fin.available();
				
				// as long as we can read
				if(remaining > MAX_read) {
					remaining = 304;
					fin.read(buffer, 0, remaining);
				}
				else {
					remaining = getMultiple16(remaining);
					fin.read(buffer, 0, remaining);
				}
				
				
				// decrypt
				bufferDe = EncrSym.decrypt(buffer, encryptType, remaining);
				
				// write to file
				if(bufferDe != null) {
					fout.write(bufferDe, 0, bufferDe.length);
				}
				else {
					fout.close();
					fin.close(); // close files
					
					break DECRYPTION_EXIT;
				}
			}
			
			if(FileDecryptor.forceStop == false) {
				if(bufferDe == null) {
					gui.write("[File Transfer] Your file could not been decrypted!", 2);
				}
				else {
					gui.write("[File Transfer] Your file has been decrypted!", 2);
				}
			}
			
			fout.close();
			fin.close(); // close files
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
