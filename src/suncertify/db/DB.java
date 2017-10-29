/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

/**
 * This interface as required has functions which is meant to be implemented by the Data class file
 * @author Segun
 */
public interface DB {
    /**
     * Reads a record from the file. Returns an array where each element is a record value.
     * @param recNo - position of the data to be read in the record/database
     * @return - an array where each element is a record value
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     */
    public String[] read(int recNo) throws RecordNotFoundException;
    
    /**
     * Modifies the fields of a record. The new value for field n appears in data[n]. Throws SecurityException 
     * if the record is locked with a cookie other than lockCookie.
     * @param recNo - position of the data to be updated in the record/database
     * @param data - New array of string value to be replaced with the old record
     * @param lockCookie - a long value to identify the thread thats holding a lock on the data at this recNo
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     * @throws SecurityException - if the record is locked with a cookie other than lockCookie passed into the parameter
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException;
    
    /**
     * Deletes a record, making the record number and associated disk storage available for reuse.
     * Throws SecurityException if the record is locked with a cookie other than lockCookie.
     * @param recNo - position of the data to be deleted from the record/database
     * @param lockCookie - a long value to identify the thread thats holding a lock on the data at this recNo
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     * @throws SecurityException - if the record is locked with a cookie other than lockCookie passed into the parameter
     */
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException;
    
    /**
     * Returns an array of record numbers that match the specified criteria. 
     * Field n in the database file is described by criteria[n]. A null value in criteria[n] matches any field value.
     * A non-null value in criteria[n] matches any field value that begins with criteria[n]. 
     * (For example, "Fred" matches "Fred" or "Freddy".)
     * @param criteria - array of string which each element denotes the value to match with the unique fields used in data matching 
     * @return - an array of record numbers that matches the given criteria
     */
    public int[] find(String[] criteria);
    
    /**
     * Creates a new record in the database (possibly reusing a deleted entry). 
     * Inserts the given data, and returns the record number of the new record.
     * @param data - array of string value to be recorded into the database possibly reusing a deleted entry flagged 1
     * @return - the record number of this new data thats added
     * @throws DuplicateKeyException - if data argument unique key exists in the database
     */
    public int create(String[] data) throws DuplicateKeyException;
    
    /**
     * Locks a record so that it can only be updated or deleted by this client. 
     * Returned value is a cookie that must be used when the record is unlocked, 
     * updated, or deleted. If the specified record is already locked by a different client, 
     * the current thread gives up the CPU and consumes no CPU cycles until the record is unlocked.
     * @param recNo - position of the data to be locked in the record/database
     * @return - a random long value that serves as the thread identification for locking the data in the record
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist
     */    
    public long lock(int recNo) throws RecordNotFoundException;
    
    /**
     * Releases the lock on a record. Cookie must be the cookie returned when the record was locked, otherwise throws SecurityException.
     * @param recNo - position of the data to be unlocked in the record/database
     * @param cookie - a long value this thread holds identifying it as the one that locked the data in the record
     * @throws RecordNotFoundException - if data was previously deleted in the record or doesn't exist 
     * @throws SecurityException - if the record is locked with a cookie other than lockCookie passed into the parameter
     */
    public void unlock(int recNo, long cookie) throws RecordNotFoundException, SecurityException;
    
}
