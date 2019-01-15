package QuarkChat.historyFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Class which handles the history file
 **/
public class FileHandler {

	File file;
	BufferedWriter writer;
	
	public static void main(String[] args) {
	FileHandler f= new FileHandler();
	f.deleteFile();;
	}
	
	/**
	 * Default constructor. Initializes the file in which the history is written
	 **/
	public FileHandler() {
		// TODO Auto-generated constructor stub
		Date date = new Date();
		String name = date.toString().replaceAll(" ", "").replaceAll(":", "");
		//name.replaceAll(":", "");
		file = new File(System.getProperty("user.dir")+"\\"+name+".txt");
		try {
			if(file.createNewFile()) {
				writer = new BufferedWriter(new FileWriter(file));
			}
			else {
				writer = new BufferedWriter(new FileWriter(file));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes into file
	 **/
	public void write(String string) {
		try {if(writer!=null) {
			if(file.canWrite()) {
				writer.write(string);
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the created file
	 **/
	public void deleteFile() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.delete();
	}
	
	/**
	 * Closes the file
	 **/
	public void closeFile() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}