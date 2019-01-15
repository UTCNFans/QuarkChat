package QuarkChat.networking;

import QuarkChat.encryption.types.FileDecryptor;
import QuarkChat.errorhandle.LogFile;
import QuarkChat.gui.ChatGUI;
import QuarkChat.historyFile.FileHandler;
import QuarkChat.messageformats.FileFormatR;
import QuarkChat.messageformats.MessageFormatR;
import QuarkChat.networking.upnp.UPnP;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Class which handles message receiving. It extends the Thread interface.
 * It uses the Socket and ServerSocket classes for this.
 **/
public class MessageListener extends Thread {

	private ServerSocket server;
	private Socket clientSocket;
	
	int port = 8877;
	ChatGUI gui;
	final int MaximumSize = 4 * 1024;
	FileHandler hand;
	
	// file to receive
	private FileFormatR fisierR = null;

	/**
	 * Constructor which receives a ChatGUI and a port and opens the connection.
	 **/
	public MessageListener(ChatGUI gui, int port) {
		
		this.gui = gui;
		this.port = port;
		try {
			server = new ServerSocket(port);
		} catch (IOException error) {
			LogFile.logger.log(Level.WARNING, "IOException", error);
		}
	}
	
	/**
	 * The run method overridden. It receives and it processes byte by byte
	 **/
	@Override
	public void run() {
		LogFile.logger.log(Level.INFO, "Connexion has been started!");
		hand = new FileHandler(); // Vicodrus added commit
		
		
		/* --- Open uPnP --- */
		if(gui.uPnPEnable == true)
		{
			gui.write("Waiting until port forwarding is configurated.... ", 2);
			gui.btnConnect.setEnabled(false);
			
			if(MessageOpenuPnP.open(port))
			{
				gui.write("Port forwarding was succesfully configurated!", 2);
			}
			else
			{
				gui.write("[Error] Port forwarding could not be configurated!", 2);
			}
			
			gui.btnConnect.setEnabled(true);
		}
		/* ----------------- */

		try {
			while((clientSocket = server.accept()) != null) { 
				InputStream in = clientSocket.getInputStream();
				BufferedInputStream BufferMsg = new BufferedInputStream(in);
				
				byte[] bufferTemp = new byte[2048];
				BufferMsg.read(bufferTemp);

				if(bufferTemp[0] == 0x34)
				{
					// it is a MESSAGE
					MessageFormatR mesaje = new MessageFormatR(bufferTemp);
					if(mesaje.indigest(null) != null) {
						String mesaj = new String(mesaje.indigest(null));
						gui.write(mesaj, 1);
					}else {
						gui.write("Ai primit un mesaj encriptat pe care nu l-ai putut decripta! "
								+ "(cheie sau setari gresite)", 2);
					}
				}
				else if(bufferTemp[0] == 0x35){
					// it is a FILE
					
					if(this.gui.FileReceive == true)
					{
						// can receive files
						if(this.fisierR == null) {
							// nothing received yet
							this.fisierR = new FileFormatR(bufferTemp);
							gui.write("[File Transfer] You received the file: " + this.fisierR.fileName, 2);
						}
						else if(Arrays.equals(FileFormatR.getSecureCode(bufferTemp), this.fisierR.secureCode) == false) {
							// it is a other file which is transferd 
							gui.write("[File Transfer] The file " + this.fisierR.fileName + " was not transfered.", 2);

							this.fisierR = new FileFormatR(bufferTemp);
							gui.write("[File Transfer] You received the file: " + this.fisierR.fileName, 2);
						}
						
						this.fisierR.indigest(bufferTemp);
						if(this.fisierR.isFinish == 1) {
							gui.write("[File Transfer] The file " + this.fisierR.fileName + " has been successfully transfered!", 2);
							
							if(this.fisierR.isEncrypted == true) {
								gui.write("[File Transfer] File decryption has started...", 2);
								
								// start file decryption
								FileDecryptor decrypt = new FileDecryptor(this.fisierR);
								decrypt.start();
							}
							
							this.fisierR = null; // stergem referinta dupa ce a fost transferat total
						}
					}
				}
				else {
					gui.write("[Security] You received an unknown type of message. Maybe your connexion has been compromised, we recommend to close the application immediately.", 2);
					LogFile.logger.log(Level.SEVERE, "[Security] You received an unknown type of message. Maybe your connexion has been compromised."
							+ "\nThe following DataInput array of bytes has been received: " + new String(bufferTemp));
				}
			}
		} catch (IOException error) {
			LogFile.logger.log(Level.FINEST, "IOException", error);
		}
	}
	
	
	/**
	 * Closes all connections 
	 **/
	public void closeConnexions(boolean del)
	{		
		try {
			if(UPnP.isMappedTCP(port))
			{
				UPnP.closePortTCP(port);
			}
			if(clientSocket != null)
			{
				this.clientSocket.close();
			}
			if(server != null)
			{
				this.server.close();
			}
			if(del==true) {
				hand.deleteFile();
			}
			else {
				hand.closeFile();
			}
			LogFile.logger.log(Level.INFO, "Connexion has been stopped");
		} catch (IOException error) {
			LogFile.logger.log(Level.FINEST, "chatproject.networking.MessageListener.closeConnexions", error);
		}
	}
	
	/**
	 * Gets the FileHandler
	 **/
	public FileHandler getHand() {
		return hand;
	}

	public void setHand(FileHandler hand) {
		this.hand = hand;
	}
	
}
