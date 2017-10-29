/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;
import java.io.*;
import java.util.*;
import suncertify.db.data.URLyBirdHotel;
import suncertify.db.data.URLyBirdDataTools;

/**
 * This Class Manages the Database, implementing new functions in the <code>URLyBirdDataManager</code> interface for data access flexibility
 * and extensibility to Manage the database records
 * @author Segun
 */
public class DatabaseManager extends DBDataModel implements URLyBirdDataManager{
    
    //member data are to have protected access modifiers since its gonna be accessed by the required class 'Data'
    protected static final int DELETED = 1;
    
    protected static final Map<Integer, URLyBirdHotel> data = new HashMap<Integer, URLyBirdHotel>(); //Integer Key is an id(primary key);
    //the value of each URLyBirdHotel
    protected static final Map<String, Integer> dataMaxLength = new HashMap<String, Integer>();// String Key represents the Field Name and Integer is the
    //maximum lenght limit
    
    /**
     * Construct this class with a String value of the database file location / path
     * @param fPath String value of the database file path
     */
    public DatabaseManager(String fPath){
        super(fPath);
        initialize();//by initializing database.. reading all records
        loadDatabaseManager();
    }
    
    /**
     * Construct this class with a <code>File</code> instance of the database file
     * @param file the <code>File</code> instance of the database file
     */
    public DatabaseManager(File file){
        super(file);
        initialize(); //by initializing database.. reading all records
        loadDatabaseManager();
    }
    
    private synchronized void loadDatabaseManager(){
        while(!isDataModelInitialized()){
            try{
                wait(); //while the data model class initialization of data from the database isnt done
                //then call a wait to this Object
            }
            catch(InterruptedException iE){
                System.err.println(iE.getMessage());
            }
        }
        
        //load the data maximum length limit map with the correspnding value read from the database
        
        for(int i = 0; i < this.getNumberOfFields(); i++){
            dataMaxLength.put(getFieldsName()[i], getFieldsLength()[i]);
        }
        //then load the data map with the value from the database record
        int rowCount = 1;        
        for(String []field : getDatabaseRecords()){
            try{
                URLyBirdHotel temp = new URLyBirdHotel(dataMaxLength, field[0], field[1], Integer.parseInt(field[2].trim()), 
                        field[3].charAt(0), URLyBirdDataTools.getAmountDoubleValue(field[4]), 
                        field[5], URLyBirdDataTools.getClientId(field[6]), Integer.parseInt(field[7]));
                data.put(rowCount, temp);//assign the primary key to each URLyBirdHotel Object
                rowCount++; //increament the rowCount which key serves as the data primary key in the data management
            }
            catch(InvalidFieldLengthException iE){
                System.err.println("Database currupted due to : " + iE.getMessage());
            }
        }
    }

    private List<URLyBirdHotel> sortDatabaseRecordToArrayList(Map<Integer, URLyBirdHotel> oldRec){
        List<URLyBirdHotel> newRec = new ArrayList<URLyBirdHotel>();
        Vector<Integer> keysVector = new Vector<Integer>();
        Object []keys = oldRec.keySet().toArray();//get a Set representation of the Map keys and turn to array of object
        Arrays.sort(keys);//sort the array
        for(Object i : keys){
            keysVector.addElement(Integer.parseInt(i.toString()));//populate the arranged Keys into the key vector variable
        }
        for(int k : keysVector){
            newRec.add(oldRec.get(k));
        }
        return newRec;
    }
    
    public void commitData() throws URLyBirdException{
        List <String []> d = new ArrayList<String[]>();
        Object []keys = data.keySet().toArray();//get a Set representation of the Map keys and turn to array of object
        Arrays.sort(keys);//sort the array
        for(Object i : keys){
            d.add(data.get(Integer.parseInt(i.toString())).toStringArray());
        }
        this.writeToDatabaseFile(d);
    }
    
    /**
     * This function looks into all the record in the database using the argument provided to query the unique key
     * values of each URLyBirdHotel Object record in the database
     * @param uniqueKeys Array of String length 2 that represents the unique keys i.e Hotel Name and Location
     * @return true if record is duplicate or false if no duplicate found occurrence found
     */
    public boolean isRecordADuplicate(String []uniqueKeys){
        boolean duplicate = false;
        Iterator<URLyBirdHotel> iterate = data.values().iterator();
        while(iterate.hasNext()){
            URLyBirdHotel temp = iterate.next();
            String []k = temp.getUniqueKeys();
            if(k[0].equals(uniqueKeys[0]) && k[1].equals(uniqueKeys[1])){
                if(temp.getFlag() != DELETED){
                    duplicate = true;
                }
                else{
                    duplicate = false;
                }
            }
        }
        return duplicate;
    }
    
    /**
     * This function loops/look into the database records and check flag of each record to know the flag value as required to
     * (possibly reusing a deleted entry) else a new record is added to the database record . i.e next row
     * @return the <code>Integer</code> value of the recNo/row Number of the found space to slot a new record into the application
     */
    public int getEmptyRowNumber(){
        int recNo = 0;
        int row = 1;
        Iterator<URLyBirdHotel> iterate = sortDatabaseRecordToArrayList(data).iterator();
        while(iterate.hasNext()){
            int flag = iterate.next().getFlag();
            if(flag == DELETED && row <= data.size()){
                recNo = row;
                break; //break out of the loop since a deleted record was found before the end of the data
            }
            else{
                recNo = data.size() + 1;
            }
            row++;
        }
        return recNo;
    }
    
    public int[] findMatchByHotelName(String criteria){
        Vector<Integer> keys = new Vector<Integer>();//create a vector to add elements of the matched id (row Number)   
        Object []k = data.keySet().toArray();
        Arrays.sort(k);     
        int loop = 1;//create a variable name loop to store / increament the scroll on each and every rocord of the Iterated List of data
        while(loop <= data.size()){ //
            if(!criteria.equals("") && 
                    data.get(Integer.parseInt(k[loop - 1].toString())).getName().toLowerCase().startsWith(criteria.toLowerCase().trim()) 
                    && data.get(Integer.parseInt(k[loop - 1].toString())).getFlag() != DELETED){
                //test for the Object's stored name getName()
                //and check if the current row Name of Hotel Starts with the argument given and its flag does nt flag to be DELETED
                keys.addElement(Integer.parseInt(k[loop - 1].toString()));//add the loop position to the Vector bcos it occurs that the Hotel Name of the current iterated Object(record)
                //starts with the string value passed into the function.
            }
            else if(criteria.equals("") && data.get(Integer.parseInt(k[loop - 1].toString())).getFlag() != DELETED){ //this checks if the criteria is an empty string which according to the 
                //requirements tell us it means all records in the database are been refered to and all records and its flag does nt flag to be DELETED
                keys.addElement(Integer.parseInt(k[loop - 1].toString())); //add the loop position to the Vector
            }
            loop++;//increament the loop as the while loop repeats the block of codes
        }
        int []recNos = new int[keys.size()];//since we know the number of records match that found, we initialize an array on integer with the keys Vector
        //size as the capacity of the integer array
        int loopRec = 0;//initialize a loop to track the index of the record numbers to store value in there respective index
        for(int i : keys){ //loop thru the keys integer values in the Vector
            recNos[loopRec] = i; //assign the values from the Vector into the array of Integer respectively
            loopRec++;//increament the loop variable which increases as the for loop enters the : for block of code
        }        
        return recNos; //return the array of integers thats holding the record numbers that matches the criteria argumented into the function
    }
    
    public int[] findMatchByHotelLocation(String criteria){
        Vector<Integer> keys = new Vector<Integer>();//create a vector to add elements of the matched id (row Number)   
        Object []k = data.keySet().toArray();
        Arrays.sort(k);     
        int loop = 1;//create a variable name loop to store / increament the scroll on each and every rocord of the Iterated List of data
        while(loop <= data.size()){ //
            if(!criteria.equals("") && 
                    data.get(Integer.parseInt(k[loop - 1].toString())).getLocation().toLowerCase().startsWith(criteria.toLowerCase().trim())  && 
                    data.get(Integer.parseInt(k[loop - 1].toString())).getFlag() != DELETED){
                //test for the Object's stored name 
                //getLocation() and check if the current row Location of Hotel Starts with the argument given and its flag does nt flag to be DELETED
                keys.addElement(loop);//add the loop position to the Vector bcos it occurs that the Hotel Location of the current iterated Object(record)
                //starts with the string value passed into the function.
            }
            else if(criteria.equals("")  && data.get(Integer.parseInt(k[loop - 1].toString())).getFlag() != DELETED){ //this checks if the criteria is an empty string which according to the 
                //requirements tell us it means all records in the database are been refered to and all records and its flag does nt flag to be DELETED
                keys.addElement(loop); //add the loop position to the Vector
            }
            //Thread.yield();
            loop++;//increament the loop as the while loop repeats the block of codes
        }
        int []recNos = new int[keys.size()];//since we know the number of records match that found, we initialize an array on integer with the keys Vector
        //size as the capacity of the integer array
        int loopRec = 0;//initialize a loop to track the index of the record numbers to store value in there respective index
        for(int i : keys){ //loop thru the keys integer values in the Vector
            recNos[loopRec] = i; //assign the values from the Vector into the array of Integer respectively
            loopRec++;//increament the loop variable which increases as the for loop enters the : for block of code
        }        
        return recNos; //return the array of integers thats holding the record numbers that matches the criteria argumented into the function        
    }
    
    public long getLockCookieValueUsed(int recNo) throws RecordNotFoundException{
        if(recNo > data.size() || recNo < 1){
            throw new RecordNotFoundException("The Record Number " + recNo + " provided does not Exsit in the database record");
            //handles the exception if the recoNo provided is less than 1 or is more than total record in the database
        }
        for(int i = 0; i < data.size(); i++){
            if(i == recNo && data.get(i).getFlag() == DELETED){ //check if the current index (i) is same as the record number request from
                //the method argument.. if True, check if the current record Flog value is same as the DETELED contant value. if true
                //throw the RecordNotFoundException with expression msg that record appeared to be deleted
                throw new RecordNotFoundException("The Record Number " + recNo + " you are tryin to access its LOCK COOKIE appear to be DELETED");
            }
        }
        return data.get(recNo).getLockCookie(); //get the Long value of the lock cooking value of the Object (record) using the recNo Key..
    }
    
    /**
     * This function retrieves the data instance Map value of the database
     * @return Map instance of the database record 
     */
    public Map<Integer, URLyBirdHotel> getData(){
        return data;
    }
}
