/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.internettoegang;

import java.lang.RuntimeException;
import bank.bankieren.IRekening;
import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.Money;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vincent
 */
public class IBankiersessieTest {
    private IBank b1;
    private int r1, r2;
    private IBankiersessie bs;

    public IBankiersessieTest() throws RemoteException {
        b1 = new Bank("Test1");
        r1 = b1.openRekening("Mike", "Tilburg");
        r2 = b1.openRekening("Vincent", "Tilburg");

        bs = new Bankiersessie(r1, b1);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void isGeldigTest() throws RemoteException, NumberDoesntExistException, InvalidSessionException, InterruptedException
    {
       /**
	 * @returns true als de laatste aanroep van getRekening of maakOver voor deze
	 *          sessie minder dan GELDIGHEIDSDUUR geleden is
	 *          en er geen communicatiestoornis in de tussentijd is opgetreden,
	 *          anders false
	 */

        bs = new Bankiersessie(r1, b1);
        bs.getRekening();

        boolean bool = bs.isGeldig();
        assertTrue("Fout; Geldigheidsduur is nog niet verlopen", bool);
        
        /* testen: zet Bankiersessie max geldigheid op 600
         Thread.sleep(1000);
        bool = bs.isGeldig();
        assertFalse("Fout; Geldigheidsduur is verlopen", bool);
         */
    }

    @Test
    public void maakOverTest() throws RemoteException, NumberDoesntExistException, InvalidSessionException
    {

        /**
	 * er wordt bedrag overgemaakt van de bankrekening met het nummer bron naar
	 * de bankrekening met nummer bestemming
         */

         Money s1 = b1.getRekening(r1).getSaldo();
         Money s2 = b1.getRekening(r2).getSaldo();
         Money mPositief = new Money(1000, Money.EURO);
         Money mNegatief = new Money(-1000, Money.EURO);

         IBankiersessie bs = new Bankiersessie(r1, b1);
         boolean bool = bs.maakOver(r2, mPositief);

         assertEquals("Geld is niet van r1 afgeschreven", b1.getRekening(r1).getSaldo(), Money.sum(s1, mNegatief));
         assertEquals("Geld is niet op r2 gezet", b1.getRekening(r2).getSaldo(), Money.sum(s2, mPositief));
         assertTrue("Geld moet zijn overgemaakt", bool);
    }

    @Test(expected = RuntimeException.class)
    public void maakOverBestemming() throws RemoteException, NumberDoesntExistException, InvalidSessionException
    {
        /* @param bestemming
	 *            is ongelijk aan rekeningnummer van deze bankiersessie
         */
         bs.maakOver(r1, new Money(1, Money.EURO));

         fail("Rekening mag niet hetzelfde zijn");
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverBedragPositief() throws NumberDoesntExistException, InvalidSessionException, RemoteException
    {
        /* @param bedrag
	 *            is groter dan 0
         */
        bs.maakOver(r2, new Money(-1, Money.EURO));
        fail("Bedrag moet positief zijn");
    }

    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverNDEE() throws NumberDoesntExistException, InvalidSessionException, RemoteException
    {
        /* @throws NumberDoesntExistException
	 *             als bestemming onbekend is
         */

        bs.maakOver(10000, new Money(1, Money.EURO));
        fail("Rekening bestaat niet");
    }

    @Test(expected = InvalidSessionException.class)
    public void testMaakOverISE() throws NumberDoesntExistException, InvalidSessionException, RemoteException, InterruptedException
    {
        /* @throws InvalidSessionException
	 *             als sessie niet meer geldig is
	 */

        // testen: zet Bankiersessie max geldigheid op 600
        // Thread.sleep(1000);
        bs.maakOver(r2, new Money(1, Money.EURO));
        fail("Sessie is niet meer geldig");
    }

    @Test(expected = InvalidSessionException.class)
    public void getRekeningISETest() throws InvalidSessionException, RemoteException
    {
        bs.getRekening();

        // testen: zet Bankiersessie max geldigheid op 600
        // Thread.sleep(1000);
        fail("Sessie is niet meer geldig");
    }
}