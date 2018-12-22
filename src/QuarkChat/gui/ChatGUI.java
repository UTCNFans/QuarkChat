package QuarkChat.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextField;

import QuarkChat.encryption.types.EncrSym;
import QuarkChat.networking.MessageListener;
import QuarkChat.networking.MessageSender;
import QuarkChat.networking.WritableGUI;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSeparator;
import javax.swing.JMenuItem;

public class ChatGUI implements WritableGUI {

	public JFrame frmChat;
	public JFrame frmTrans;
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
	private JCheckBox aesChkBox;
	/* ------------------ */
	
	/* Message I/O */
	protected MessageSender sender;
	protected JMenuItem mntmChooseFile;
	/* ---------------- */
	
	private JMenu mnHistory;
	private JCheckBox chckbxSave;
	private JMenu mnFiles;
	private JComboBox chosenFile;
	private JButton transferBtn;
	private JTextField chosenDirectory;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;

	/**
	 *Create the application.
	 **/
	public ChatGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmTrans = new JFrame();
		frmChat.setForeground(Color.WHITE);
		frmTrans.setAlwaysOnTop(true);
		frmTrans.setVisible(false);
		frmTrans.setForeground(Color.WHITE);
		frmChat.setTitle("Chat");
		frmTrans.setBounds(200,200,400,400);
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
		frmTrans.getContentPane().add(browser);
		
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
					MessageSender sender = new MessageSender(msgBox.getText(), ipField.getText(), Integer.parseInt(sendPort.getText()), chatThis);
					sendMessage.normalMessage(chatThis, sender,msgListen.getHand());
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
			    sender = new MessageSender(msgBox.getText(), ipField.getText(), Integer.parseInt(sendPort.getText()), chatThis);
				sendMessage.normalMessage(chatThis, sender,msgListen.getHand());
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
				EncrSym.enable("AES", aesKeyField.getText());
			}
		});
		
		aesChkBox = new JCheckBox("AES");
		aesChkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(aesChkBox.isSelected())
				{
					aesKeyField.setEnabled(true);
					EncrSym.enable("AES", aesKeyField.getText());
				}
				else
				{
					aesKeyField.setEnabled(false);
					EncrSym.disable("AES");
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
						
		checkboxuPnP.chkbox(chatThis);
		closeFrame.close(chatThis);
		
		mnHistory = new JMenu("History");
		menuBar.add(mnHistory);
		
		chckbxSave = new JCheckBox("Save");
		mnHistory.add(chckbxSave);
		
		mnFiles = new JMenu("Files");
		menuBar.add(mnFiles);
		
		lblNewLabel = new JLabel("Path");
		mnFiles.add(lblNewLabel);
		
		chosenDirectory = new JTextField();
		mnFiles.add(chosenDirectory);
		chosenDirectory.setColumns(10);
		
		lblNewLabel_1 = new JLabel("File");
		mnFiles.add(lblNewLabel_1);
		
		chosenFile = new JComboBox();
		mnFiles.add(chosenFile);
		
		transferBtn = new JButton("Transfer");
		mnFiles.add(transferBtn);
	}


	//@Override
	public void write(String s, int i) {
		sendWrite.write(chatThis, i, s,msgListen.getHand());
	}
	
	public boolean returnSave() {
		return chckbxSave.isSelected();
	}
}
