/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * This Object is the model for the database file record, it interacts with the Database file directly.
 * It manages the reading(initialisation) of records in the database file and the writing back of the 
 * record to the designated file in the proper required format
 * @author Segun
 */
public class DBDataModel {
    
    private static final int RECORD_FLAG_BYTES = 1; //EOF flag for each record byte value
    
    private static final String ENCODING = "US-ASCII"; //the specified encoding to be used
    
    private int magicCookie;
    private int totalOverallLength;
    
    private File databaseFile; //file instance of the database file given as db-1x1.db
    private int numberOfFields; //the variable to store the number of fields in the db structure
    private String []fieldNames; //array of String that holds the name of the db fields
    private int []fieldsLength; //the array of int to store each field's maximum length value
    private short []nameLengthRecord;
    private final List<String[]> dataList = new Vector<String[]>();
    
    private boolean initializationStatus = false;
    
    /**
     * This constructs the database model object with a <code>String</code> value of the database file path
     * @param fPath <code>String</code> - fPath - a String value of the db file and path 
     */
    public DBDataModel(String fPath){
        this.databaseFile = new File(fPath);
        //this.initilize();
    }
    
    /**
     * This constructs the database model object with a <code>File</code> instance
     * @param file <code>File</code> - file - a File instance representation of the db
     */
    public DBDataModel(File file){
        this.databaseFile = file;
        //this.initilize();
    }
    
    /**
     * This functions serves as the database file format reader and initialise all the database schema and records respectively
     * to be used at runtime by the application
     */
    public synchronized void initialize(){
        try{
            InputStream input = new FileInputStream(this.databaseFile); //create an imput stream for the db file input
            DataInputStream dataInput = new DataInputStream(input); //make a Data Input from the input stream from the db file
            magicCookie = dataInput.readInt();
            totalOverallLength = dataInput.readInt();
            //System.out.println("Magic Cookie is : " + magicCookie + " & Total Overall Length : " + totalOverallLength);
            
            int numberOfFieldsinRecord = dataInput.readShort();
            this.numberOfFields =  (1 + numberOfFieldsinRecord);
            this.fieldNames = new String[(1 + numberOfFieldsinRecord)];
            this.fieldsLength = new int[(1 + numberOfFieldsinRecord)]; //initalize an array of integer to hold each field's length respectively
            this.nameLengthRecord = new short[numberOfFieldsinRecord];
            
            for(int i = 0; i < numberOfFieldsinRecord; i++){
                short nameLength = dataInput.readShort();
                nameLengthRecord[i] = nameLength;
                byte[] fieldNameByteArray = new byte[nameLength];//determine the field name byte array size by the name length    
                dataInput.read(fieldNameByteArray);
                fieldNames[i] = new String(fieldNameByteArray, ENCODING);
                short fieldLength = dataInput.readShort();
                fieldsLength[i] = fieldLength;
            }
            
            this.fieldNames[numberOfFieldsinRecord] = "flag";
            this.fieldsLength[numberOfFieldsinRecord] = 1;
            
            byte []flagByteArray = new byte[RECORD_FLAG_BYTES]; //the flag byte array

            while(true){     
                int endOfFile = dataInput.read(flagByteArray);
               
                int flag = getValue(flagByteArray);
                if(endOfFile == -1){
                    break;
                }
                String []fields = new String[(1 + numberOfFieldsinRecord)];//each row fields value
                for (int i = 0; i < numberOfFieldsinRecord; i++) {  
                    byte []buffer = new byte[fieldsLength[i]];
                    dataInput.read(buffer);
                    fields[i] = "" + new String(buffer, ENCODING).trim();
                }
                fields[numberOfFieldsinRecord] = String.valueOf(flag);//to add to the flag field introduced
                dataList.add(fields);
            }

            input.close();
            dataInput.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Bad File format : " + e.getMessage() + "\nReconfigure Settings or Reconfigure/Replace DB File",
                    "ALERT !", JOptionPane.ERROR_MESSAGE);
            System.out.println("Bad File format : " + e.getMessage() + "\nReconfigure Settings or Reconfigure/Replace DB File");
        }
        finally{
            initializationStatus = true; //set the initialization status to true now that the block of codes has
            //completed by reaching the finally block.
            notifyAll(); //then notify all waiting threads about this change in the flag variable
        }
    }
   
    
    /**
     * This function writes the new data cache which contains all modified and new recored the application is currently holding
     * and perform a file writing task on the database file specified using the required Character encoding.
     * @param m - List that contains an array of String, which each array of string represents a row of data in the record
     * is not allowed to write into the file
     * @throws URLyBirdException If any exception occurs when trying to write the <code>List</code> of records back to the database
     * file
     */
    public synchronized void writeToDatabaseFile(List<String []> m) throws URLyBirdException{
        try{
            int dbNumberOfFields = numberOfFields - 1;
            
            OutputStream output = new FileOutputStream(databaseFile); //create an output stream for the db file input
            DataOutputStream dataOutput = new DataOutputStream(output);                 
            dataOutput.writeInt(magicCookie);
            dataOutput.writeInt(totalOverallLength);
            
            dataOutput.writeShort(dbNumberOfFields);
            
             
            for(int i = 0; i < dbNumberOfFields; i++){
                dataOutput.writeShort(nameLengthRecord[i]);
                byte[] buffer = getFormattedByteArray(fieldNames[i], nameLengthRecord[i]);
                byte[] fieldsByteArray = new byte[nameLengthRecord[i]];
                System.arraycopy(buffer, 0, fieldsByteArray, 0, nameLengthRecord[i]);               
                dataOutput.write(fieldsByteArray);
                dataOutput.writeShort(fieldsLength[i]);
            }
            byte []flagByteArray = new byte[RECORD_FLAG_BYTES]; //the flag byte array
            Iterator<String []> iterate = m.iterator();
            while(iterate.hasNext()){
                String []rowData = iterate.next();
                flagByteArray = intToByteArray(Integer.parseInt(rowData[(rowData.length - 1)]));                   
                //System.arraycopy(flag, 0, flagByteArray, 0, 1);
                dataOutput.write(flagByteArray);//write the new data flag byte value
                for(int i = 0; i < dbNumberOfFields; i++){                    
                    //byte []column = rowData[i].getBytes(ENCODING);
                    byte []column = getFormattedByteArray(rowData[i].trim(), fieldsLength[i]);
                    byte []buffer = new byte[fieldsLength[i]];
                    System.arraycopy(column, 0, buffer, 0, fieldsLength[i]);
                    dataOutput.write(column);
                }
            }
            output.flush();
            dataOutput.close();
            output.close();     
        }
        catch(IOException fE){
            throw new URLyBirdException("Error trying to write data to Database file " + fE.getMessage());
        }
    }
    
    private static int getValue(byte[] byteArray){  
        int value = 0;  
        int byteArrayLength = byteArray.length;  
      
        for(int i = 0; i < byteArrayLength; i++){  
            int shift = (byteArrayLength - 1 - i) * 8;  
            value += (byteArray[i] & 0x000000FF) << shift;  
        }
        return value;  
    }
    
    private static byte[] intToByteArray(int value) {
        byte[] b = new byte[RECORD_FLAG_BYTES];
        for (int i = 0; i < RECORD_FLAG_BYTES; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
   
    /**
     * This function gets the number of field in the database structure
     * @return <code>Integer</code> - the number of fields value in the db structure
     */
    public int getNumberOfFields(){
        return this.numberOfFields;
    }
    
    /**
     * This function gets the database field names in form of an array of string.
     * @return <code>String[]</code> array of String to holding the database fields name
     */
    public String[] getFieldsName(){
        return this.fieldNames;
    }
    
    /**
     * This functions stores the maximum character length of each field of data into an array of integers in the database
     * @return <code>Integer[]</code> array of integers holding the field length/limit of
     * the fields in the database.... it corresponds to what the column can hold in the database.
     * i.e field name location = 64 e.t.c
     */
    public int[] getFieldsLength(){
        return this.fieldsLength;
    }

    /**
     * This function stores all records in the database into a List containing an array of String, each String array holds a row
     * in the database
     * @return <code>List<String []></code> - that contains all data in the database 
     */
    public List<String []> getDatabaseRecords(){
        return dataList;
    }
    
    
    /**
     * This function returns the counted data rows in the database
     * it returns the Collection of record <code>size()</code>h
     * @return <code>Integer</code> - value of rows count in the database
     * both valid and invalid data are counted form the database record 
     */
    public int getDataRowsCount(){
        return dataList.size();
    }
    
    /**
     * This function returns true if this Object has finished all necessary reading of data in the database file or returns false
     * if this object is still busy with necessary data reading and population from the function <code>initialize()</code>
     * @return - the status of the data initialisation process of this Object
     */
    public boolean isDataModelInitialized(){
        return initializationStatus;
    }
    
    private byte[] getFormattedByteArray(String s, int len){
        byte []val = new byte[len];
        char []ch = new char[len];
        for(int i = 0; i < len; i++){
            try{
                ch[i] = s.toCharArray()[i];
            }
            catch(Exception aE){
                ch[i] = ' ';
            }
        }
        String st = "";
        for(char c: ch){
            st+= c;
        }
        try{
            val = st.getBytes(ENCODING);
        }
        catch(UnsupportedEncodingException uE){
            System.err.println(uE.getMessage());
        }
        return val;
    }
}
