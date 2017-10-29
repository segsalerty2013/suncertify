/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db;

import suncertify.exception.URLyBirdException;

/**
 * If new Record thats about to be added is incomplete i.e necessary fields required is left out empty
 * @author Segun
 */
public class InvalidRecordException extends URLyBirdException{

    /**
     * 
     * This Exception catches exception thrown when :
     * value provided isn't what's expected. i.e when array of length 3 expected AND an empty array was provided
     * 
     */
    public InvalidRecordException(){
        super();
    }
  
    /**
     * 
     * This Exception catches exception thrown when :
     * value provided isn't what's expected. i.e when array of length 3 expected AND an empty array was provided
     * @param exDesc - Exception is handled with String value of exDesc as the error message value
     * 
     */
    public InvalidRecordException(String exDesc){
        super(exDesc);
    }
}
