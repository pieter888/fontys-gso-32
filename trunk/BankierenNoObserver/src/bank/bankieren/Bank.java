package bank.bankieren;

import centralebank.ICentraleBank;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.util.*;
import java.beans.PropertyChangeEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank extends UnicastRemoteObject implements IBank {

    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private BasicPublisher publisher;
    private ICentraleBank centraleBank;

    public Bank(String name) throws RemoteException, NotBoundException, MalformedURLException {
        accounts = new HashMap<Integer, IRekeningTbvBank>();
        clients = new ArrayList<IKlant>();
        nieuwReknr = 100000000;
        this.name = name;
        this.publisher = new BasicPublisher(new String[]{"bank"});
    }

    public int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        nieuwReknr++;
        return nieuwReknr - 1;
    }

    private IKlant getKlant(String name, String city) {
        for (IKlant k : clients) {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr) {
        return accounts.get(nr);
    }

    public boolean maakOver(int source, int destination, Money money)
            throws NumberDoesntExistException {
        if (source == destination) {
            throw new RuntimeException(
                    "cannot transfer money to your own account");
        }
        if (!money.isPositive()) {
            throw new RuntimeException("money must be positive");
        }

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) {
            throw new NumberDoesntExistException("account " + source
                    + " unknown at " + name);
        }

        Money negative = Money.difference(new Money(0, money.getCurrency()),
                money);
        boolean success = source_account.muteer(negative);
        if (!success) {
            return false;
        }

        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        if (dest_account == null) {
            throw new NumberDoesntExistException("account " + destination
                    + " unknown at " + name);
        }
        success = dest_account.muteer(money);

        if (!success) // rollback
        {
            source_account.muteer(money);
        } else {
            publisher.inform(publisher, "bank", null, this);
        }

        return success;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addListener(RemotePropertyListener listener, String property) {
        publisher.addListener(listener, property);
    }

    public void removeListener(RemotePropertyListener listener, String property) {
        publisher.removeListener(listener, property);
    }

    public void setCentraleBank(ICentraleBank centraleBank) throws RemoteException {
        this.centraleBank = centraleBank;
    }

    public ICentraleBank getCentraleBank() throws RemoteException {
        return this.centraleBank;
    }

    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        if(pce.getPropertyName().equals("centralebank")) {
            ICentraleBank centraleBank = (ICentraleBank) pce.getNewValue();
            this.setCentraleBank(centraleBank);
        }
    }
}