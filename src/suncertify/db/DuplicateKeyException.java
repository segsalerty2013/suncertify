/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;

/**
 * This handles Exception when the a Newly added record have same Hotel Name and Location which is flagged ACTIVE in the 
 * database record.
 * @author Segun
 */
public class DuplicateKeyException extends URLyBirdException{
    
    /**
     * 
     * an empty argument when :
     * New record that want to added already exist in the database
     */
    public DuplicateKeyException(){
        super();
    }
    
    /**
     * 
     * @param exDesc <code>String</code> - exDesc - Describing the Exception Message when :
     * New record that want to added already exist in the database
     */
    public DuplicateKeyException(String exDesc){
        super(exDesc);
    }
    
}
