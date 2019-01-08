package QuarkChat.messageformats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import QuarkChat.errorhandle.LogFile;

public class FileFormatR implements Reassambler {
	// where to save the input file
	FileOutputStream fisier = null;
	
	// file data
	public String fileName = null;
	private File fileFile = null;
	
	// useful informations
	public byte[] secureCode = new byte[20];
	public byte isFinish = 0;
	
	private final byte[] encryptionsMark = QuarkChat.encryption.types.EncrSym.whatEnable();

	// encryption informations
	public boolean isEncrypted = false;
	private byte[] encryptionUsage = new byte[5];
	
	public FileFormatR(byte[] InputData) {		
		// get file name
		byte[] fileNameByte = new byte[20];
		System.arraycopy(InputData, 1 + 1, fileNameByte, 0, 20);
		this.fileName = new String(fileNameByte);
		this.fileName = this.fileName.substring(0, this.fileName.indexOf('\0'));
		
		this.fileName = getAvaible(this.fileName);
		this.fileFile = new File(fileName);
		
		// get secure code 
		System.arraycopy(InputData, 1 + 1 + 20, this.secureCode, 0, 20);
		
		// set encryption procedure
		this.isEncrypted = InputData[1 + 1 + 20 + 20] == 1? true:false;
		encryptionUsage[0] = 1; // mark the use of the encryption
	}
	
	private String getAvaible(String fileName) {
		if(new File(fileName).exists()) {
			return new String(fileName);
		}
		else {
			for(int j=1; true; j++) {
				fileName = fileName + String.format("_%04d", j);
				if(!new File(fileName).exists()) {
					return new String(fileName);
				}
				fileName = fileName.substring(0, fileName.length()-5);
			}
		}
	}
	
	public static byte[] getSecureCode(byte[] InputData) {
		// get file name
		byte[] secureCodes = new byte[20];
		System.arraycopy(InputData, 1 + 1 + 20, secureCodes, 0, 20);
		
		return secureCodes;
	}
	
	public static String getName(byte[] InputData) {
		// get file name
		byte[] fileNameByte = new byte[20];
		System.arraycopy(InputData, 1 + 1, fileNameByte, 0, 20);
		String fileName = new String(fileNameByte);
		fileName = fileName.substring(0, fileName.indexOf('\0'));
		
		return new String(fileName);
	}
	
	@Override
	public byte[] indigest(byte[] InputData) {
		// get file finish status
		this.isFinish = InputData[1];
		
		if(!this.fileFile.exists()) {
			try {
				this.fileFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			this.fisier = new FileOutputStream(this.fileFile, true);
		} catch (FileNotFoundException error) {
			LogFile.logger.log(Level.WARNING, "FileFormatR -> constructor", error);
		}
		
		// data size
		int startPos = 1 + 1 + 20 + 20 + encryptionsMark.length;
		int messageSize = InputData[startPos] << 24 | (InputData[startPos + 1] & 0xFF) << 16 | (InputData[startPos + 2] & 0xFF) << 8 | (InputData[startPos + 3] & 0xFF);
		
		// prepare input data to be written
		byte[] data = new byte[messageSize];
		System.arraycopy(InputData, startPos + 4, data, 0, messageSize);
				
		try {
			this.fisier.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.fisier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null; // nothing to return
	}
}
