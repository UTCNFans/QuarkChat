package QuarkChat.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class fileSender extends JFrame {

	private JPanel contentPane;
	private JTextField filePath;
	private JFileChooser browser; 
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnSend;

	/**
	 * Create the frame.
	 */
	public fileSender() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 438, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		filePath = new JTextField();
		filePath.setBounds(10, 30, 315, 20);
		filePath.setText("Choose File");
		contentPane.add(filePath);
		filePath.setColumns(10);
		
		browser = new JFileChooser();
		browser.setBounds(110,110,180,130);
		
		JButton browseBtn = new JButton("Browse");
		browseBtn.setBounds(335, 29, 89, 23);
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				browser.showOpenDialog(browseBtn);
			}
		});
		contentPane.add(browseBtn);
		
		textField = new JTextField();
		textField.setBounds(291, 92, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(291, 151, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(93, 92, 86, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(169, 196, 89, 23);
		contentPane.add(btnSend);
		
		JLabel lblNewLabel = new JLabel("Send Port");
		lblNewLabel.setBounds(10, 95, 73, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Data Send");
		lblNewLabel_1.setBounds(189, 95, 92, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblDataRecieve = new JLabel("Data Recieve");
		lblDataRecieve.setBounds(189, 154, 92, 14);
		contentPane.add(lblDataRecieve);
	}
}
