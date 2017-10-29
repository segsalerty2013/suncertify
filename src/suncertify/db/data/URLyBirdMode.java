/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db.data;

/**
 * This enumeration the mode of the application in respect to use argument at program execution
 * @author Segun
 */

public enum URLyBirdMode {
    /**
     * Program is running on a SERVER mode. probably the program is currently executing with i.e
     * <code>java -jar runme.jar server</code>
     */
    SERVER, 
    
    /**
     * Program is running on a CLIENT mode. probably the program is currently executing with i.e
     * <code>java -jar runme.jar</code>  no argument 
     */
    CLIENT,
    /**
     * Program is running on a LOCAL mode. probably the program is currently executing with i.e
     * <code>java -jar runme.jar alone</code>
     */
    ALONE
}
