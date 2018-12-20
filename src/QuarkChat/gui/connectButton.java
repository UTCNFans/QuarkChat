package QuarkChat.gui;

import QuarkChat.networking.MessageListener;

public class connectButton {
	public static void btn(ChatGUI gui)
	{
		if(gui.btnConnect.getText() == "Connect")
		{
			gui.btnConnect.setText("Disconnect");
			
			/* -- Open listening the ports -- */
			gui.msgListen = new MessageListener(gui, Integer.parseInt(gui.listenPort.getText()), gui.crypto);
			gui.msgListen.start();
			/* ------------------------------ */
			
			/* Disable forms in order to prevent change of data during connexion */
			gui.listenPort.setEnabled(false); // disable listen port while the Connexion is made, in order to prevent errors
			gui.sendPort.setEnabled(false);
			gui.ipField.setEnabled(false);
			
			gui.sendBtn.setEnabled(true);
			gui.msgBox.setEnabled(true);
			/* ----------------------------------------------------------------- */
		}
		else
		{
			/* -- Close listening the ports -- */
			gui.msgListen.interrupt();
			gui.msgListen.closeConnexions(!gui.returnSave());
			/* ------------------------------- */
			gui.btnConnect.setText("Connect");
			
			/* Enable the forms */
			gui.listenPort.setEnabled(true); // disable listen port while the Connexion is made, in order to prevent errors
			gui.sendPort.setEnabled(true);
			gui.ipField.setEnabled(true);
			
			gui.msgBox.setText(null); // empty box
			gui.sendBtn.setEnabled(false);
			gui.msgBox.setEnabled(false);
			/* --------------- */
		}
	}
}
