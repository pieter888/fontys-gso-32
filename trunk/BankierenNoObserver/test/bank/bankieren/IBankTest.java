/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.observer.RemotePropertyListener;
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
public class IBankTest {

    private IBank b1, b2;
    private int r1, r2, r3;

    public IBankTest() {
    }

    @Before
    public void setUp() throws RemoteException {
        b1 = new Bank("test1");
        r1 = b1.openRekening("Mike", "Tilburg");
        r2 = b1.openRekening("Gino", "Tilburg");
        r3 = b1.openRekening("Vincent", "Tilburg");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testOpenRekening() throws RemoteException {
        int rTest = b1.openRekening("Mike", "Tilburg");
        boolean bool = b1.getRekening(r1).getEigenaar().equals(b1.getRekening(rTest).getEigenaar());
        assertTrue("De Klant objecten (eigenaar) moet hetzelfde zijn", bool);

        int openRekening = b1.openRekening("", "Tilburg");
        assertEquals("Naam mag niet leeg zijn", openRekening, -1);

        openRekening = b1.openRekening("Mike", "");
        assertEquals("Woonplaats mag niet leeg zijn", openRekening, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverBestemmingGelijk() throws NumberDoesntExistException, RemoteException {
        /* @param bron
         * @param bestemming
         *      ongelijk aan bron
         */
        b1.maakOver(r1, r1, new Money(1000, Money.EURO));
        fail("Rekeningen zijn gelijk");
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverBedragPositief() throws NumberDoesntExistException, RemoteException {
        /* @param bedrag
         * is groter dan 0
         */
        b1.maakOver(r1, r2, new Money(-1, Money.EURO));
        fail("Bedrag moet positief zijn");
    }

    @Test
    public void testMaakOver() throws NumberDoesntExistException, RemoteException {
        /* @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
         */

        Money m1 = b1.getRekening(r1).getSaldo();
        Money m2 = b1.getRekening(r2).getSaldo();
        Money mPositief = new Money(1000, Money.EURO);
        Money mNegatief = new Money(-1000, Money.EURO);

        Boolean bool = b1.maakOver(r1, r2, mPositief);

        assertEquals("Geld is niet van r1 afgeschreven", b1.getRekening(r1).getSaldo(), Money.sum(m1, mNegatief));
        assertEquals("Geld is niet op r2 gezet", b1.getRekening(r2).getSaldo(), Money.sum(m2, mPositief));
        assertTrue("Geld moet zijn overgemaakt", bool);


    }

    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverExcp() throws NumberDoesntExistException, RemoteException {
        /* @throws NumberDoesntExistException
         *             als een van de twee bankrekeningnummers onbekend is
         */
        b1.maakOver(9999, 10000, new Money(1000, Money.EURO));
        fail("1 vd 2 rekeningen bestaat niet");
    }

    @Test
    public void testGetName() throws RemoteException {
        /**
         * @return de naam van deze bank
         */
        assertEquals("Hoort de naam 'test1' te hebben", b1.getName(), "test1");
    }

    @Test
    public void testGetRekening() throws RemoteException {
        //* @return de bankrekening met nummer nr mits bij deze bank bekend, anders null
        assertNotNull("Rekeningnummer moet geen null zijn", b1.getRekening(r1));
        assertNull("Rekeningnummer moet null zijn", b1.getRekening(0));
        assertEquals("Rekening is niet geinitializeerd", b1.getRekening(r1).getNr(), r1);
    }
}
