package QuarkChat.networking;

import QuarkChat.encryption.types.*;
import QuarkChat.errorhandle.LogFile;
import QuarkChat.historyFile.fileHandler;
import QuarkChat.networking.upnp.UPnP;
import QuarkChat.encryption.AES;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;


public class MessageListener extends Thread {

	private ServerSocket server;
	private Socket clientSocket;
	
	int port = 8877;
	WritableGUI gui;
	
	final int MaximumSize = 4 * 1024;
	EncrType crypto;
	fileHandler hand;
	
	public MessageListener(WritableGUI gui, int port, EncrType encry_args) {
		this.gui = gui;
		this.port = port;
		this.crypto = encry_args;
		try {
			server = new ServerSocket(port);
		} catch (IOException error) {
			LogFile.logger.log(Level.WARNING, "IOException", error);
		}
	}
	
	private int readBuffer(BufferedInputStream BuffMsg, byte[] ByteData) throws IOException
	{
		int lengthMsg = 0;
		byte TempBuff;
		
		TempBuff = (byte) BuffMsg.read();
		while(TempBuff != -1)
		{
			ByteData[lengthMsg++] = TempBuff;
			TempBuff = (byte) BuffMsg.read();
		}
		
		return lengthMsg;
	}
	
	private byte[] copyData(byte[] d_1, int length) // copy d_1 with fix lenght
	{
		byte[] d_2 = new byte[length];
		
		for(int i=0; i<length; i++)
		{
			d_2[i] = d_1[i];
		}
		
		return d_2;
	}
	
	@Override
	public void run() {
		byte[] InputData = new byte[MaximumSize];
		LogFile.logger.log(Level.INFO, "Connexion has been started!");
		hand=new fileHandler();
		
		/* --- Open uPnP --- */
		MessageOpenuPnP.open(port);
		/* ----------------- */

		try {
			while((clientSocket = server.accept()) != null) { 
				System.out.print("Da");
				InputStream in = clientSocket.getInputStream();
				
				
				BufferedInputStream BufferMsg = new BufferedInputStream(in);	
				int BuffSize = readBuffer(BufferMsg, InputData); /* Read data */
				
				if(BuffSize == -1) // nothing found
				{
					System.err.println("[Error] Empty message! Maybe your connexion is compromised!");
					return; // force exit
				}
				
				String line;
				byte[] TempData = copyData(InputData, BuffSize);
				
				if(crypto.Symmetric.isEnable("AES") == true)
				{
					line = AES.decrypt(TempData, crypto.Symmetric.key[0]);
				}
				else
				{
					line = new String(TempData);
				}
				
				if (line != null)
				{
					gui.write(line,1);
				}
				else
				{
					if(AES.isError())
					{
						if(AES.isWrong()) {
							gui.write("[AES encryption] Wrong password!",0);
						}
						else {
							gui.write("[AES encryption] Wrong encryption specifications!", 0);
							LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageListener->run", 
									"Wrong decryption key! The used key ends with: " + 
							crypto.Symmetric.key[0].substring(crypto.Symmetric.key[0].length()-1));

						}
					}
					gui.write("[Error] Error chatproject.networking.94 -> null line.", 0);
					LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageListener->run", "null pointer or null line or bad transmission");
				}
			}
		} catch (IOException error) {
			LogFile.logger.log(Level.FINEST, "IOException", error);
		}
	}
	
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
			LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageListener.closeConnexions", error);
		}
	}

	public fileHandler getHand() {
		return hand;
	}

	public void setHand(fileHandler hand) {
		this.hand = hand;
	}
	
	
}
