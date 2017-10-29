/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;

/**
 * This handles Exception that occurs when the new value length thats about to inserted into a database field is not more than
 * the required length thats in the database schema
 * @author Segun
 */
public class InvalidFieldLengthException extends URLyBirdException{
    
    /**
     * This Exception catches exception thrown when :
     * The length of data provided is more than maximum length required by the Object property in URLyBirdHotel (record)
     */
    public InvalidFieldLengthException(){
        super();
    }
  
    /**
     * This Exception catches exception thrown when :
     * The length of data provided is more than maximum length required by the Object property in URLyBirdHotel (record)
     * @param exDesc - Exception is handled with String value of exDesc as the error message value
     */
    public InvalidFieldLengthException(String exDesc){
        super(exDesc);
    }
    
}
