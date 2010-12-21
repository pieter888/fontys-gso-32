package bank.gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class OpenRekeningDialoog extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private GuiController controller;
	
	JTextField tfName = new JTextField();
	JTextField tfCity = new JTextField();
	JButton btOpen = new JButton();
	JLabel lbName = new JLabel();
	JLabel lbCity = new JLabel();
	JLabel lbPassword = new JLabel();
	JPasswordField tfPassword = new JPasswordField();

	public OpenRekeningDialoog(GuiController controller) {
		this.controller = controller;
		 try {
			 init();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	}
	
	 private void init() throws Exception {
		tfName.setBounds(new Rectangle(51, 30, 167, 31));
		setLayout(null);
		tfCity.setBounds(new Rectangle(51, 105, 167, 31));
		btOpen.setBounds(new Rectangle(239, 187, 135, 31));
		btOpen.setText("open rekening");
		btOpen.addActionListener(new java.awt.event.ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		    btOpen_actionPerformed(e);
		  }
		});
		lbName.setText("naam");
		lbName.setBounds(new Rectangle(51, 10, 167, 20));
		lbCity.setBounds(new Rectangle(51, 86, 167, 20));
		lbCity.setText("plaats");
		lbPassword.setBounds(new Rectangle(51, 168, 167, 20));
		lbPassword.setText("wachtwoord");
		tfPassword.setBounds(new Rectangle(51, 187, 167, 31));
		add(tfName, null);
		add(tfCity, null);
		add(btOpen, null);
		add(lbName, null);
		add(lbCity, null);
		add(lbPassword, null);
		add(tfPassword, null);
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 }
	 
	 void btOpen_actionPerformed(ActionEvent e) {
		    String password = new String(tfPassword.getPassword());
		    controller.openRekening(tfName.getText(), tfCity.getText(), password);			}
		  }
