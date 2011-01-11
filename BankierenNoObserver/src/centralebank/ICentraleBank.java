package centralebank;

import bank.bankieren.IBank;
import fontys.observer.Publisher;
import fontys.observer.RemotePropertyListener;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Mike
 */
public interface ICentraleBank extends Remote, Publisher {
    /**
     * Meld een bank aan bij de centrale bank
     * Deze word alleen toegevoegd als deze nog niet aangemeld was.
     *
     * @param bank Nieuwe bank
     * @return True als de bank is aangemeld, false als de bank al aangemeld was
     * @throws RemoteException
     */
    public boolean addBank(IBank bank) throws RemoteException;

    /**
     * @return Iterator met alle banken die bekend zijn bij de centrale bank
     * @throws RemoteException
     */
    public ArrayList<IBank> getBanken() throws RemoteException;

    /**
     * @return Naam van de centrale bank
     * @throws RemoteException
     */
    public String getNaam() throws RemoteException;

    /**
     * Stuur de nieuwe centrale bank door naar de clients
     * @throws RemoteException
     */
    public void inform() throws RemoteException;
}
