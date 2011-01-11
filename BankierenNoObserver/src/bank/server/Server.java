package bank.server;

import bank.bankieren.*;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import centralebank.ICentraleBank;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Mike
 */
public class Server extends javax.swing.JFrame implements RemotePropertyListener {

    private IBank bank;
    private IBalie balie;

    /** Creates new form Server */
    public Server(String nameBank) throws RemoteException {
        initComponents();

        try {
            String address = java.net.InetAddress.getLocalHost().getHostAddress();
            int port = 1099;

            Properties props = new Properties();
            String rmiBalie = address + ":" + port + "/" + nameBank;
            props.setProperty("balie", rmiBalie);

            FileOutputStream out = new FileOutputStream(nameBank + ".props");
            props.store(out, null);
            out.close();

            //Variabelen initializeren
            this.bank = new Bank(nameBank);
            this.balie = new Balie(this.bank);

            //Bind bank
            Naming.rebind("Bank", this.bank);

            //Listener toevoegen
            this.bank.setCentraleBank((ICentraleBank) Naming.lookup("rmi://localhost:1099/CentraleBank"));
            this.bank.getCentraleBank().addListener(this.bank, "centralebank");

            //TODO: zorgen dat de bank ook bekend is bij de centrale bank
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Er kon geen verbinding worden gemaakt met de centrale bank!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }

        this.updateInfo();

        //Maak zichtbaar
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCentraleBank = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bank");

        lblCentraleBank.setText("Centrale bank: -");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCentraleBank)
                .addContainerGap(312, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCentraleBank)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            //TODO: args[0] als bank naam
            new Server("testbank");
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "Er kon geen verbinding worden gemaakt met de server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Update informatie over de bank in het frame
     * @throws RemoteException
     */
    public void updateInfo() throws RemoteException {
        //Title
        this.setTitle("Bank - " + this.bank.getName());

        //Centrale bank
        String centraleBank = this.bank.getCentraleBank().getNaam();
        this.lblCentraleBank.setText("Centrale bank: " + centraleBank);
    }

    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        System.out.println("Updated");
        if (pce.getPropertyName().equals("centralebank")) {
            try {
                ICentraleBank centraleBank = (ICentraleBank) Naming.lookup("rmi://localhost:1099/CentraleBank");

                this.bank.setCentraleBank(centraleBank);
            } catch (NotBoundException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblCentraleBank;
    // End of variables declaration//GEN-END:variables
}