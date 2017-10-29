/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db.data;

import java.text.*;
import java.util.*;
/**
 * This class contains static functions that has to do with Data Utilities and tools for Handling User Input data into
 * this application/database usable format.
 * @author Segun
 */
public class URLyBirdDataTools {
    
    /**
     * This converts argument String amount value format ($254 .00) into a double instance that can be used in the program for 
     * arithmetical use . ie $254.00 will trim off the ($) sign and return a double value of 254.00
     * @param a String value of the amount to convert
     * @return double value of the argument provided
     */
    public static double getAmountDoubleValue(String a){
        StringBuilder buff = new StringBuilder(a);
        buff.deleteCharAt(0);
        double d = Double.parseDouble(buff.toString());
        
        return d;
    }
    
    /**
     * This does a reverse of the <code>getAmountDoubleValue(String a)</code> function by converting a double amount value to its String
     * representation
     * @param d double value of the argument to be converted
     * @return String value of the returned formatted amount with leading ($) sign as required
     */
    public static String getAmountStringValue(double d){
        String val = "";
        DecimalFormat df = new DecimalFormat("$#####0.00");
        val = df.format(d);
        return val;     
    }
    
    /**
     * This function utility is used on the Client/Owner ID read from the database to handle the value read and convert to
     * appropriate primitive long datatype
     * @param i String value of whats read in database, it should have a length value of 8
     * @return long value of the processed String value parsed into the function argument. returns 0 if String value is empty or its
     * length isn't 8digit
     */
    public static long getClientId(String i){
        long l = 0;
        if(i.trim().length() != 8){
            l = 0; //not a valid client id
        }
        else{
            l = Long.parseLong(i);
        }
        return l;
    }
    
    /**
     * This function Utility call is used to generate random long value to be assigned to each Thread locking a data in the database
     * @return a Random long value that can be used as data Thread lock cookie 
     */
    public static long generateLockCookie(){
        Random rand = new Random();
        long l = Math.abs(rand.nextLong());
        return l;
    }
}
