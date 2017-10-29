/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import suncertify.exception.URLyBirdException;
import suncertify.db.*;
import suncertify.db.data.URLyBirdHotel;
import java.util.Map;

/**
 * The Local Interface which contains all URLyBird Services that can be accessed from a within the program when accessed
 * @author Segun
 */
public interface LocalURLyBirdServices extends URLyBirdServices{
    
    /**
     * Handles room to be created in the database
     * @param data - String array that contains the room data to be created which each element of the array represent the
     * database structure field as expected
     * @return - an Integer value of the row number of which new room data was created
     * @throws DuplicateKeyException - if data argument unique key exists in the database
     */
    public int createRoom(String []data) throws DuplicateKeyException;
    
    /**
     * Handles booking a room passing the unique record number to be booked
     * @param recNo - an Integer value of the record number which the client connected to the server request to be booked
     * @param ownerId - a long value of the client id who this room is gonna be booked to
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     * @throws SecurityException - if the record is locked with a cookie other than lockCookie passed into the parameter
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     * @throws RoomAlreadyBookedException - if the room was previously booked by this thread or another
     */
    public void bookRoom(int recNo, long ownerId) throws RecordNotFoundException, SecurityException, 
            InvalidFieldLengthException, RoomAlreadyBookedException;
    
    /**
     * This function deletes a room record from the database .. passing the unique record number to be deleted
     * @param recNo - an Integer value of the record number to be deleted
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     * @throws SecurityException - if the record is locked with a cookie other than lockCookie passed into the parameter
     */
    public void deleteRoom(int recNo) throws RecordNotFoundException, SecurityException;
    
    /**
     * Searched the record that matched the unique criteria passed into the function argument, URLyBirdHotel object of matched data are returned
     * with there respective row number as there key in a Map data collection
     * @param creteria - array of string with 2 sizes, first element matching the Hotel Name and second element matched the Location 
     * @return - Map object of matched URLyBirdHotel object value with there row number returned respectively as the map key
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     */
    public Map<Integer, URLyBirdHotel> searchRecords(String []creteria) throws RecordNotFoundException;  
    
    /**
     * This function is called to backup (Store the data cookie stored in the Map into the database file) in respect of values the Map contains
     * @throws URLyBirdException when an unexpected error occurs when writing the data value back to the database file
     */    
    public void backupData() throws URLyBirdException;
}
