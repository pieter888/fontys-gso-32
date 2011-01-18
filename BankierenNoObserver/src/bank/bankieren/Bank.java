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
import javax.swing.JOptionPane;

public class Bank extends UnicastRemoteObject implements IBank {

    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private BasicPublisher publisher;
    private ICentraleBank centraleBank;

    public Bank(String name, int id) throws RemoteException, NotBoundException, MalformedURLException {
        accounts = new HashMap<Integer, IRekeningTbvBank>();
        clients = new ArrayList<IKlant>();
        nieuwReknr = 10 * id;
        this.name = name;
        this.publisher = new BasicPublisher(new String[]{});
    }

    public int openRekening(String name, String city) {
        if (name.equals("") || city.equals("")) {
            return -1;
        }

        IKlant klant = getKlant(name, city);
        IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
        accounts.put(nieuwReknr, account);
        nieuwReknr++;

        this.publisher.addProperty(account.getNr() + "");

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

    public IRekeningTbvBank getRekeningTbvCentraleBank(int nr) {
        return (IRekeningTbvBank) accounts.get(nr);
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
        boolean success = false;

        Money negative = Money.difference(new Money(0, money.getCurrency()), money);
        success = source_account.muteer(negative);
        if (!success) {
            return false;
        }

        if (source_account == null) {
            throw new NumberDoesntExistException("account " + source + " unknown at " + name);
        } else {
            IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
            if (dest_account == null) {
                //throw new NumberDoesntExistException("account " + destination
                //        + " unknown at " + name);
                try {
                    String message = this.centraleBank.maakOver(source, destination, money);
                    if (message.equals("overgemaakt")) {
                        success = true;
                        System.out.println(message);
                    } else {
                        System.out.println(message);
                        success = false;
                    }
                } catch (RemoteException ex) {
                    success = false;
                }
            } else {
                success = dest_account.muteer(money);
                publisher.inform(publisher, dest_account.getNr() + "", null, this);
            }
        }

        if (!success) //rollback
        {
            source_account.muteer(money);
        } else {
            publisher.inform(publisher, source_account.getNr() + "", null, this);
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
        this.centraleBank.addBank(this);
    }

    public ICentraleBank getCentraleBank() throws RemoteException {
        return this.centraleBank;
    }

    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        if (pce.getPropertyName().equals("centralebank")) {
            ICentraleBank centraleBank = (ICentraleBank) pce.getNewValue();
            this.setCentraleBank(centraleBank);
        }
    }

    public void muteerCentraal(int reknr, Money money) throws RemoteException {
        IRekeningTbvBank dest_account = (IRekeningTbvBank) this.getRekening(reknr);
        if (dest_account != null) {
            boolean success = dest_account.muteer(money);

            if (success) {
                publisher.inform(publisher, reknr + "", null, this);
            }
        }
    }
}
