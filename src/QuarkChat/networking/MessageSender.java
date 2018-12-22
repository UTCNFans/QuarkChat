package QuarkChat.networking;

import QuarkChat.errorhandle.LogFile;
import QuarkChat.gui.ChatGUI;
import QuarkChat.messageformats.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;


public class MessageSender extends Thread{
	
	private String hostname;
	private int port;
	private ChatGUI error_handle;
	
	// lista de mesaje pe care o adaugam in coada
	public Queue<String> lista_mesaje = new LinkedList<String>(); // access public
	
	// fisierul care trebuie trimis
	public FileFormat fisier = null;
	
	// socket for sending informations
	Socket client = null; // connect socket
	
	// refresh frequency
	private final int MAX_frequency = 100;
	
	// to stop the thread
	public boolean stopThread;

	public MessageSender(String hostname, int port, ChatGUI gui_args) {
		this.hostname = hostname;
		this.port = port;
		this.error_handle = gui_args;
	}
	
	private void ThreadSleep() {
		// pause for a while
		try {
			Thread.sleep(MAX_frequency);
		} catch (InterruptedException error) {
			this.error_handle.write("[Error] Something went SEVER wrong at MessageSender -> Thread synchronizing", 2);
			LogFile.logger.log(Level.SEVERE, "MessageSender -> Thread Sleep", error);
		}
	}

	@Override
	public void run(){
		// disable stop when it runs
		this.stopThread = false;
		
		TOTAL_EXIT: while(!stopThread) {
			while(!(this.fisier != null && this.fisier.isFinish() == false) && this.lista_mesaje.peek() == null) {
				// do nothing;
				this.ThreadSleep();
				
				if(this.stopThread) {
					break TOTAL_EXIT;
				}
			}
			
			try {
				// configure connexion
				this.client = new Socket(this.hostname, this.port); 

				// daca exista mesaje care trebuie trimise
				if(this.lista_mesaje.peek() != null) {
					// exista mesaje care trebuie trimise
					String mesajS = this.lista_mesaje.remove();
					MessageFormat mesaj = new MessageFormat(mesajS);
					// se trimite mesajul
					this.client.getOutputStream().write(mesaj.getData());
				}
				else if(this.fisier != null){		
					// daca nu exista mesaje in coada, se vor trimite fisierele
					if(this.fisier.isFinish() == false) {
						this.client.getOutputStream().write(fisier.getData());
					}
					else if(this.fisier.isFinish() == true) {
						error_handle.write("[File Transfer] File has been succesfully transfered!", 2);
						this.fisier = null; // stergem obiectul curent
					}
				}
				
				this.client.close();
			} catch (UnknownHostException error) {
				error_handle.write("[Error] Could not connect to the client! Maybe you have no internet connexion.", 2);
				LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageSender->run", error);
				this.fisier = null; // stergem obiectul curent
				if(!this.lista_mesaje.isEmpty()) {
					this.lista_mesaje.remove(); // stergem mesajul
				}
				
			} catch (IOException error) {
				error_handle.write("[Error] Could not connect to the client port! Maybe the client is listening another port.", 2);
				LogFile.logger.log(Level.WARNING, "chatproject.networking.MessageSender->run", error);
				this.fisier = null; // stergem obiectul curent
				if(!this.lista_mesaje.isEmpty()) {
					this.lista_mesaje.remove(); // stergem mesajul
				}
			} finally {
				this.ThreadSleep();
			}
		}
	}
	
	public void closeConnexion() {
		if(this.client != null) {
			if(this.fisier != null && this.fisier.isFinish() != true) {
				this.error_handle.write("[File Transfer] File Transfer is stopping...", 2);
				this.fisier = null;
			}
			try {
				this.client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
