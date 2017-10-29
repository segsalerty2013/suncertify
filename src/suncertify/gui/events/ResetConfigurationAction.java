/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui.events;

import suncertify.db.data.URLyBirdMode;
import suncertify.gui.URLyBirdConfigDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This implements ActionListener to handle Reset Button action on the configuration Dialog in respect to the MODE
 * this application is running on !
 * @author Segun
 */
public class ResetConfigurationAction implements ActionListener{
   
    private URLyBirdMode mode;
    private URLyBirdConfigDialog config;
    
    /**
     * Constructs this ActionListener with the Configuration dialog and the mode with which the program is executing
     * @param c URLyBirdConfigDialog instance of the configuration dialog
     * @param m URLyBirdMode enumerator value of the MODE with which the application is executing
     */
    public ResetConfigurationAction(URLyBirdConfigDialog c, URLyBirdMode m){
        config = c;
        mode = m;        
    }
    
    public void actionPerformed(ActionEvent e){
        switch(mode){
            case SERVER:
                config.getDbChooserButton().setText("SELECT");
                config.getDbChooserButton().setEnabled(true);//re-enable the chooser button
                config.getPortNumberTextField().setText("");
                break;
            case ALONE:
                config.getDbChooserButton().setText("SELECT");
                config.getDbChooserButton().setEnabled(true);//re-enable the chooser button
                break;
            case CLIENT:
                config.getPortNumberTextField().setText("");
                config.getHostNameTextField().setText("");
                break;
            default:                
        }
    }
    
}
