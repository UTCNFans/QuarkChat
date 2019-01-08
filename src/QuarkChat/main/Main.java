package QuarkChat.main;

import java.awt.EventQueue;
import java.util.logging.Level;

import QuarkChat.encryption.types.FileDecryptor;
import QuarkChat.errorhandle.LogFile;
import QuarkChat.gui.ChatGUI;
import QuarkChat.update.CheckUpdate;
import QuarkChat.update.UpdateGUI;

public class Main {

	public static void main(String[] args) {
		/* Error handle */
		LogFile.Settings();
		/* -> use LogFile. (it is static)
		/* ------------ */
		
		/* Check for updates */
		UpdateGUI updateWindow = new UpdateGUI();
		if(CheckUpdate.isUpdate() == true) // exists an update available
		{
			try {
				updateWindow.frame.setVisible(true);
			} 
			catch (Exception error)
			{
				LogFile.logger.log(Level.SEVERE, "Update client could not start properly!", error);
			}
		}
		else
		{
			LogFile.logger.log(Level.INFO, "No update available!");
		}
		/* ----------------- */
		
		/* Launch the application*/
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//encryptModules.disableAll();
					ChatGUI window = new ChatGUI();
					window.frmChat.setVisible(true);
					
					// set fileDecryption informations
					FileDecryptor.forceStop = false;
					FileDecryptor.gui = window;
					
					if(updateWindow.frame.isVisible())
					{
						updateWindow.frame.toFront();
					}

				} catch (Exception error) {
					LogFile.logger.log(Level.SEVERE, "Program could not start properly or it has a fatal error", error);
				}
			}
		});
	}

}
