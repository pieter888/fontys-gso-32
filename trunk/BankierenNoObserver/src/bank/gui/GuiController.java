package bank.gui;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;

public class GuiController extends UnicastRemoteObject {

	private static final long serialVersionUID = 1L;
	
	private IBalie balie; 
	private IBankiersessie sessie;
	
	private LoginDialoog loginDialoog;
	private OpenRekeningDialoog openDialoog;
	private BankiersessieDialoog bankierDialoog;
	
	
	public GuiController(IBalie balie, String bankNaam) throws RemoteException {
		this.balie = balie;
		loginDialoog = new LoginDialoog(this, bankNaam);
		loginDialoog.setVisible(true);
	}
	
	public void login(String accountnaam, String wachtwoord) {
		try {
			sessie = balie.logIn(accountnaam,wachtwoord); 
			if (sessie == null) {
			    loginDialoog.setMessage("accountname or password not correct");
			}
			else {
				loginDialoog.dispose();
				bankierDialoog = new BankiersessieDialoog(this);
				bankierDialoog.setVisible(true);
				System.out.println(accountnaam+" "+wachtwoord);
			}
		} catch (RemoteException e1) {
			loginDialoog.setMessage("bad connection with counter");
			e1.printStackTrace();
		}
	} 		
			
	public void openRekening(String naam, String plaats, String wachtwoord){
	    String accountname;
		try {
			accountname = balie.openRekening(naam, plaats, wachtwoord);
			if (accountname == null) return;
		    loginDialoog.setAccountname(accountname);
		    openDialoog.dispose();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}	
	}
	
	public void logUit() {
		try {
			sessie.logUit();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
		
	public void maakOver(int bestemming, Money bedrag) {
		try{
			sessie.maakOver(bestemming, bedrag);
                        System.out.println("MIKE MIKE MAD MIKE");
		} catch (RemoteException e1) {
			e1.printStackTrace();
			bankierDialoog.setMessage("verbinding verbroken");
		} catch (NumberDoesntExistException e1) {
			e1.printStackTrace();
			bankierDialoog.setMessage(e1.getMessage());
		} catch (InvalidSessionException e1) {
			e1.printStackTrace();
			bankierDialoog.setMessage(e1.getMessage());
		}
	}

	public IRekening getRekening() {
		try{
			return sessie.getRekening();
		} catch (RemoteException e) {
			e.printStackTrace();
			bankierDialoog.setMessage("verbinding verbroken");
			return null; 
		} catch (InvalidSessionException e) {
			e.printStackTrace();
			bankierDialoog.setMessage("bankiersessie is verlopen");
			return null; 
		}
	}

	public void openRekeningDialoog() {
		this.openDialoog = new OpenRekeningDialoog(this);
		openDialoog.setVisible(true);		
	}
}

