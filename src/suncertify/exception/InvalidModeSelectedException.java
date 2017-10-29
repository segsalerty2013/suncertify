/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.exception;

/**
 * This handles Exception when an Invalid Mode is Selected when trying to run the program from the command line with argument(s)
 * other than the single mode flag or the one that this application support
 * @author Segun
 */
public class InvalidModeSelectedException extends suncertify.exception.URLyBirdException{
    
    /**
     * A Zero argument constructor for the InvalidModeSelectedException
     */
    public InvalidModeSelectedException(){
        super();
    }
    
    /**
     * @param exDesc String Describing the Exception Message  
     */
    public InvalidModeSelectedException(String exDesc){
        super(exDesc);
    }
}
