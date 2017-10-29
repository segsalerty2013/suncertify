/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.exception;

/**
 * This extends Exception class so that other exceptions
 * in this project
 * can extend this and be its sub class
 * @author Segun
 */
public class URLyBirdException extends Exception{
    
    /**
     * 
     * A Zero argument constructor for the URLyBirdException
     */
    public URLyBirdException(){
        super();
    }
    
    /**
     * 
     * @param exDesc String - exDesc - Describing the Exception Message  
     */
    public URLyBirdException(String exDesc){
        super(exDesc);       
    }
    
}
