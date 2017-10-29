/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import suncertify.gui.*;
import suncertify.db.*;
import suncertify.db.data.URLyBirdHotel;
import suncertify.exception.URLyBirdException;
import java.util.*;
import java.io.File;

/**
 * Implementation of the Local / Alone Application coding
 * @author Segun
 */
public class URLyBirdLocalImpl implements LocalURLyBirdServices{
    
    private static Data database;
    
    private URLyBirdApplicationFrame application;
    
    /**
     * This Constructor accepts a string value of the database path to be used by the local application thats running alone 
     * in a non-networked environment
     * @param dbPath a String value of the path where the database file is located
     */
    public URLyBirdLocalImpl(String dbPath){
        database = new Data(dbPath);

    }
    
    /**
     * This Constructor accepts a File instance to be used by the local application thats running alone 
     * in a non-networked environment
     * @param db a File value of the database file
     */
    public URLyBirdLocalImpl(File db){
        database = new Data(db);
    }
    
    /**
     * This function initialise the Alone User Interface by displaying the user Graphical User Interface for running this application on 
     * the local machine (Alone Mode)
     */
    public void initializeAloneUI(){
        application = new URLyBirdAloneFrame(this);
        application.packPanel();
        application.setVisible(true);
    }
    
    public int createRoom(String []data) throws DuplicateKeyException{
        return database.create(data);
    }
    
    public void bookRoom(int recNo, long ownerId) throws RecordNotFoundException, SecurityException, 
            InvalidFieldLengthException, RoomAlreadyBookedException{
        //URLyBirdHotel temp = database.getData().get(recNo);//store the URLyBirdHotel Object in a temporary variable
        if(database.getData().get(recNo).getOwnerId() != 0){
            //means room already booked
            throw new RoomAlreadyBookedException("Room was Previously Booked by this Thread or Another");
        }
        database.getData().get(recNo).setOwnerId(ownerId); //modify the instance of that variable, setting its owner id to what the user provided
        database.update(recNo, database.getData().get(recNo).toStringArray(), database.getLockCookieValueUsed(recNo));//update the database data with the toStingArray value of the
        
        //modified URLyBirdHotel object
    }
    
    public void deleteRoom(int recNo) throws RecordNotFoundException, SecurityException{
        database.delete(recNo, database.getLockCookieValueUsed(recNo));
    }
    
    public Map<Integer, URLyBirdHotel> searchRecords(String []creteria) throws RecordNotFoundException{
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
   
    public void backupData() throws URLyBirdException{
        database.commitData();
    }
    
}
