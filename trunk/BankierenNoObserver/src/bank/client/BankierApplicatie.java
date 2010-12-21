package bank.client;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;

import bank.gui.GuiController;
import bank.internettoegang.IBalie;

public class BankierApplicatie {

	private IBalie balie;
	
	public BankierApplicatie(String bankNaam) {
		
		try {
			FileInputStream in = new FileInputStream(bankNaam+".props");
			Properties props = new Properties();
			props.load(in);
			String rmiBalie = props.getProperty("balie");
			in.close();

			balie = (IBalie) Naming.lookup("rmi://" + rmiBalie);

			new GuiController(balie, bankNaam);
				
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	 
	public static void main(String[] args) {
		new BankierApplicatie("testbank");
	}
}
