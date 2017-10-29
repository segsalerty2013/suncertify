/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import suncertify.db.data.URLyBirdMode;
import suncertify.gui.*;
import java.rmi.*;
import java.rmi.registry.*;
import javax.swing.JOptionPane;

/**
 * Implementation of the client Application coding
 * @author Segun
 */
public class URLyBirdClientImpl{
    
    private URLyBirdApplicationFrame application;
    private RemoteURLyBirdServices services;
    
    /**
     * This constructor accepts remote host address/IP with which the Server Services resides and the long value of the port number to initialise
     * the implementation of client's mode of this application.
     * @param host String value of the host address/IP
     * @param p long value of the port number to locate the registry where services resides
     */
    public URLyBirdClientImpl(String host, long p){
        long portNumber = p;
        String remoteName = "rmi://" + host + ":" + p + "/URLyBird";
        try{
            LocateRegistry.getRegistry((int) portNumber);
            services = (RemoteURLyBirdServices) Naming.lookup(remoteName);
        }
        catch(Exception E){
            JOptionPane.showMessageDialog(null, E.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            //if error occurs when connecting to remote system... show a JOptionPane to confirm if user wants to exit program or edit the config
            int op = JOptionPane.showConfirmDialog(null, "Do you want to reconfigure Settings", "RECONFIGURATION", JOptionPane.YES_NO_OPTION);
            if(op == JOptionPane.YES_OPTION){
                URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.CLIENT);
                configDialog.setVisible(true);
            }
            else{
                System.exit(0);
            }
        }
    }
    
    /**
     * This function initialise the client User Interface by displaying the user Graphical User Interface of the client system
     */
    public void initializeClientUI(){
        application = new URLyBirdClientFrame(services);
        application.packPanel();
        application.setVisible(true);
    }
    
}
