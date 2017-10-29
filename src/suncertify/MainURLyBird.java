/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify;

import suncertify.exception.InvalidModeSelectedException;
import suncertify.db.data.URLyBirdMode;
import javax.swing.SwingUtilities;
import suncertify.gui.URLyBirdConfigDialog;
import suncertify.db.data.URLyBirdPropertyFileManager;
import suncertify.services.*;
import java.io.File;

/**
 * This is the Main class which contains the main method which the program execution starts from in respect of the singleton command
 * provided at execution of the program
 * @author Segun
 */
public class MainURLyBird {
    
    /**
     * This is the main program main method
     * @param mode Array of String value or the command argument provided at program execution. String at index (0) denotes the first
     * argument provided at program execution via the command like 
     * @throws InvalidModeSelectedException if the mode command provided is not a valid mode required in this assignment
     */
    public static void main(String []mode) throws InvalidModeSelectedException{
        if(mode.length ==  1){
            if(mode[0].equals("server")){
                //if user provide "server" as argument.. this program should execute the Server
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        if(URLyBirdPropertyFileManager.isPropertiesFileExist()){
                            //if property file already exist.. means this isnt the first time of running this application
                            try{
                                String []configVal = URLyBirdPropertyFileManager.readServerProperties();
                                URLyBirdServerImpl server = new URLyBirdServerImpl(new File(configVal[0]), Long.parseLong(configVal[1]));
                                server.initializeServerUI();
                            }
                            catch(Exception nE){
                                //means the right format configuration is not set in the config file.. so, ask for reconfiguration
                                URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.SERVER);
                                configDialog.setVisible(true);
                                System.err.println(nE.getMessage());
                            }
                        }
                        else{
                            URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.SERVER);
                            configDialog.setVisible(true);
                        }
                    }
                });                
            }
            else if(mode[0].equals("alone")){
                //if "alone" is provided, the program should run without using any networking scheme and able to use the database on the local system
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        if(URLyBirdPropertyFileManager.isPropertiesFileExist()){
                            //if property file already exist.. means this isnt the first time of running this application
                            try{
                                String database = URLyBirdPropertyFileManager.readAloneProperties();
                                URLyBirdLocalImpl alone = new URLyBirdLocalImpl(database);
                                alone.initializeAloneUI();
                            }
                            catch(Exception nE){
                                System.err.println(nE.getMessage());
                            }
                        }
                        else{
                            URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.ALONE);
                            configDialog.setVisible(true);
                        }
                    }
                });                 
            }
            else{
                //an invalid command line argument was provided
                throw new InvalidModeSelectedException("Invalid single mode Argument provided at Executing this program or Wrong Argument provided,"
                        + " please read the user manual for the right form of executing program \ni.e "
                        + "java -jar runme.jar alone \t for executing program at Alone Mode" +
                        "\njava -jar runme.jar server \t for starting program Server Application (Server Mode)" +
                        "\njava -jar runme.jar \t (Note: no argument must be provided) to execute a Client program to connect to the Server (Client Mode");
            }
        }
        else if(mode.length == 0){
            //this is if empty, means this program assums a server has already been started which requires a client to remotely use the server
            //at the available host and port number via RMI
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    if(URLyBirdPropertyFileManager.isPropertiesFileExist()){
                        //if property file already exist.. means this isnt the first time of running this application
                        try{
                            String []configVal = URLyBirdPropertyFileManager.readClientProperties();
                            String hostName = configVal[0];
                            long port = Long.parseLong(configVal[1]);
                            URLyBirdClientImpl client = new URLyBirdClientImpl(hostName, port);
                            client.initializeClientUI();
                        }
                        catch(Exception nE){
                            //means the right format configuration is not set in the config file.. so, ask for reconfiguration
                            URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.CLIENT);
                            configDialog.setVisible(true);
                            System.err.println(nE.getMessage());
                        }
                    }
                    else{
                        URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(URLyBirdMode.CLIENT);
                        configDialog.setVisible(true);
                    }
                }
            }); 
        }
        else{
        //an invalid command line argument was provided
        throw new InvalidModeSelectedException("Invalid single mode Argument provided at Executing this program or Wrong Argument provided,"
            + " please read the user manual for the right form of executing program \ni.e "
            + "java -jar runme.jar alone \t for executing program at Alone Mode" +
            "\njava -jar runme.jar server \t for starting program Server Application (Server Mode)" +
            "\njava -jar runme.jar \t (Note: no argument must be provided) to execute a Client program to connect to the Server (Client Mode");
        }
    }
    
}
