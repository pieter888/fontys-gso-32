package centralebank;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Mike
 */
public class Server extends javax.swing.JFrame {

    private ICentraleBank centraleBank;

    /** Creates new form Server */
    public Server(String naam) throws RemoteException, MalformedURLException {
        initComponents();

        //Variabelen initializeren
        this.centraleBank = new CentraleBank(naam);

        //Create RMI registry
        LocateRegistry.createRegistry(1099);
        System.setProperty("java.rmi.server.codebase", "http://localhost/bank/");
        Naming.rebind("CentraleBank", this.centraleBank);

        //Maak zichtbaar
        this.setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Centrale Bank");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws RemoteException, MalformedURLException, NotBoundException {
        Server server = null;

        //CentraleBank initializeren
        try {
            server = new Server(args[0]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(null, "Er moet wel een naam voor de centrale bank worden opgegeven!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return Centrale bank die bij de server hoort
     */
    public ICentraleBank getCentraleBank() {
        return this.centraleBank;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
