package bank.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class LoginDialoog extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private GuiController controller;
	
	JPanel loginPanel = new JPanel();
	JTextField tfAccount = new JTextField();
	JButton btLogin = new JButton();
	JButton btOpenAccount = new JButton();
	JTextArea taMessages = new JTextArea();
	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	TitledBorder titledBorder3;
	TitledBorder titledBorder4;
	TitledBorder titledBorder5;
	JPasswordField tfPassword = new JPasswordField();


	public LoginDialoog(GuiController controller, String bankNaam) {
		this.controller = controller;
		
		try {
			init(bankNaam);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(String bankNaam)throws Exception {

		titledBorder1 = new TitledBorder("");
		titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(178, 178, 178)), "accountnaam");
		titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(178, 178, 178)), "paswoord");
		titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(134, 134, 134)), "mededelingen");

		titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(178, 178, 178)), "wachtwoord");
		tfAccount.setBorder(titledBorder2);
		tfAccount.setBounds(new Rectangle(26, 20, 150, 45));

		btLogin.setBounds(new Rectangle(26, 100, 101, 45));
		btLogin.setText("inloggen");
		btLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btLogin_actionPerformed(e);
			}
		});
		btOpenAccount.setText("open rekening");
		btOpenAccount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btOpenAccount_actionPerformed(e);
			}
		});
		btOpenAccount.setBounds(new Rectangle(227, 100, 106, 45));
		btOpenAccount.setMargin(new Insets(2, 4, 2, 4));
		taMessages.setBackground(Color.lightGray);
		taMessages.setBorder(titledBorder4);
		taMessages.setBounds(new Rectangle(26, 154, 276, 127));

		tfPassword.setBorder(titledBorder5);
		tfPassword.setBounds(new Rectangle(226, 21, 109, 45));

		setLayout(null);
		add(tfAccount);
		add(btLogin);
		add(btOpenAccount);
		add(taMessages);
		add(tfPassword);
		setSize(400,350);
		setTitle("Please log in at "+bankNaam);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	void btOpenAccount_actionPerformed(ActionEvent e) {
		tfAccount.setText("");
		tfPassword.setText("");
		controller.openRekeningDialoog();
	}	

	void btLogin_actionPerformed(ActionEvent e) {
		controller.login(tfAccount.getText(), new String(tfPassword.getPassword()));
	}

	public void setAccountname(String accountname) {
		tfAccount.setText(accountname);
	}
	
	public void setMessage(String message){
		taMessages.append(message);	
	}	
}
