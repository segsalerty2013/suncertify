/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui.events;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTable;
import javax.swing.JButton;

/**
 * This implements the <code>ListSelectionListener</code> used to monitor the record table row selected by the user
 * @author Segun
 */
public class URLyBirdTableSelectionListener implements ListSelectionListener{
    
    private JTable table;
    private int selectedRow;
    private JButton []buttons;
    
    /**
     * Construct this Object by passing the JTable to be listen to its row selection and array if control buttons whole listened effect 
     * acts upon. ie. enable/disable them in respect of what this object listens to from the event that occurs on the JTable
     * @param t The JTable instance to listen to its row selection
     * @param b Array of buttons to enable/disable accordingly to the row selection event listened to
     */
    public URLyBirdTableSelectionListener(JTable t, JButton []b){
        this.table = t;
        this.buttons = b;
    }
    
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()){
            selectedRow = table.getSelectedRow();
            for(JButton b : buttons){
                b.setEnabled(true); //enable all the buttons passed in the constructor
            }
        }
    }
    
    /**
     * This returns the row number of the JTable thats been selected
     * @return <code>Integer</code> value of the row number that was selected
     */
    public int getSelectedRowNumber(){
        return selectedRow;
    }
}
