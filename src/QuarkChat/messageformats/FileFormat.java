package QuarkChat.messageformats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import QuarkChat.encryption.SHA_1;
import QuarkChat.errorhandle.LogFile;

public class FileFormat implements Formats {
	/*
	 * FILE FORMAT
	 * 
	 * MARK IS_FINISH FILE_NAME (x20) CRYPTOGRAPHIC_SECURE(x20) ENCRYPTION FILE_SIZE (x4) FILE_CONTENT(512)
	 *
	 * MARK = if is a file or a message
	 * IS_FINISH = if file has been succesfuly downloaded
	 * FILE_NAME = name of the file (0 for no, 1 for yes)
	 * CRYPTOGRAPHIC_SECURE = to determinate that it is the same file from the beginning
	 * FILE_CONTENT = content of the file
	 * 
	 **/
	
	// configuration of the file
	static final int MAX_fileread = 300; // 1 KB maximum file size buffer
	public FileInputStream file = null;
	private File fisier = null;
	
	// for informations send packs
	private byte[] dataPack = null;
	private byte[] fileName = new byte[20];
	private byte[] cryptoSecure = new byte[20];
	
	// encryption marks
	private final byte[] encryptionsMark = QuarkChat.encryption.types.EncrSym.whatEnable();
	
	
	// is finish
	private byte isFinish = 0;
	
	
	public FileFormat(String file_path) throws FileNotFoundException {
		this.fisier = new File(file_path);
		
		this.file = new FileInputStream(fisier);
		this.dataPack = new byte[1029]; 
		
		// copiem numele fisierului
		// daca numele fisierului este mai mare de 20 caractere
		if(fisier.getName().length() >= 20) {
			System.arraycopy(fisier.getName().substring(0, 20).getBytes(), 0, fileName, 0, 20);
		}
		else {
			System.arraycopy(fisier.getName().getBytes(), 0, fileName, 0, fisier.getName().getBytes().length);
		}
	
		// current time
		Date current_time = Calendar.getInstance().getTime();
		String Scurrent_time = new String(new SimpleDateFormat("HH:mm:ss").format(current_time));
		
		// Secure CODE
		String secure_code = SHA_1.fhash(fisier.getName() + Scurrent_time).substring(0, 20);
		
		// copiem secure code
		System.arraycopy(secure_code.getBytes(), 0, cryptoSecure, 0, 20);
	}

	@Override
	public byte[] getData() {
		try {
			this.digestInfo();
			return this.dataPack;
		} catch (IOException error) {
			LogFile.logger.log(Level.WARNING, "IOException -> File format", error);
			System.err.println("[Error] Something went wrong when reading from file! Check logs");
		}
		return null;
	}

	// O ZI PERFECTA! 
	// FRUMOASA SI ETERNA ROMANIA!
	// BEAU CU BAIETII MEI
	
	@Override
	public void digestInfo() throws IOException {	
		// allocate memory for dataPack
		this.dataPack = new byte[encryptionsMark.length + 1 + 1 + 20 + 20 + 4 + FileFormat.MAX_fileread];
				
		// add markers
		this.dataPack[0] = MARKS[1];

		// add the file name
		System.arraycopy(this.fileName, 0, this.dataPack, 1 + 1, 
				20);
		
		// add secure code
		System.arraycopy(this.cryptoSecure, 0, this.dataPack,  1 + 1 + 20, 20);
		
		// add the message the encryption types for each message
		System.arraycopy(encryptionsMark, 0, this.dataPack, 1 + 1 + 20 + 20, 
				encryptionsMark.length);
		
		// add the file content to the byte array
		int fileRemainingSize = this.file.available()>FileFormat.MAX_fileread?FileFormat.MAX_fileread:this.file.available();
		byte[] tempData = ByteBuffer.allocate(4).putInt(fileRemainingSize).array();
		
		
		// copy size
		System.arraycopy(tempData, 0, this.dataPack, 1 + 1 + 20 + 20 + encryptionsMark.length, 4);

		this.file.read(this.dataPack, 1 + 1 + 20 + 20 + encryptionsMark.length + 4, fileRemainingSize);
		if(this.file.available() == 0) {
			// nothing left to be transfered
			isFinish = 1;
			
		}
		// add is_Finish
		this.dataPack[1] = isFinish;
	}
	
	@Override
	public boolean isFinish() {
		try {
			if(this.file.available() == 0)
			{
				return true;
			}
			else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return true;
		}
	}
}
