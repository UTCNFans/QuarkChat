package QuarkChat.historyFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileHandler {

	File file;
	BufferedWriter writer;
	
	public static void main(String[] args) {
	FileHandler f= new FileHandler();
	f.deleteFile();;
	}
	
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

	public void deleteFile() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.delete();
	}
	
	public void closeFile() {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}