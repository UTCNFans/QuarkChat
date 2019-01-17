package QuarkChat.update;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import QuarkChat.errorhandle.LogFile;
import java.awt.Window.Type;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.awt.event.ActionEvent;

public class GUIUpdater {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIUpdater window = new GUIUpdater();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIUpdater() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setType(Type.POPUP);
		frame.setBounds(100, 100, 404, 307);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblUpdateSystem = new JLabel("Update Client");
		lblUpdateSystem.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblUpdateSystem.setEnabled(false);
		frame.getContentPane().add(lblUpdateSystem, BorderLayout.NORTH);
		
		JLabel lblAnUpdateIs = new JLabel("An update is abaible");
		frame.getContentPane().add(lblAnUpdateIs, BorderLayout.CENTER);
		
		JLabel lblAnUpdateTo = new JLabel("Do you want to get the last software version?");
		lblAnUpdateTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAnUpdateTo.setBounds(12, 121, 398, 63);
		frame.getContentPane().add(lblAnUpdateTo);
		
		JLabel lblUpdateClient = new JLabel("Update Client");
		lblUpdateClient.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUpdateClient.setBounds(12, 4, 226, 54);
		frame.getContentPane().add(lblUpdateClient);
		
		JLabel label = new JLabel("An update to this application is available.");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(12, 83, 350, 63);
		frame.getContentPane().add(label);
		
		JButton btnNewButton = new JButton("Get Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    try {
					Desktop.getDesktop().browse(new URI("https://utcnfans.github.io/QuarkChat/"));
				} catch (IOException error) {
					LogFile.logger.log(Level.WARNING, "IOException", error);
				} catch (URISyntaxException error) {
					LogFile.logger.log(Level.WARNING, "URISyntaxException", error);
				}
			    frame.dispose();
			}
		});
		btnNewButton.setBounds(33, 197, 108, 35);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Continue");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(243, 197, 119, 35);
		frame.getContentPane().add(btnNewButton_1);
	}
}
