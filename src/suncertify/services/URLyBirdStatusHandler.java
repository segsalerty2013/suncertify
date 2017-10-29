/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.services;

import java.awt.Color;
import suncertify.db.data.URLyBirdStatusOption;
import javax.swing.JLabel;

/**
 * Handles the status Label on the Application User Interface showing the application updated status
 * @author Segun
 */
public class URLyBirdStatusHandler {
    
    private static JLabel statusLabel;
    private static URLyBirdStatusOption status;
    private static String statusMessage;
    
    private static void handle(){
        statusLabel.setText(statusMessage);
        switch(status){
            case SUCCESS:
                statusLabel.setForeground(Color.BLUE);
                break;
            case ERROR:
                statusLabel.setForeground(Color.RED);
                break;
            default:
                statusLabel.setForeground(Color.WHITE);
                break;
        }
    }
    
    /**
     * This static function sets the text of the JLabel designated to display the current status of success or error on the application
     * @param l JLabel instance to display the success or error message
     * @param op <code>enum</code> instance of the status option mode which the status be displayed. if SUCCES, text will be painted blue,
     * if ERROR, will be painted red
     * @param s The String value to be set on the JLabel as it changes value
     */
    public static void setStatus(JLabel l, URLyBirdStatusOption op, String s){
        statusLabel = l;
        status = op;
        statusMessage = s;
        handle();      
    }
    
}
