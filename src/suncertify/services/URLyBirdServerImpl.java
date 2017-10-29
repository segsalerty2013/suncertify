/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import suncertify.db.*;
import suncertify.db.data.*;
import suncertify.gui.URLyBirdServerFrame;
import suncertify.exception.URLyBirdException;
import java.util.*;
import java.io.File;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.MalformedURLException;

/**
 * Implementation of the Server Services / Functions
 * @author Segun
 */
public class URLyBirdServerImpl extends UnicastRemoteObject implements RemoteURLyBirdServices{
    private static Data database;
    private static File dbFile;
    private static long portNumber;
    
    private URLyBirdServerFrame serverGUI;
    
    private boolean serverStatus = false;
    
    /**
     * This Constructor initialise the Server Service implementation in respect to the File instance of the database in use and the 
     * port number the Remote Server would reside for remote access by clients on the network
     * @param db a File value of the database file
     * @param port long value of the port number which this service will be made available
     * @throws RemoteException if an exception occurs at initialising all necessary remote components to be used by this service
     */
    public URLyBirdServerImpl(File db, long port) throws RemoteException{
        dbFile = db;
        portNumber = port;
    }
    
    /**
     * This function initialise the server User Interface by displaying the user Graphical User Interface of the Server
     */
    public void initializeServerUI(){
        serverGUI = new URLyBirdServerFrame(this); //create a new instance of the URLBirdServer Frame to show the Server GUI with controls and pass
        //this implentation instance to its constructor 
        serverGUI.setVisible(true);
    }

    /**
     * If the need arises to get the Database file Object .. this function returns the database file object property
     * @return - <code>File</code>the value of the Database file
     */
    public File getDatabaseFile(){
        return dbFile;
    }
    
    /**
     * This gets the current port number of the URLyBirdServer
     * @return - <code>Long</code> value of the Server port number
     */
    public long getPortNumber(){
        return portNumber;
    }
    
    public int createRoom(String []data) throws DuplicateKeyException, RemoteException{
        return database.create(data);
    }
    
    public void bookRoom(int recNo, long ownerId) throws RecordNotFoundException, SecurityException, 
            InvalidFieldLengthException, RoomAlreadyBookedException, RemoteException{
        //URLyBirdHotel temp = database.getData().get(recNo);//store the URLyBirdHotel Object in a temporary variable
        if(database.getData().get(recNo).getOwnerId() != 0){
            //means room already booked
            throw new RoomAlreadyBookedException("Room was Previously Booked by this Thread or Another");
        }
        database.getData().get(recNo).setOwnerId(ownerId); //modify the instance of that variable, setting its owner id to what the user provided
        database.update(recNo, database.getData().get(recNo).toStringArray(), database.getLockCookieValueUsed(recNo));//update the database data with the toStingArray value of the
        
        //modified URLyBirdHotel object
    }
    
    public void deleteRoom(int recNo) throws RecordNotFoundException, SecurityException, RemoteException{
        database.delete(recNo, database.getLockCookieValueUsed(recNo));
    }
    
    public Map<Integer, URLyBirdHotel> searchRecords(String []creteria) throws RecordNotFoundException, RemoteException{
        Map<Integer, URLyBirdHotel> result = new HashMap<Integer, URLyBirdHotel>();
        int []recNos = database.find(creteria);
        for(int r : recNos){
            String []val = database.read(r);
            try{
                result.put(r, new URLyBirdHotel(val));
            }
            catch(InvalidRecordException iR){
                throw new RecordNotFoundException(iR.getMessage());
            }
            catch(InvalidFieldLengthException iF){
                throw new RecordNotFoundException(iF.getMessage());
            }
        }
        return result;
    }

    /**
     * This function is called to backup (Store the data cookie stored in the Map into the database file) in respect of values the Map contains
     * @throws URLyBirdException when an unexpected error occurs when writing the data value back to the database file
     */
    public void backupData() throws URLyBirdException{
        if(serverStatus){
            database.commitData();
        }
        else{
            //dnt do any backup coz server wasnt started and definately no changes wa accounted on the database file
        }
    }
    
    /**
     * The function call Starts the RMI and making the remote function to be available for call by remote Objects
     * @throws URLyBirdServerException - if an exception occur when starting the server. URLyBirdServerException is thrown
     */
    public void startServer() throws URLyBirdServerException{
        try{
            Registry rmiRegistry = LocateRegistry.createRegistry((int)portNumber);
            rmiRegistry.rebind("rmi:///URLyBird", this);
            Naming.rebind("rmi:///URLyBird", this);
            database = new Data(dbFile); //initialize the object that manages the database so far the server was started successfully.
            serverStatus = true;//sets the server status to true because server was successfully started
        }
        catch(RemoteException rE){           
            throw new URLyBirdServerException(rE.getMessage());
        }
        catch(MalformedURLException mE){
            throw new URLyBirdServerException(mE.getMessage());
        }
    }
}
