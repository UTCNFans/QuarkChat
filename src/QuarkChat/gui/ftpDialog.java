package QuarkChat.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

public class ftpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ftpDialog() {
		setBounds(100, 100, 300, 174);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[110px][30px][124px]", "[74px][23px]"));
		
		JButton btnNewButton = new JButton("Yes");
		contentPanel.add(btnNewButton, "cell 0 1,alignx right,aligny top");
		
		JButton btnNewButton_1 = new JButton("No");
		contentPanel.add(btnNewButton_1, "cell 2 1,alignx left,aligny top");
		
		JLabel lblNewLabel = new JLabel("You are being sent a file.\r\n Would you like to download it?");
		contentPanel.add(lblNewLabel, "cell 0 0 3 1,grow");
	}
}
