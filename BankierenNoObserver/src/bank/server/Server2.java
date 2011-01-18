/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.server;

import java.rmi.RemoteException;
import javax.swing.JOptionPane;

/**
 *
 * @author Mike
 */
public class Server2 {
    public static void main(String[] args) {
        try {
            //TODO: args[0] als bank naam
            new Server("vincentspauperbank");
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null, "Er kon geen verbinding worden gemaakt met de server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
