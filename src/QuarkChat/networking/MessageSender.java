package QuarkChat.networking;

import QuarkChat.encryption.types.*;
import QuarkChat.errorhandle.LogFile;
import QuarkChat.gui.ChatGUI;
import QuarkChat.encryption.AES;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;


public class MessageSender extends Thread{
	
	private EncrType crypto;
	private String message, hostname;
	private int port;
	private ChatGUI error_handle;

	public MessageSender(EncrType encry_args, String message, String hostname, int port, ChatGUI gui_args) {
		this.crypto = encry_args;
		this.message = message;
		this.hostname = hostname;
		this.port = port;
		this.error_handle = gui_args;
	}

	@Override
	public void run(){
		try {
			Socket client = new Socket(hostname, port);
			byte []cryptMsg;
			
			if(crypto.Symmetric.isEnable("AES") == true) // if AES encryption is ON
			{
				cryptMsg = AES.encrypt(message, crypto.Symmetric.key[0]);
			}
			else
			{
				cryptMsg = message.getBytes();
			}
			
			client.getOutputStream().write(cryptMsg);
			client.close();
		} catch (UnknownHostException error) {
			error_handle.write("[Error] Could not connect to the client! Maybe you have no internet connexion.", 2);
			LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageSender->run", error);
		} catch (IOException error) {
			error_handle.write("[Error] Could not connect to the client port! Maybe the client is listening another port.", 2);
			LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageSender->run", error);
		}
	}
	
	
}
