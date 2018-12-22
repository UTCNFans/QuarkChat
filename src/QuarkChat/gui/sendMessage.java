package QuarkChat.gui;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;

import QuarkChat.historyFile.FileHandler;
import QuarkChat.networking.MessageSender;

public class sendMessage{
	public static void normalMessage(ChatGUI gui, MessageSender sender, FileHandler hand)
	{
		StyleConstants.setForeground(gui.style, Color.GREEN);
        try {
        	gui.document.insertString(gui.document.getLength(), "Me: ", gui.style);
        	hand.write("Me: ");
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        StyleConstants.setForeground(gui.style, Color.BLACK);
        try {
        	gui.document.insertString(gui.document.getLength(), gui.msgBox.getText() + System.lineSeparator(), gui.style);
        	hand.write(gui.msgBox.getText()+"\n");
        } catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		//chatBox.append("Me: " + msgBox.getText() + System.lineSeparator());
		sender.lista_mesaje.add(gui.msgBox.getText()); //adaugam mesajul la coada
		gui.msgBox.setText(null);
	}
}
