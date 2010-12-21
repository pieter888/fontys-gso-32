package bank.internettoegang;

import junit.framework.Assert;
import bank.bankieren.IRekening;
import bank.bankieren.IBank;
import bank.bankieren.Bank;
import java.rmi.RemoteException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mike
 */
public class IBalieTest {

    private IBank bank;
    private IBalie balie;

    @Before
    public void setUp() throws RemoteException {
        this.bank = new Bank("Test");
        this.balie = new Balie(this.bank);
    }

    /**
     * Test of openRekening method, of class IBalie.
     */
    @Test
    public void testOpenRekening() throws Exception {
        /* @return null zodra naam of plaats een lege string of wachtwoord minder dan
         * vier of meer dan acht karakters lang is en anders de gegenereerde
         * accountnaam(8 karakters lang) waarmee er toegang tot de nieuwe bankrekening
         * kan worden verkregen
         */
        String br = this.balie.openRekening("", "Tilburg", "geheim");
        Assert.assertNull("Naam mag niet leeg zijn", br);

        br = this.balie.openRekening("Mike", "", "geheim");
        Assert.assertNull("Plaats mag niet leeg zijn", br);

        br = this.balie.openRekening("Mike", "Tilburg", "geh");
        Assert.assertNull("Wachtwoord moet 4 of meer karakers lang zijn", br);

        br = this.balie.openRekening("Mike", "Tilburg", "geheimpje");
        Assert.assertNull("Wachtwoord mag niet meer dan 8 karakters hebben", br);

        br = this.balie.openRekening("Mike", "Tilburg", "geheim");
        Assert.assertEquals("Accountnaam moet 8 karakters lang zijn", br.length(), 8);

        /* creatie van een nieuwe bankrekening; het gegenereerde bankrekeningnummer is
         * identificerend voor de nieuwe bankrekening en heeft een saldo van 0 euro
         */
        IBankiersessie bs = this.balie.logIn(br, "geheim");
        IRekening rek = bs.getRekening();

        Assert.assertEquals("Saldo moet 0,00 euro zijn", rek.getSaldo().getValue(), "0,00");
    }

    /**
     * Test of logIn method, of class IBalie.
     */
    @Test
    public void testLogIn() throws Exception {
        /**
         * er wordt een sessie opgestart voor het login-account met de naam
         * accountnaam mits het wachtwoord correct is
         *
         * @return de gegenereerde sessie waarbinnen de gebruiker
         * toegang krijgt tot de bankrekening die hoort bij het betreffende login-
         * account mits accountnaam en wachtwoord matchen, anders null
         */
        String br = this.balie.openRekening("Mike", "Tilburg", "geheim");

        IBankiersessie bs = this.balie.logIn(br, "verkeerd");
        Assert.assertNull("Wachtwoord mag niet verkeerd zijn", bs);

        bs = this.balie.logIn("Mike", "geheim");
        Assert.assertNull("Accountnaam mag niet verkeerd zijn", bs);

        bs = this.balie.logIn(br, "geheim");
        Assert.assertNotNull("Bankiersessie moet gecreeerd worden als de gegevens goed waren", bs);
    }
}
