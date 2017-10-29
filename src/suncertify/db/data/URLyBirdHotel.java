/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db.data;
import suncertify.db.InvalidRecordException;
import suncertify.db.InvalidFieldLengthException;
import java.util.Map;
import java.io.Serializable;

/**
 * This is an Object that represents a URLyBirdObject and its <code>Serializable</code> if used in a networked environment. Its hold data member and function
 * necessary in make up of each Hotel record in the database.
 * @author Segun
 */
public class URLyBirdHotel implements Serializable{
    
    private Map<String, Integer> fieldsLimit;//fields limit property can only be set at object initialization
    
    private String name;
    private String location;
    private int size;
    private char smoking;
    private double rate;
    private String dateAvailable;
    private long ownerId;
    private int flag = 0;
    
    private long lockCookie = 0;
    private boolean lockStatus;
    
    /**
     * This Constructor set values of some of the class member thats needed when a new 
     * hotel is to be added to the database for booking. The required values to be stored in
     * the database are passed into the constructor argument
     * @param limit - a Map collection holding the integer value of all respective field name
     * @param name - the Hotel name
     * @param loc - the City which the Hotel resides
     * @param s - the Maximum occupation of the room. as in size of occupants it can admit
     * @param sm - character 'Y' for yes if the room allows smoking or 'N' if smoking not allowed
     * @param rate - a double value of the price per night of the hotel
     * @param date - a Date value of the room availability in format is <code>yyyy/mm/dd</code>
     * @throws InvalidFieldLengthException - If the argument values respective length exceeds the required length
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public URLyBirdHotel(Map<String, Integer> limit, String name, String loc, int s, char sm, double rate, String date) throws InvalidFieldLengthException{
        this.fieldsLimit = limit;
        this.setName(name);
        this.setLocation(loc);
        this.setSize(s);
        this.setSmoking(sm);
        this.setRate(rate);
        this.setDateAvailable(date);
    }
    
    /**
     * This constructor set values of some of the class members thats needed when reading values 
     * the data from the database which an instance of this class can be stored in a memory cache 
     * for this Object modification, control and all using the setter and getter. The required 
     * values to be stored in the database are passed into the constructor argument
     * <p>Note that : Instance of this Object assigns default values to the member data <code>lockCookie</code>
     * to 0 and the <code>lockStatus</code> to false (means the URLyBirdHotel object not locked).</p>
     * @param limit - a Map collection holding the integer value of all respective field name
     * @param name - the Hotel name
     * @param loc - the City which the Hotel resides
     * @param s - the Maximum occupation of the room. as in size of occupants it can admit
     * @param sm - character 'Y' for yes if the room allows smoking or 'N' if smoking not allowed
     * @param rate - a double value of the price per night of the hotel
     * @param date - a date value of the room availability in format is <code>yyyy/mm/dd</code>
     * @param owner - a long value of the customer id that booked this Hotel
     * @param flag - an Integer value that denotes the Hotel record status if Valid or Deleted
     * @throws InvalidFieldLengthException - If the argument values respective length exceeds the required length
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public URLyBirdHotel(Map<String, Integer> limit, String name, String loc, int s, char sm, double rate, String date, long owner, int flag) 
            throws InvalidFieldLengthException {
        this.fieldsLimit = limit;
        this.setName(name);
        this.setLocation(loc);
        this.setSize(s);
        this.setSmoking(sm);
        this.setRate(rate);
        this.setDateAvailable(date);
        this.ownerId = owner;
        this.setFlag(flag);
    }

    /**
     * This Constructor call sets the fields of this Object which represent a record. The value for field n appears in data[n]
     * @param data -  array of String that each index is a record value to the field name, location, size, smoking, rate, dateAvailable and owner id 
     * respectively
     * @throws InvalidRecordException - throws this exception if the length of the array of string in argument is not 6 which each index
     * modifies its respective member data
     * @throws InvalidFieldLengthException - If the argument values respective length exceeds the required length
     */
    public URLyBirdHotel(String []data) throws InvalidRecordException, InvalidFieldLengthException{
        if(data.length != 8){
            throw new InvalidRecordException("The URLyBirdHotel you want to construct is not valid i.e Check the array of String "
                    + "parsed into this function");
        }
        this.name = data[0];
        this.location = data[1];
        this.size = Integer.parseInt(data[2]);
        this.smoking = data[3].charAt(0);
        this.rate = URLyBirdDataTools.getAmountDoubleValue(data[4]);
        this.dateAvailable = data[5];
        this.ownerId = Long.parseLong(data[6]);
        this.flag = Integer.parseInt(data[7]);
    }
    
    /**
     * This function call Modifies the fields of this Object which represent a record. The new value for field n appears in data[n]
     * @param data -  array of String that each index is a record value to the field name, location, size, smoking, rate, dateAvailable respectively
     * @throws InvalidRecordException - throws this exception if the length of the array of string in argument is not 6 which each index
     * modifies its respective member data
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void updateURLyBirdHotel(String []data) throws InvalidRecordException, InvalidFieldLengthException{
        if(data.length != 8){
            throw new InvalidRecordException("The Record you want to update is not valid i.e Check the array of String parsed into this function");
        }
        this.setName(data[0]);
        this.setLocation(data[1]);
        this.setSize(Integer.parseInt(data[2]));
        this.setSmoking(data[3].charAt(0));
        this.setRate(URLyBirdDataTools.getAmountDoubleValue(data[4]));
        this.setDateAvailable(data[5]);
        this.setOwnerId(Long.parseLong(data[6]));
        this.setFlag(Integer.parseInt(data[7]));
    }
    
    /**
     * Sets the name for the Hotel
     * @param n - the Hotel name
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void setName(String n) throws InvalidFieldLengthException{
        if(n.length() > fieldsLimit.get("name")){
            throw new InvalidFieldLengthException("The new Hotel name exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("name") + " Expected");
        }
        this.name = n;
    }
    
    /**
     * Sets the Location for the Hotel
     * @param l - the City which the Hotel resides
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void setLocation(String l) throws InvalidFieldLengthException{
        if(l.length() > fieldsLimit.get("location")){
            throw new InvalidFieldLengthException("The new Hotel location exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("location") + " Expected");
        }        
        this.location = l;
    }
    
    /**
     * Sets the maximum occupants size of the room, maximum number of people the Hotel room can take is specified.
     * @param s - the Maximum occupation of the room. as in size of occupants it can admit
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void setSize(int s) throws InvalidFieldLengthException{
        if(String.valueOf(s).length() > fieldsLimit.get("size")){
            throw new InvalidFieldLengthException("The new Maximum Occupancy (Size) exceeds the Maximum Character length Limit of " + 
                    fieldsLimit.get("size") + " Expected");
        } 
        this.size = s;
    }
    
    /**
     * Set the smoking status of the Hotel room, a character value is passed into the function to set 'Y' if smoking room
     * or 'N' if non smoking is allowed.
     * @param s - character 'Y' for yes if the room allows smoking or 'N' if smoking not allowed
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void setSmoking(char s)  throws InvalidFieldLengthException{
        if(String.valueOf(s).length() > fieldsLimit.get("smoking")){
            throw new InvalidFieldLengthException("The new Smoking or Non-Smoking status exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("smoking") + " Expected");
        } 
        this.smoking = s;
    }
    
    /**
     * Set the amount rate per room to be booked.
     * @param r - a double value of the price per night of the hotel
     * @throws InvalidFieldLengthException - If the argument String value length exceeds the required length
     */
    public void setRate(double r) throws InvalidFieldLengthException{
        if(String.valueOf(r).length() > fieldsLimit.get("rate")){
            throw new InvalidFieldLengthException("The new Hotel rate exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("rate") + " Expected");
        }    
        this.rate = r;
    }
    
    /**
     * 
     * @param d - a Date value of the room availability in format is <code>yyyy/mm/dd</code> 
     */
    public void setDateAvailable(String d) throws InvalidFieldLengthException{
        if(d.length() > fieldsLimit.get("date")){
            throw new InvalidFieldLengthException("The new date availability of the Room exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("date") + " Expected");
        }
        this.dateAvailable = d;
    }
    
    /**
     * Sets the customer Id number which is an 8digits unique long value for every URLyBird customer
     * A booked hotel has its owner if set to this customer id provided
     * @param id - a long value (8 digits) that represent the customer id who booked this hotel
     * @throws InvalidFieldLengthException - If the argument long value is not length of 8
     */
    public void setOwnerId(long id) throws InvalidFieldLengthException{
        if(String.valueOf(id).length() != fieldsLimit.get("owner")){
            throw new InvalidFieldLengthException("The new Customer ID produced to book the Room is not same as length " 
                    + fieldsLimit.get("owner") + " Expected");
        }
        this.ownerId = id;
    }
    
    /**
     * Sets a new flag value for the Hotel Data... 0 if data is still valid and 1 if Deleted
     * @param f - an Integer value denoting the data status, 0 if valid and 1 if its deleted
     */
    public void setFlag(int f){// throws InvalidFieldLengthException{
        /*
        if(String.valueOf(f).length() > fieldsLimit.get("flag")){
            throw new InvalidFieldLengthException("The new data flag constant exceeds the Maximum Character length Limit of " 
                    + fieldsLimit.get("flag") + " Expected");
        }
         * 
         */
        this.flag = f;
    }
    
    /**
     * 
     * @param cookie - a long value storing the cookie value of lock on this object. 
     */
    public void setLockCookie(long cookie){
        this.lockCookie = cookie;
    }
    
    /**
     * 
     * @param status - a boolean value to set the lock status of the object. true to lock and false if to be unlocked
     */
    public void setLockStatus(boolean status){
        this.lockStatus = status;
    }
    
    /**
     * 
     * @return - the Hotel name
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * 
     * @return - the City which the Hotel resides
     */
    public String getLocation(){
        return this.location;
    }
    
    /**
     * 
     * @return - the Maximum occupation of the room. as in size of occupants it can admit
     */
    public int getSize(){
        return this.size;
    }
    
    /**
     * 
     * @return - character 'Y' for yes if the room allows smoking or 'N' if smoking not allowed
     */
    public char getSmoking(){
        return this.smoking;
    }
    
    /**
     * 
     * @return - a double value of the price per night of the hotel 
     */
    public double getRate(){
        return this.rate;
    }
    
    /**
     * 
     * @return - a Date value of the room availability in format is <code>yyyy/mm/dd</code>  
     */
    public String getDateAvailable(){
        return this.dateAvailable;
    }
    
    /**
     * 
     * @return - a long value (8 digits) that represent the customer id who booked this hotel 
     */
    public long getOwnerId(){
        return this.ownerId;
    }
    
    /**
     * 
     * @return - an Integer value denoting the data status, 0 if valid and 1 if its deleted  
     */
    public int getFlag(){
        return this.flag;
    }
    
    /**
     * 
     * @return - a long value storing the cookie value of lock on this object.  
     */
    public long getLockCookie(){
        return this.lockCookie;
    }
    
    /**
     * 
     * @return - true if the object is locked by another thread or false if it is not so that the current
     * thread might acquire the lock.  
     */
    public boolean isLocked(){
        return this.lockStatus;
    }    
    
    /**
     * Making choices of Unique key fields as the Hotel Name and Location. Same Hotel Name (branch) in same location
     * must not exist more than once in the database. So, this function gets the Unique key value of this object
     * @return - array of string, first element is the Hotel Name and second element is the Location
     */
    public String[] getUniqueKeys(){
        String []keys = new String[2]; //i made a choice of making two fields as the unique key for every data (URLyBirdHotel)
        keys[0] = this.name;
        keys[1] = this.location;
        return keys;
    }
    
    /**
     * Return an array of String which contains all field member value of this Object placed into each element of the array
     * @return  - an array of String value of all data member populated into index of an array
     */
    public String[] toStringArray(){
        String []values = new String[8];
        values[0] = this.name;
        values[1] = this.location;
        values[2] = String.valueOf(this.size);
        values[3] = String.valueOf(this.smoking);
        values[4] = URLyBirdDataTools.getAmountStringValue(this.rate);
        values[5] = this.dateAvailable;
        values[6] = String.valueOf(this.ownerId);
        values[7] = String.valueOf(this.flag);
        return values;
    }
    
    /**
     * Return an array of String which contains all field member value of this Object placed into each element of the array including the record number
     * good to be used to present record on a JTable 
     * @param recNo - the record number value of this URLBirdHotel object in the overall database. this is its primary key
     * @return - an array of String value of all data member populated into index of an array with its primary record number key
     */
    public String[] toStringArray(int recNo){
        String []values = new String[8];
        values[0] = String.valueOf(recNo);
        values[1] = this.name;
        values[2] = this.location;
        values[3] = String.valueOf(this.size);
        values[4] = String.valueOf(this.smoking);
        values[5] = URLyBirdDataTools.getAmountStringValue(this.rate);
        values[6] = this.dateAvailable;
        values[7] = String.valueOf(this.ownerId);
        //values[7] = String.valueOf(this.flag);
        return values;
    }
}
