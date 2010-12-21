package bank.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.observer.RemotePropertyListener;

public class BankiersessieDialoog extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private GuiController controller;
	
	JLabel lbAccount = new JLabel();
	JLabel lbBalance = new JLabel();
	JLabel lbNameCity = new JLabel();
	TitledBorder titledBorder1;
	TitledBorder titledBorder2;
	TitledBorder titledBorder3;
	TitledBorder titledBorder4;
	TitledBorder titledBorder5;
	TitledBorder titledBorder6;
	JTextArea taAccount = new JTextArea();
	JButton btTransfer = new JButton();
	JTextArea taMessages = new JTextArea();
	JTextArea taAmount = new JTextArea();

	public BankiersessieDialoog(GuiController controller) throws RemoteException {
		this.controller = controller;
		init();
		
		IRekening rekening;

		rekening = controller.getRekening();
		lbAccount.setText(rekening.getNr() + "");
		lbBalance.setText(rekening.getSaldo() + "");
		String eigenaar = rekening.getEigenaar().getNaam() + " te "
				+ rekening.getEigenaar().getPlaats();
		lbNameCity.setText(eigenaar);
		setTitle(eigenaar);
		}

	private void init() {
		this.setBackground(Color.lightGray);
		setVisible(true);
		setSize(400, 350);
		titledBorder1 = new TitledBorder("saldo");
		titledBorder1.setTitleColor(Color.lightGray);
		titledBorder2 = new TitledBorder("nr");
		titledBorder2.setTitleColor(Color.lightGray);
		titledBorder3 = new TitledBorder("naam en plaats");
		titledBorder3.setTitleColor(Color.lightGray);
		titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(
				Color.white, new Color(134, 134, 134)), "mededelingen");
		titledBorder4.setTitleColor(Color.black);
		titledBorder5 = new TitledBorder("bedrag");
		titledBorder5.setTitleColor(Color.lightGray);
		titledBorder6 = new TitledBorder("naar");
		titledBorder6.setTitleColor(Color.lightGray);
		lbAccount.setBackground(Color.white);
		lbAccount.setBorder(titledBorder2);
		lbAccount.setBounds(new Rectangle(29, 34, 86, 40));
		getContentPane().setLayout(null);
		lbBalance.setBorder(titledBorder1);
		lbBalance.setBounds(new Rectangle(29, 73, 66, 40));
		lbNameCity.setBorder(titledBorder3);
		lbNameCity.setBounds(new Rectangle(123, 34, 250, 40));
		taAmount.setBorder(titledBorder5);
		taAmount.setBounds(new Rectangle(123, 73, 62, 40));
		taAccount.setBounds(new Rectangle(198, 73, 86, 40));
		taAccount.setBorder(titledBorder6);
		btTransfer.setBounds(new Rectangle(273, 73, 99, 40));
		btTransfer.setText("maak over");
		btTransfer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btTransfer_actionPerformed(e);
			}
		});
		taMessages.setBackground(Color.lightGray);
		taMessages.setForeground(Color.darkGray);
		taMessages.setBorder(titledBorder4);
		taMessages.setBounds(new Rectangle(29, 117, 344, 168));

		getContentPane().add(lbBalance, null);
		getContentPane().add(lbAccount, null);
		getContentPane().add(lbNameCity, null);
		getContentPane().add(taAmount, null);
		getContentPane().add(taAccount, null);
		getContentPane().add(btTransfer, null);
		getContentPane().add(taMessages, null);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		    	controller.logUit();
		        System.exit(0); 
		    }
		});
	}

	void btTransfer_actionPerformed(ActionEvent e) {
		int from = Integer.parseInt(lbAccount.getText());
		int to = Integer.parseInt(taAccount.getText());
		if (from == to)
			taMessages.append("can't transfer money to your own account");
		long centen = (long) (Double.parseDouble(taAmount.getText()) * 100);
		controller.maakOver(to, new Money(centen, Money.EURO));
	}

	public void setBalance(String balance) {
		lbBalance.setText(balance);
	}
	
	public void setMessage(String message) {
		taMessages.setText(message);
	}
}
