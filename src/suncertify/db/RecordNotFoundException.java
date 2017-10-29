/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;

/**
 * When the record/row thats been looked for cannot be found in the database record
 * @author Segun
 */
public class RecordNotFoundException extends URLyBirdException{
    
    /**
     * 
     * an empty argument when :
     * A record doesn't exist in the database or previously deleted.
     * 
     */
    public RecordNotFoundException(){
        
    }
    
    /**
     * 
     * exDesc - Describing the Exception Message when :
     * A record doesn't exist in the database or previously deleted.
     */
    
    public RecordNotFoundException(String exDesc){
        super(exDesc);
    }
    
}
