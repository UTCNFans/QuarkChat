package QuarkChat.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextField;

import QuarkChat.encryption.types.EncrSym;
import QuarkChat.errorhandle.LogFile;
import QuarkChat.messageformats.FileFormat;
import QuarkChat.networking.MessageListener;
import QuarkChat.networking.MessageSender;
import QuarkChat.networking.WritableGUI;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.logging.Level;

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
import java.awt.Font;

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
	protected MessageSender msgSender;
	/* -------------- */

	/* Connexion Settings */
	public boolean uPnPEnable = true;
	protected JCheckBox chckbxUpnp;
	private JSeparator separator;
	private JCheckBox aesChkBox;
	/* ------------------ */

	/* Message I/O */
	protected JMenuItem mntmChooseFile;
	/* ---------------- */

	/* File chooser */
	protected JFileChooser fileChooser = null;
	/* ------------ */

	private JMenu mnHistory;
	private JCheckBox chckbxSave;
	private JMenu mnFiles;
	private JMenu mnNewMenu_1;

	/* FILE RECEIVE */
	public boolean FileReceive = false; // when it is false it will receive no file
	/* ------------ */

	/**
	 * Create the application.
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
		frmTrans.setBounds(200, 200, 400, 400);
		frmChat.setBounds(100, 100, 610, 453);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.setResizable(false);

		btnConnect = new JButton("Connect");
		btnConnect.setBounds(7, 7, 102, 23);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConnectButton.btn(chatThis);
			}
		});
		frmChat.getContentPane().setLayout(null);
		frmChat.getContentPane().add(btnConnect);

		browseBtn = new JButton("Browse");
		browseBtn.setBounds(115, 115, 190, 135);
		browseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				browser.showOpenDialog(browseBtn);
			}
		});

		browser = new JFileChooser();
		browser.setBounds(110, 110, 180, 130);
		frmTrans.getContentPane().add(browser);

		ipField = new JTextField();
		ipField.setBounds(121, 9, 367, 20);
		ipField.setText("localhost");
		frmChat.getContentPane().add(ipField);
		ipField.setColumns(10);

		sendPort = new JTextField();
		sendPort.setBounds(502, 9, 76, 20);
		sendPort.setText("8879");
		frmChat.getContentPane().add(sendPort);
		sendPort.setColumns(10);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 34, 590, 318);
		frmChat.getContentPane().add(scrollPane);

		document = new DefaultStyledDocument();
		chatBox = new JTextPane(document);
		chatBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
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
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SendWriter.normalMessage(chatThis, msgSender, msgListen.getHand());
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
				SendWriter.normalMessage(chatThis, msgSender, msgListen.getHand());
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
				if (aesChkBox.isSelected()) {
					aesKeyField.setEnabled(true);
					EncrSym.enable("AES", aesKeyField.getText());
				} else {
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

		CheckboxuPnP.chkbox(chatThis);
		CloseFrame.close(chatThis);

		mnHistory = new JMenu("History");
		menuBar.add(mnHistory);

		chckbxSave = new JCheckBox("Save");
		mnHistory.add(chckbxSave);

		mnFiles = new JMenu("Files");
		menuBar.add(mnFiles);

		mnNewMenu_1 = new JMenu("Upload File");
		mnFiles.add(mnNewMenu_1);

		this.fileChooser = new JFileChooser();
		mnNewMenu_1.add(fileChooser);
		this.fileChooser.setControlButtonsAreShown(false);
		this.fileChooser.setEnabled(false);

		this.fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.getSelectedFile() != null) {
					if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
						chatThis.write(
								"[File Loader] You chose to open this file: " + fileChooser.getSelectedFile().getName(),
								2);
						try {
							msgSender.fisier = new FileFormat(fileChooser.getSelectedFile().getAbsolutePath());
						} catch (FileNotFoundException error) {
							chatThis.write("[Error] There was an error at loading this file! Please check the logs.",
									2);
							LogFile.logger.log(Level.WARNING, "ChatGUI->fileChooser (action listener)", error);
						}

					}
					else if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
						fileChooser.setVisible(false);
					}
				}
			}
		});

		JCheckBox checkBox = new JCheckBox("Allow File Receive");
		mnFiles.add(checkBox);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkBox.isSelected()) {
					FileReceive = true;
				} else {
					FileReceive = false;
				}
			}
		});
	}

	// @Override
	public void write(String s, int i) {
		RecieveWriter.write(chatThis, i, s, msgListen.getHand());
	}

	public boolean returnSave() {
		return chckbxSave.isSelected();
	}
	
	public void scroll() {
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}
}
