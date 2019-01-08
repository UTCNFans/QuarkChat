package QuarkChat.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class CheckboxuPnP {
	public static void chkbox(ChatGUI gui)
	{
		gui.chckbxUpnp = new JCheckBox("uPnP");
		gui.chckbxUpnp.setSelected(true);
		gui.chckbxUpnp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.uPnPEnable = gui.chckbxUpnp.isSelected();
			}
		});
		gui.ConnexionSettings.add(gui.chckbxUpnp);
	}
}
