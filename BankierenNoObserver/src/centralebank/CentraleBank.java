package centralebank;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.IRekeningTbvBank;
import bank.bankieren.Money;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Mike
 */
public class CentraleBank extends UnicastRemoteObject implements ICentraleBank {

    private String naam;
    private ArrayList<IBank> banken;
    private BasicPublisher publisher;

    public CentraleBank(String naam) throws RemoteException {
        this.naam = naam;
        this.banken = new ArrayList<IBank>();
        this.publisher = new BasicPublisher(new String[]{"centralebank"});
    }

    public boolean addBank(IBank bank) throws RemoteException {
        boolean found = false;

        for (IBank temp : this.banken) {
            if (temp.getName().equalsIgnoreCase(bank.getName())) {
                found = true;
                break;
            }
        }

        if (!found) {
            this.banken.add(bank);
            return true;
        }

        return false;
    }

    public String getNaam() throws RemoteException {
        return this.naam;
    }

    public String maakOver(int source, int destination, Money money) throws RemoteException {
        String message = "Account niet gevonden!";

        for (IBank bank : this.banken) {
            IRekeningTbvBank dest_account = (IRekeningTbvBank) bank.getRekening(destination);
            if (dest_account != null) {
                bank.muteerCentraal(dest_account.getNr(), money);
                return "overgemaakt";
            }
        }

        return message;
    }

    public int aantalBanken() throws RemoteException {
        return this.banken.size();
    }
}
