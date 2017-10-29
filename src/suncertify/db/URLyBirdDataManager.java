/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;

/**
 * This interface is designed to have added function required to manage the database smoothly.
 * @author Segun
 */
public interface URLyBirdDataManager{
    /**
     * Saving the data held by the data cache ..i.e Map<> in the DatabaseManager back to the file/database
     * @throws URLyBirdException - if any form of error occurs when saving data to the file/database
     */
    public void commitData() throws URLyBirdException;
    
    /**
     * Breaking down the match using the unique keys choices. this finds match by Hotel Name
     * @param criteria - String value of hotel name criteria to match
     * @return - an array of record numbers that matches the given criteria
     */
    public int[] findMatchByHotelName(String criteria);
    
    /**
     * Breaking down the match using the unique keys choices. this finds match by Hotel Location
     * @param criteria - String value of hotel location criteria to match
     * @return - an array of record numbers that matches the given criteria 
     */
    public int[] findMatchByHotelLocation(String criteria);
    
    /**
     * Reads the lock cookie of the record in the database
     * @param recNo - Integer value of record number to get its lock cookie value
     * @return - long value of the lock cookie of the data row in the database
     */
    public long getLockCookieValueUsed(int recNo) throws RecordNotFoundException;

}
