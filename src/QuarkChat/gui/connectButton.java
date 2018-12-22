package QuarkChat.gui;

import QuarkChat.networking.MessageListener;
import QuarkChat.networking.MessageSender;

public class connectButton {
	public static void btn(ChatGUI gui)
	{
		if(gui.btnConnect.getText() == "Connect")
		{
			gui.btnConnect.setText("Disconnect");
			
			/* -- Open listening the ports -- */
			gui.msgListen = new MessageListener(gui, Integer.parseInt(gui.listenPort.getText()));
			gui.msgListen.start();
			/* ------------------------------ */
			
			/* -- Open sender -- */
			gui.msgSender = new MessageSender(gui.ipField.getText(), Integer.parseInt(gui.sendPort.getText()), gui);
			gui.msgSender.start();
			/* ---------------- */ 
			
			/* Disable forms in order to prevent change of data during connexion */
			gui.listenPort.setEnabled(false); // disable listen port while the Connexion is made, in order to prevent errors
			gui.sendPort.setEnabled(false);
			gui.ipField.setEnabled(false);
			
			gui.sendBtn.setEnabled(true);
			gui.msgBox.setEnabled(true);
			
			gui.frmTrans.setEnabled(true);
			
			gui.fileChooser.setControlButtonsAreShown(true);

			/* ----------------------------------------------------------------- */
			
			/* -- file transfer -- */
			if(gui.FileReceive == false) {
				gui.write("[File Transfer] In order to receive files from your partner, please active the 'File Receive Option' from menu 'Files'", 2);
			}
			/* ------------------- */
			
			gui.write("[Connexion] Connexions has successfully started!", 2);
		}
		else
		{
			/* first stop premare */
			gui.msgSender.stopThread = true;
			try {
				Thread.sleep(101); // to be sure that evreything is altright
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block 
				e.printStackTrace();
			}
			
			/* -- Close listening the ports -- */
			gui.msgListen.interrupt();
			gui.msgListen.closeConnexions(!gui.returnSave());
			/* ------------------------------- */
			
			/* -- Close sender the ports -- */
			gui.msgSender.interrupt();
			gui.msgSender.closeConnexion();
			/* ------------------------------- */
			
			gui.btnConnect.setText("Connect");
			
			/* Enable the forms */
			gui.listenPort.setEnabled(true); // disable listen port while the Connexion is made, in order to prevent errors
			gui.sendPort.setEnabled(true);
			gui.ipField.setEnabled(true);
			
			gui.msgBox.setText(null); // empty box
			gui.sendBtn.setEnabled(false);
			gui.msgBox.setEnabled(false);
			
			gui.fileChooser.setEnabled(false);
			gui.fileChooser.setControlButtonsAreShown(false);
			/* --------------- */
		
			gui.write("[Connexion] All conexions and all ports has been stoped!", 2);
		}
	}
}
