/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui.events;

import suncertify.db.data.URLyBirdMode;
import suncertify.services.*;
import suncertify.gui.URLyBirdConfigDialog;
import suncertify.db.data.URLyBirdPropertyFileManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.IOException;

/**
 * Handles the save button action of the configuration dialog, it implements <code>ActionListener</code>
 * @author Segun
 */
public class SaveConfigurationAction implements ActionListener{
    
    private URLyBirdMode mode;
    private URLyBirdConfigDialog config;
    private boolean isConfigured = false;
    
    public SaveConfigurationAction(URLyBirdConfigDialog c, URLyBirdMode m){
        config = c;
        mode = m;
    }
    
    public void actionPerformed(ActionEvent e){
        switch(mode){
            case SERVER:
                try{
                    final long portNumber;
                    portNumber = Long.parseLong(config.getPortNumberText());//convert the port text field value to long
                    new SwingWorker<Void, Void>(){
                        public Void doInBackground(){
                            if(!config.getDbFile().isFile() || portNumber == 0){
                                //means the configuration is not well filled when the file isnt pointing to any File instance and port number is empty or 0
                                JOptionPane.showMessageDialog(config, "INVALID CONFIGURATION DATA PROVIDED", "CONFIGURATION ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                try{
                                    URLyBirdPropertyFileManager.writeServerProperties(config.getDbFile().getAbsolutePath(), portNumber);
                                    isConfigured = true;
                                }
                                catch(IOException iO){
                                    JOptionPane.showMessageDialog(config, "CRITICAL ERROR SAVING CONFIGURATION", iO.getMessage(), JOptionPane.ERROR_MESSAGE);
                                    System.exit(1);//since a critical eror occur in IO. confuguration cannot be saved rentering starting the server useless
                                }
                            }
                            return null;
                        }
                        //when the background job is done .... should execute the code below
                        public void done(){
                            if(isConfigured){
                                try{
                                    //config.getCurrentService().backupData();//get the current service from the config dialof and backup the data to the file
                                    JOptionPane.showMessageDialog(config, "CONFIGURATION MODIFIED, Appllication will close and Relaunch", "MESSAGE", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                    config.dispose(); //dispose the dialog
                                    URLyBirdServerImpl impl = new URLyBirdServerImpl(config.getDbFile(), portNumber); //initialize the server implementation service for use
                                    impl.initializeServerUI();
                                }
                                catch(Exception e){
                                    JOptionPane.showMessageDialog(config, "CONFIGURATION CANNOT BE MODIFIED \nREASON: " + e.getMessage(), "ERROR", 
                                        JOptionPane.ERROR_MESSAGE);                                        
                                }
                            }
                        }
                    }.execute(); //execute the background job of saving the configuration data into the properties file
                }
                catch(NumberFormatException nE){ //this exception catched number format error when trying to convert the port text field value to a long
                    JOptionPane.showMessageDialog(config, "INVALID PORT NUMBER " + nE.getMessage(), "CONFIGURATION ERROR", JOptionPane.ERROR_MESSAGE);
                }                
                break;
            case CLIENT:
                try{
                    final long portNumber;
                    portNumber = Long.parseLong(config.getPortNumberText());//convert the port text field value to long
                    new SwingWorker<Void, Void>(){
                        public Void doInBackground(){
                            if(config.getHostName().isEmpty() || portNumber == 0){
                                //means the configuration is not well filled when the host name provided isnt empty and port number is empty or 0@
                                JOptionPane.showMessageDialog(config, "INVALID CONFIGURATION DATA PROVIDED", "CONFIGURATION ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                try{
                                    URLyBirdPropertyFileManager.writeClientProperties(config.getHostName(), portNumber);
                                    isConfigured = true;
                                }
                                catch(IOException iO){
                                    JOptionPane.showMessageDialog(config, "CRITICAL ERROR SAVING CONFIGURATION", iO.getMessage(), JOptionPane.ERROR_MESSAGE);
                                    System.exit(1);//since a critical eror occur in IO. confuguration cannot be saved rentering starting the client useless
                                }
                            }
                            return null;
                        }
                        //when the background job is done .... should execute the code below
                        public void done(){
                            if(isConfigured){
                                try{
                                    //config.getCurrentService().backupData();//get the current service from the config dialof and backup the data to the file
                                    JOptionPane.showMessageDialog(config, "CONFIGURATION MODIFIED, Appllication will close and Relaunch", "MESSAGE", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                    config.dispose(); //dispose the dialog
                                    URLyBirdClientImpl impl = new URLyBirdClientImpl(config.getHostName(), portNumber); //initialize the client implementation service for use
                                    impl.initializeClientUI();
                                }
                                catch(Exception e){
                                    JOptionPane.showMessageDialog(config, "CONFIGURATION CANNOT BE MODIFIED \nREASON: " + e.getMessage(), "ERROR", 
                                        JOptionPane.ERROR_MESSAGE);                                        
                                }
                            }
                        }
                    }.execute(); //execute the background job of saving the configuration data into the properties file
                }
                catch(NumberFormatException nE){ //this exception catched number format error when trying to convert the port text field value to a long
                    JOptionPane.showMessageDialog(config, "INVALID PORT NUMBER " + nE.getMessage(), "CONFIGURATION ERROR", JOptionPane.ERROR_MESSAGE);
                } 
                break;
            case ALONE:
                new SwingWorker<Void, Void>(){
                    public Void doInBackground(){
                        if(!config.getDbFile().isFile()){
                            //means the configuration is not well filled when the file isnt pointing to any File instance
                            JOptionPane.showMessageDialog(config, "INVALID CONFIGURATION DATA PROVIDED", "CONFIGURATION ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            try{
                                URLyBirdPropertyFileManager.writeAloneProperties(config.getDbFile().getAbsolutePath());
                                isConfigured = true;
                            }
                            catch(IOException iO){
                                JOptionPane.showMessageDialog(config, "CRITICAL ERROR SAVING CONFIGURATION", iO.getMessage(), JOptionPane.ERROR_MESSAGE);
                                System.exit(1);//since a critical eror occur in IO. confuguration cannot be saved rentering starting the app useless
                            }
                        }
                        return null;
                    }
                    //when the background job is done .... should execute the code below
                    public void done(){
                        if(isConfigured){
                            try{
                                //config.getCurrentService().backupData();//get the current service from the config dialof and backup the data to the file
                                JOptionPane.showMessageDialog(config, "CONFIGURATION MODIFIED, Appllication will close and Relaunch", "MESSAGE", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                config.dispose(); //dispose the dialog
                                URLyBirdLocalImpl impl = new URLyBirdLocalImpl(config.getDbFile()); //initialize the local implementation service for use
                                impl.initializeAloneUI();
                            }
                            catch(Exception e){
                                JOptionPane.showMessageDialog(config, "CONFIGURATION CANNOT BE MODIFIED \nREASON: " + e.getMessage(), "ERROR", 
                                    JOptionPane.ERROR_MESSAGE);                                        
                            }
                        }
                    }
                }.execute();
                break;
            default:
                
        }
    }
}
