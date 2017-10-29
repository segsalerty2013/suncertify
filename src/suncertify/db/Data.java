/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import java.io.*;
import java.util.*;
import suncertify.db.data.URLyBirdHotel;
import suncertify.db.data.URLyBirdDataTools;

/**
 * This is the Data file required to manage/implement DB interface of the specification given which handles the various manipulation 
 * of the database record
 * @author Segun
 */
public class Data extends DatabaseManager implements DB{
    
    /**
     * This Constructor initialises the data class and loads all required database manager using the designed database Model
     * @param fPath - a String value of the path where the database file is located
     */
    public Data(String fPath){
        super(fPath);
    }
    
    /**
     * This Constructor initialises the data class and loads all required database manager using the designed database Model
     * @param file a File value of the database file
     */
    public Data(File file){
       super(file);
    }   

    public String[] read(int recNo) throws RecordNotFoundException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }        
        else if(data.get(recNo).getFlag() == DELETED){
            throw new RecordNotFoundException("The Record Number " + recNo + " you are tryin to read appear to be DELETED");
        }
        //if no exception was thrown at fnction body... then, call get the value of the data Map using the recNo as the key and call on the Object (record)
        //toStringArray() function that returns an array of String which each index represent field of data respectively.
        return data.get(recNo).toStringArray();
    }
    
    public synchronized void update(int recNo, String[] d, long lockCookie) throws RecordNotFoundException, SecurityException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }
        else if(data.get(recNo).getFlag() == DELETED){
            throw new RecordNotFoundException("The Record Number  " + recNo + " you are tryin to update appear to be DELETED");
        }
        //if no RecordNotFoundException is thrown / occur.. then go ahead to check for a SecurityException on the record that this method want to update
        //with the record number passed in
        //URLyBirdHotel tempURLyBird = data.get(recNo); //initialize a variable to reference to the Object in the data map at the key VALUE as recNo
        //System.out.println("I got the record number : " +recNo+ " : " + tempURLyBird.getName());
        if(data.get(recNo).isLocked() && data.get(recNo).getLockCookie() != lockCookie){
            //if the Object (URLyBirdHotel) is currently locked and the lockCookie of the Object is not what is passed into this function
            //requirements tell us to throw a SecurityException 
            throw new SecurityException("Thread cannot access this record because this Object is currently locked by cookie " + 
                    data.get(recNo).getLockCookie() + " While you are trying to access it with cookie " + lockCookie);
        }
        //if no SecurityException was thrown then go ahead to update the dataMap that contains the record by re-setting the URLyBirdHotel object
        long dataLockCookie = lock(recNo); //lock this record Number and store the value returned by the lock function in a variable so as to use it
        //to unlock the data record as soon as update is performed on the record
        try{
            data.get(recNo).updateURLyBirdHotel(d); //then update the data with the array of string argument passed into the function
        }
        catch(InvalidRecordException iE){
            throw new RecordNotFoundException(iE.getMessage());
        }
        catch(InvalidFieldLengthException fE){
            throw new RecordNotFoundException(fE.getMessage());
        }
        finally{
            unlock(recNo, dataLockCookie);//as soon as the Object is updated even if an InvalidRecordException occurs
            //the Record must be unlocked using the data lock cookie that was returned by the function lock() so as to enable
            //access by another Thread to this data at the row number passed int this function.
        }
    }
    
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }
        else if(data.get(recNo).getFlag() == DELETED){
            throw new RecordNotFoundException("The Record Number " + recNo + " you are tryin to read appear to be DELETED");
        }
        //if no RecordNotFoundException is thrown / occur.. then go ahead to check for a SecurityException on the record that this method want to delete
        //with the record number passed in
        if(data.get(recNo).isLocked() && data.get(recNo).getLockCookie() != lockCookie){
            //if the Object (URLyBirdHotel) is currently locked and the lockCookie of the Object is not what is passed into this function
            //requirements tell us to throw a SecurityException 
            throw new SecurityException("Thread cannot access this record because this Object is currently locked by cookie " + 
                    data.get(recNo).getLockCookie() + " While you are trying to access it with cookie " + lockCookie);
        }
        //if no SecurityException was thrown then go ahead to delete the data (Object) at record Number that was passed into this function
        long dataLockCookie = lock(recNo); //lock this record Number and store the value returned by the lock function in a variable so as to use it
        //to unlock the data record as soon as delete task is completed on the record
        data.get(recNo).setFlag(DELETED); //perform the delete task by setting the Flag value if this record to contstant value DELETED = 1
        unlock(recNo, dataLockCookie);//as soon as the Object is successfully deleted or not.... but
        //the Record must be unlocked using the data lock cookie that was returned by the function lock() so as to enable
        //access by another Thread to this data at the row number passed int this function.
    }
    
    public int[] find(String[] criteria){       
        int []hotelNameMatch = findMatchByHotelName(criteria[0]);//use the function to get integer array of records that matches the first element
        //of the array of string (Name of Hotel Keyword ) thats passed into the function
        int []hotelLocationMatch = findMatchByHotelLocation(criteria[1]);//use the function to get integer array of records that matches the 
        //second element of the array of string (Name of Hotel Keyword ) thats passed into the function

        Set<Integer> recordNumbers = new TreeSet<Integer>(); //initialize a TreeSet so that record can be sorted and avoid duplication of result recNo
        for(int n : hotelNameMatch){
            recordNumbers.add(n);//populate the record number collection of hotel names that matched the search using the SET to avoid duplicate values
             //System.out.println("Hotel Name Matches are : " + n + " and size is : " + hotelNameMatch.length);
        }
        for(int l : hotelLocationMatch){
            recordNumbers.add(l);//populate the record number collection of hotel location that matched the search using the SET to avoid duplicate values
            //System.out.println("Hotel Location Matches are : " + l + " and size is : " + hotelLocationMatch.length);
        }
        //int []o = Collections.fill(, this)
        int []recNos = new int[recordNumbers.size()];
        int loop = 0;//initialize a loop to track all the index of the record numbers to store value in there respective index
        for(int i : recordNumbers){ //loop through the integer values in the set
            recNos[loop] = i; //assign the values from the Set into the array of Integer respectively
            loop++; //increament the loop variable which increases as the for loop enters the : for block of code
        }        
        return recNos;//return the sorted set of record numbers found as match from the whole database
    }
    
    public int create(String[] d) throws DuplicateKeyException{
        int position = 1;
        //i go use Iterator when i get to new horizons
        if(data.isEmpty()){
            //dont waste time, the record is empty already ... just put a new value to the map loop at 1;
            try{
                data.put(position, new URLyBirdHotel(dataMaxLength, d[0], d[1], Integer.parseInt(d[2].trim()), d[3].charAt(0), 
                    URLyBirdDataTools.getAmountDoubleValue(d[4]), d[5]));
            }
            catch(InvalidFieldLengthException iE){
                //thow the InvalidFieldLengthException to DuplicateKeyException to handle it
                throw new DuplicateKeyException(iE.getMessage());
            }
        }
        else{
            //if data is not empty... first check for duplicate occurence first
            String []newRecordUniqueKeys = new String[2];
            newRecordUniqueKeys[0] = d[0];
            newRecordUniqueKeys[1] = d[1];
            if(this.isRecordADuplicate(newRecordUniqueKeys)){
                    throw new DuplicateKeyException("The new data you are trying to add to the record already exist i.e "
                        + "Same Hotel Name and Location exist in the record");                
            }
            else{
                //since the new record is not a duplicate .. then, try to check thru the data for a deleted record to replace new 
                //record in its row or else add a new row to the database which is handled by the super function getEmptyRowNumber()
                //to retrieve the empty row number space
                try{
                    position = this.getEmptyRowNumber();
                    data.put(position, new URLyBirdHotel(dataMaxLength, d[0], d[1], Integer.parseInt(d[2].trim()), d[3].charAt(0),
                        URLyBirdDataTools.getAmountDoubleValue(d[4]), d[5]));
                }
                catch(InvalidFieldLengthException iE){
                    //thow the InvalidFieldLengthException to DuplicateKeyException to handle it
                    throw new DuplicateKeyException(iE.getMessage());
                }                
            }
        }    
        return position;//return the record number of the new data created by adding 1 to its index in the record Collection
    }
    
    public synchronized long lock(int recNo) throws RecordNotFoundException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }
        else if(data.get(recNo).getFlag() == DELETED){
            throw new RecordNotFoundException("The Record Number " + recNo + " you are tryin to lock appear to be DELETED");
        }
        //if no RecordNotFoundException is thrown / occur.. then, we check if the Object with record number is currently locked/in use by another thread
        while(data.get(recNo).isLocked()){
            try{
                wait();
                //while this Object/record is locked call a wait() on the object 
                //while the current thread gives up the CPU and consumes no CPU cycles until the record is unlocked
            }
            catch(InterruptedException iE){
                System.err.println(iE.getMessage());
            }
        }
        //as soon as this thread is notified that the record is no more locked. it then try to lock the record
        long lockCookie = URLyBirdDataTools.generateLockCookie();
        data.get(recNo).setLockCookie(lockCookie);//then set a new lockCookie value on this Object (data)
        //at the row number that was specified in the argument
        data.get(recNo).setLockStatus(true); //set the lock status to true. meaning this data (Object) in the record is locked
        notifyAll(); //notify all waiting threads who are waiting for this locked data
        return lockCookie;
    }
    
    public void unlock(int recNo, long cookie) throws RecordNotFoundException, SecurityException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }
        //if no RecordNotFoundException is thrown / occur.. then go ahead to check for a SecurityException on the record that this method want to unlock
        //with the record number passed in
        if(data.get(recNo).isLocked() && data.get(recNo).getLockCookie() != cookie){
            //if the Object (URLyBirdHotel) is currently locked and the lockCookie of the Object is not what is passed into this function
            //requirements tell us to throw a SecurityException 
            throw new SecurityException("Thread cannot access this record because this Object is currently locked by cookie " + 
                    data.get(recNo).getLockCookie() + " While you are trying to access it with cookie " + cookie);
        }
        //if no SecurityException was thrown then go ahead to unlock the data
        data.get(recNo).setLockStatus(false); //set the lock status of the Object to false to show that its unlocked        
    }
}
