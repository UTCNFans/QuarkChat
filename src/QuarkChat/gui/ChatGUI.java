package QuarkChat.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextField;

import QuarkChat.encryption.types.EncrType;
import QuarkChat.networking.MessageListener;
import QuarkChat.networking.MessageSender;
import QuarkChat.networking.WritableGUI;
import QuarkChat.networking.ftp.FTPsender;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JSeparator;
import javax.swing.JMenuItem;

public class ChatGUI implements WritableGUI {

	public JFrame frmChat;
	public fileSender frmTrans;
	protected JTextField ipField;
	protected JTextField sendPort;
	protected JButton sendBtn;
	public JTextField msgBox;
	protected JTextPane chatBox;
	protected DefaultStyledDocument document;
	protected MessageListener listener;
	protected JScrollPane scrollPane;
	protected StyleContext context;
	protected Style style;
	protected JFileChooser browser; 
	protected JButton browseBtn;
	
	
	/* For Crypto Functions */
	protected EncrType crypto;
	protected JMenuBar menuBar;
	protected JMenu mnNewMenu;
	protected JTextField aesKeyField;
	/* -------------------- */
	
	
	/* For Error Handle */
	private ChatGUI chatThis = this;
	protected JMenu ConnexionSettings;
	protected Box horizontalBox;
	protected JLabel receivePortLable;
	public JButton btnConnect;
	protected JTextField listenPort;
	/* ---------------- */
	
	/* Connect button */
	protected MessageListener msgListen;
	/* -------------- */
	
	/* Connexion Settings */
	public boolean uPnPEnable = true;
	protected JCheckBox chckbxUpnp;
	private JSeparator separator;
	private JSeparator separator_1;
	private JCheckBox aesChkBox;
	/* ------------------ */
	
	/* Message I/O */
	protected MessageSender sender;
	protected JMenuItem mntmChooseFile;
	JMenu fileTrans;
	JTextField ftpSend;
	/* ---------------- */

	/**
	 *Create the application.
	 **/
	public ChatGUI(EncrType encry_arg) {
		this.crypto = encry_arg;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmTrans = new fileSender();
		frmChat.setForeground(Color.WHITE);
		frmTrans.setAlwaysOnTop(true);
		frmTrans.setVisible(false);
		frmTrans.setForeground(Color.WHITE);
		frmChat.setTitle("Chat");
		//frmTrans.setBounds(200,200,400,400);
		frmChat.setBounds(100, 100, 610, 453);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.setResizable(false);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(7, 7, 102, 23);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectButton.btn(chatThis);
			}
		});
		frmChat.getContentPane().setLayout(null);
		frmChat.getContentPane().add(btnConnect);
		
		browseBtn = new JButton("Browse");
		browseBtn.setBounds(115,115,190,135);
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				browser.showOpenDialog(browseBtn);
			}
		});
		
		browser = new JFileChooser();
		browser.setBounds(110,110,180,130);
		//frmTrans.getContentPane().add(browser);
		
		ipField = new JTextField();
		ipField.setBounds(121, 9, 367, 20);
		ipField.setText("localhost");
		frmChat.getContentPane().add(ipField);
		ipField.setColumns(10);
		
		sendPort = new JTextField();
		sendPort.setBounds(502, 9, 76, 20);
		sendPort.setText("4321");
		frmChat.getContentPane().add(sendPort);
		sendPort.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 34, 590, 318);
		frmChat.getContentPane().add(scrollPane);
		
		document = new DefaultStyledDocument();
		chatBox = new JTextPane(document);
		chatBox.setEditable(false);
		scrollPane.setViewportView(chatBox);
		
		context = new StyleContext();
        style = context.addStyle("test", null);
        

		
		msgBox = new JTextField();	
		msgBox.setBounds(7, 358, 495, 20);
		msgBox.setEnabled(false);
		
		msgBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
	            {
					MessageSender sender = new MessageSender(crypto, msgBox.getText(), ipField.getText(), Integer.parseInt(sendPort.getText()), chatThis);
					sendMessage.normalMessage(chatThis, sender);
	            }
			}
		});
		frmChat.getContentPane().add(msgBox);
		msgBox.setColumns(10);
		
		sendBtn = new JButton("Send");
		sendBtn.setBounds(519, 356, 78, 23);
		sendBtn.setEnabled(false);
		
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    sender = new MessageSender(crypto, msgBox.getText(), ipField.getText(), Integer.parseInt(sendPort.getText()), chatThis);
				sendMessage.normalMessage(chatThis, sender);
			}
		});
		frmChat.getContentPane().add(sendBtn);
		
		menuBar = new JMenuBar();
		frmChat.setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("Encryption");
		menuBar.add(mnNewMenu);
		
		aesKeyField = new JTextField();
		aesKeyField.setEnabled(false);
		aesKeyField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				crypto.Symmetric.enable("AES", aesKeyField.getText());
			}
		});
		
		aesChkBox = new JCheckBox("AES");
		aesChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(aesChkBox.isSelected())
				{
					aesKeyField.setEnabled(true);
				}
				else
				{
					aesKeyField.setEnabled(false);
				}
			}
		});
		mnNewMenu.add(aesChkBox);
		
		mnNewMenu.add(aesKeyField);
		aesKeyField.setColumns(10);
		
		ConnexionSettings = new JMenu("Connexion Settings");
		menuBar.add(ConnexionSettings);
		
		horizontalBox = Box.createHorizontalBox();
		ConnexionSettings.add(horizontalBox);
		
		receivePortLable = new JLabel("Receive Port");
		receivePortLable.setHorizontalAlignment(SwingConstants.LEFT);
		ConnexionSettings.add(receivePortLable);
		
		listenPort = new JTextField();
		listenPort.setText("8879");
		ConnexionSettings.add(listenPort);
		listenPort.setColumns(10);
		
		separator = new JSeparator();
		ConnexionSettings.add(separator);
		
		separator_1 = new JSeparator();
		ConnexionSettings.add(separator_1);
		
		fileTrans = new JMenu("FTP");
		menuBar.add(fileTrans);
		
		mntmChooseFile = new JMenuItem("Choose File");
		mntmChooseFile.setEnabled(false);
		mntmChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmTrans.setVisible(true);
			}
		});
		fileTrans.add(mntmChooseFile);
		
		JSeparator separator_2 = new JSeparator();
		fileTrans.add(separator_2);
		
		JLabel lblFtpPort = new JLabel("FTP Port");
		fileTrans.add(lblFtpPort);
		
		ftpSend = new JTextField();
		ftpSend.setText("8880");
		fileTrans.add(ftpSend);
		ftpSend.setColumns(10);
		
		FTPbrowser.show(chatThis);
				
		checkboxuPnP.chkbox(chatThis);
		
		closeFrame.close(chatThis);
	}


	//@Override
	public void write(String s, int i) {
		sendWrite.write(chatThis, i, s);
	}
}
