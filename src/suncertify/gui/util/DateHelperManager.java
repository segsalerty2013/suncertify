/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui.util;

import javax.swing.*;
import java.awt.*;

/**
 * This Object serves as a Tool tip presentation of the Date entry for the user when adding a new Hotel Record. Its Guides the user 
 * on expected input requited by the JTextFields
 * @author Segun
 */
public class DateHelperManager {
    
    private JTextField year;
    private JTextField month;
    private JTextField day;
    
    /**
     * Constructs this Object with the Year, Month and Day JTextFields respectively to be dynamically changing text state in
     * respect of user input either right/wrong
     * @param y The Year JTextField instance
     * @param m The Month JTextField instance
     * @param d The Day JTextField instance
     */
    public DateHelperManager(JTextField y, JTextField m, JTextField d){
        year = y;
        month = m;
        day = d;
        init();
    }
    
    private void init(){
        year.setFont(new Font("Arial", Font.ITALIC, 12));
        year.setText("YYYY");
        year.setForeground(new Color(203, 203, 203));
        
        month.setFont(new Font("Arial", Font.ITALIC, 12));
        month.setText("MM");
        month.setForeground(new Color(203, 203, 203));
        
        day.setFont(new Font("Arial", Font.ITALIC, 12));
        day.setText("DD");
        day.setForeground(new Color(203, 203, 203));
    }
    
    /**
     * Re initialise the provided JTextField by setting its text value to the String value passed into the function argument
     * @param f JTextField instance
     * @param s String value to set the JTextField value
     */
    public void reInit(JTextField f, String s){
        f.setFont(new Font("Arial", Font.ITALIC, 12));
        f.setText(s);
        f.setForeground(new Color(203, 203, 203));        
    }
    
    /**
     * Disable the JTextField helper contents and make it suitable for user to enter value
     * @param f JTextField instance
     */
    public void startWrite(JTextField f){
        f.setFont(new Font("Tahoma", Font.PLAIN, 13));
        f.setText("");
        f.setForeground(Color.BLACK);
    }
    
    /**
     * If the user input is valid, this function call makes the help tool not show up 
     * @param f JTextField instance
     */
    public void written(JTextField f){
        f.setFont(new Font("Tahoma", Font.PLAIN, 13));
        f.setForeground(Color.BLACK);
    }
}
