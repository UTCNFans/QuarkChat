package QuarkChat.gui;

import java.util.logging.Level;

import javax.swing.JFrame;

import QuarkChat.encryption.types.FileDecryptor;
import QuarkChat.errorhandle.LogFile;

public class CloseFrame {
	public static void close(ChatGUI gui)
	{
		gui.frmChat.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		gui.frmChat.addWindowListener(new java.awt.event.WindowAdapter() { 
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if(gui.msgListen != null)
		    	{
				    gui.msgListen.closeConnexions(!gui.returnSave());
		    	}
		    	
		    	if(gui.msgSender != null) {
		    		gui.msgSender.stopThread = true;
		    		FileDecryptor.forceStop = true;
		    		
		    		try {
						Thread.sleep(101);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    		gui.msgSender.closeConnexion();
		    		gui.msgSender.interrupt();
		    		FileDecryptor.interrupted();
		    	}
		    	
			    LogFile.logger.log(Level.INFO, "Application is closing", windowEvent);
		    	System.exit(0);
		    }
		});
	}
}
