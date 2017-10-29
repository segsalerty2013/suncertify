/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

/**
 * Handles Exceptions that Might Occur at the server side of the program
 * @author Segun
 */

public class URLyBirdServerException extends suncertify.exception.URLyBirdException{
    
    /**
     * Empty constructor with the default exception description
     */
    public URLyBirdServerException(){
        super();
    }
    
    /**
     * Constructor with a custom description
     * @param exDesc String value of the exception message
     */
    public URLyBirdServerException(String exDesc){
        super(exDesc);
    }
    
}
