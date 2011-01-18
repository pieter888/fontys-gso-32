package bank.bankieren;

import centralebank.ICentraleBank;
import fontys.observer.Publisher;
import fontys.observer.RemotePropertyListener;
import fontys.util.*;
import java.rmi.RemoteException;

/**
 * @author 871059
 * 
 */
public interface IBank extends Publisher, RemotePropertyListener {
	/**
	 * creatie van een nieuwe bankrekening; het gegenereerde bankrekeningnummer
	 * is identificerend voor de nieuwe bankrekening; als de klant
	 * geidentificeerd door naam en plaats nog niet bestaat wordt er ook een
	 * nieuwe klant aangemaakt
	 * 
	 * @param naam
	 *            van de eigenaar van de nieuwe bankrekening
	 * @param plaats
	 *            de woonplaats van de eigenaar van de nieuwe bankrekening
	 * @return -1 zodra naam of plaats een lege string of rekeningnummer niet
	 *         door centrale kon worden vrijgegeven en anders het nummer van de
	 *         gecreeerde bankrekening
	 */
	int openRekening(String naam, String plaats) throws RemoteException;

	/**
	 * er wordt bedrag overgemaakt van de bankrekening met nummer bron naar de
	 * bankrekening met nummer bestemming
	 * 
	 * @param bron
	 * @param bestemming
	 *            ongelijk aan bron
	 * @param bedrag
	 *            is groter dan 0
	 * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
	 * @throws NumberDoesntExistException
	 *             als een van de twee bankrekeningnummers onbekend is
	 */
	boolean maakOver(int bron, int bestemming, Money bedrag)
			throws NumberDoesntExistException, RemoteException;

	/**
	 * @param nr
	 * @return de bankrekening met nummer nr mits bij deze bank bekend, anders null
	 */
	IRekening getRekening(int nr) throws RemoteException;

	/**
	 * @return de naam van deze bank
	 */
	String getName() throws RemoteException;

        /**
         * Methode om de centrale bank te wijzigen
         * @throws RemoteException
         */
        void setCentraleBank(ICentraleBank centraleBank) throws RemoteException;

        /**
         * @return Centrale bank die bij de bank hoort
         * @throws RemoteException
         */
        public ICentraleBank getCentraleBank() throws RemoteException;

        /**
         * Voeg listener aan publisher
         * @param listener
         * @param property
         * @throws RemoteException
         *          Als server niet kan worden gevonden
         */
        public void addListener(RemotePropertyListener listener, String property) throws RemoteException;

         /**
         * Voeg listener aan publisher
         * @param listener
         * @param property
         * @throws RemoteException
         *          Als server niet kan worden gevonden
         */
        public void removeListener(RemotePropertyListener listener, String property) throws RemoteException;
        public void muteerCentraal(int reknr, Money money) throws RemoteException;
}
