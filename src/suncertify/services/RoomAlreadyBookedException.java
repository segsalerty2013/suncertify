/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import suncertify.exception.URLyBirdException;

/**
 * Handles Exception when trying to book a room thats already been booked
 * @author Segun
 */
public class RoomAlreadyBookedException extends URLyBirdException{
    
    /**
     * Empty constructor with the default exception description
     */
    public RoomAlreadyBookedException(){
        super();
    }
    
    /**
     * Constructor with a custom description
     * @param exDesc String value of the exception message
     */
    public RoomAlreadyBookedException(String exDesc){
        super(exDesc);
    }
}
