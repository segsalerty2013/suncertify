/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import suncertify.db.data.URLyBirdHotel;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * This class extends the <code>AbstractTableModel</code> class of the java library which populates the table to present the data
 * @author Segun
 */
public class URLyBirdTableModel extends AbstractTableModel{
    
    private static final String []header = {"Record Number", "Name", "Location", "Size", "Smoking Status", "Rate", "Data Available", "Owner ID"};
    private Map<Integer, URLyBirdHotel> data = new HashMap<Integer, URLyBirdHotel>();
    private Vector<Integer> keysVector = new Vector<Integer>();

    /**
     * An Empty Constructor to initialise the table model leaving it out for the <code>updateURLyBirdTableModel(Map<Integer, URLyBirdHotel> d)</code>
     *  function to handle the population of data to be presented by this table model
     */
    public URLyBirdTableModel(){
        
    }    

    public Object getValueAt(int row, int col){
        Object obj = null;
        int primaryKey = keysVector.elementAt(row);//the primary key of the row value
        if(row < data.size()){            
            String []r = data.get(primaryKey).toStringArray(primaryKey);//get the URLyBirdHotel Object and store its String Array value in []r
            if(col < r.length){
                obj = r[col];
            }
        }
        return obj;
    }

    public int getColumnCount(){
        return header.length;
    }

    public int getRowCount(){
        return data.size();
    }

    @Override
    public String getColumnName(int c){
        if(c < header.length){
            return header[c];
        }
        else{
            return "unknown";
        }
    } 
    
    /**
     * This gets the row database primary key value which this model has stored
     * @param row - <code>Integer</code> value of the row number to get its database primary key
     * @return - <code>Integer</code> value of primary key of the row number provided
     */
    public int getRowKey(int row){
        return Integer.parseInt(getValueAt(row, 0).toString());
    }
    
    /**
     * This function updates the table by making changes to the data in the table Model. 
     * @param d - Map of new data to present in the table is passed including there unique primary key and URLyBirdHotel Object value
     */
    public void updateURLyBirdTableModel(Map<Integer, URLyBirdHotel> d){
        data = d;
        keysVector.clear(); //clear the keys vector since the table is gonna update
        Object []keys = data.keySet().toArray();//get a Set representation of the Map keys and turn to array of object
        Arrays.sort(keys);//sort the array
        for(Object i : keys){
            keysVector.addElement(Integer.parseInt(i.toString()));
        }
        this.fireTableDataChanged();
    }
}
