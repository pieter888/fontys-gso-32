package bank.internettoegang;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

public class Bankiersessie extends UnicastRemoteObject implements
        IBankiersessie, RemotePropertyListener {

    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;
    private BasicPublisher publisher;

    public Bankiersessie(int reknr, IBank bank) throws RemoteException {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;

        this.publisher = new BasicPublisher(new String[]{"saldo"});
        this.bank.addListener(this, "bank");
    }

    public boolean isGeldig() throws RemoteException {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag)
            throws NumberDoesntExistException, InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        if (reknr == bestemming) {
            throw new RuntimeException(
                    "source and destination must be different");
        }
        if (!bedrag.isPositive()) {
            throw new RuntimeException("amount must be positive");
        }


        return bank.maakOver(reknr, bestemming, bedrag);
    }

    private void updateLaatsteAanroep() throws InvalidSessionException, RemoteException {
        if (!isGeldig()) {
            bank.removeListener(this, "bank");
            throw new InvalidSessionException("session has been expired");
        }

        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
            RemoteException {

        updateLaatsteAanroep();

        return bank.getRekening(reknr);
    }

    @Override
    public void logUit() throws RemoteException {
        UnicastRemoteObject.unexportObject(this, true);
        this.bank.removeListener(this, "bank");

    }

    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        this.bank = (IBank) evt.getNewValue();
        publisher.inform(this, "saldo", null, this.bank.getRekening(reknr).getSaldo());
    }

    public void addListener(RemotePropertyListener listener, String property) {
        publisher.addListener(listener, property);
    }

    public void removeListener(RemotePropertyListener listener, String property) {
        publisher.removeListener(listener, property);
    }
}
