package centralebank;

import bank.bankieren.IBank;
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
    private Server server;

    public CentraleBank(String naam, Server server) throws RemoteException {
        this.naam = naam;
        this.banken = new ArrayList<IBank>();
        this.publisher = new BasicPublisher(new String[] {"centralebank"});
        this.server = server;
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
            this.server.updateInfo();
            return true;
        }

        return false;
    }

    public ArrayList<IBank> getBanken() throws RemoteException {
        return this.banken;
    }

    public String getNaam() throws RemoteException {
        return this.naam;
    }

    public void inform() throws RemoteException {
        this.publisher.inform(this, "centralebank", null, this.banken);
    }

    public void addListener(RemotePropertyListener rl, String string) throws RemoteException {
        this.publisher.addListener(rl, string);
    }

    public void removeListener(RemotePropertyListener rl, String string) throws RemoteException {
        this.publisher.removeListener(rl, string);
    }
}
