package centralebank;

import bank.bankieren.IBank;
import bank.bankieren.Money;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Mike
 */
public interface ICentraleBank extends Remote {
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
     * @return Naam van de centrale bank
     * @throws RemoteException
     */
    public String getNaam() throws RemoteException;

    /**
     * Maak geld over van de ene bank naar de andere bank
     * 
     * @param source Afzender
     * @param destination Ontvanger
     * @param money Geld
     * @return String
     */
    public String maakOver(int source, int destination, Money money) throws RemoteException;
    public int aantalBanken() throws RemoteException;
}
