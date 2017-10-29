/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import suncertify.gui.util.DateHelperManager;
import suncertify.db.data.URLyBirdDataTools;
import suncertify.exception.URLyBirdException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This abstract class extends the JDialog class
 * It draws to the user interface required components thats Dialog needs to create a new URLyBirdHotel record which will be added to the Database
 * @author Segun
 */
public abstract class NewURLyBirdHotelDialog extends JDialog{
    
    private JPanel wrapPanel;
    private JLabel nameLbl;
    private JLabel cityLbl;
    private JLabel maxOcupantsLbl;
    private JLabel smokingStatLbl;
    private JLabel priceLbl;
    private JLabel dollarLbl;
    private JLabel dateLbl;
    
    private JPanel priceTxtPanel;
    private JPanel dateTxtPanel;
    
    private JTextField nameTxt;
    private JTextField cityTxt;
    private JTextField maxOcupantsTxt;
    private JComboBox smokingStatCombo;
    private JTextField priceTxt;
    private JTextField dayTxt;
    private JTextField monthTxt;
    private JTextField yearTxt;
    
    private JButton okBtn;
    private JButton cancelBtn;
    
    private String ownerId = "";//an empty owner ID for default when creating new URLyBirdHotel
    
    private DateHelperManager dateHelper;
    
    /**
     * A constructor that needed to be called by child class using super() providing the parent JComponent(JFrame) which this Dialog should
     * appear on top.
     * @param parent The parent JFrame UI 
     */
    public NewURLyBirdHotelDialog(JFrame parent){
        setSize(300, 200);
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(parent);
        this.setUndecorated(true);
        setLayout(new BorderLayout());
        initComponents();
        dateHelper = new DateHelperManager(yearTxt, monthTxt, dayTxt);
        loadActions();
        setVisible(true);
        parent.setEnabled(false);//as soon as this Dialog is visible, dissable the parent dialog
    }
    
    /**
     * Override this abstract method in child class to provide instructions to be executed when the okButton on the GUI is clicked
     * this function calls the <code>getValue()</code> of this class perform the adding of new URLyBirdHotel to the database
     */
    public abstract void add();
    
    /**
     * Override this abstract method in child class to provide instructions to be executed when the cancelButton on the GUI is clicked
     */
    public abstract void cancel();
    
    private void initComponents(){
        wrapPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        wrapPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        nameLbl = new JLabel("Hotel Name : ");
        nameLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        cityLbl = new JLabel("City : ");
        cityLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        smokingStatLbl = new JLabel("Smoking Status : ");
        smokingStatLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        priceLbl = new JLabel("Price : ");
        priceLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        dollarLbl = new JLabel("$");
        dateLbl = new JLabel("Date Available : ");
        dateLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        maxOcupantsLbl = new JLabel("Max Occupants : ");
        maxOcupantsLbl.setHorizontalAlignment(SwingConstants.TRAILING);
        nameTxt = new JTextField();
        cityTxt = new JTextField();
        maxOcupantsTxt = new JTextField();
        smokingStatCombo = new JComboBox(new Object[]{"", "Y", "N"});
        priceTxt = new JTextField();
        priceTxt.setPreferredSize(new Dimension((this.getWidth()/2) - 30, 22));
        priceTxtPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        dateTxtPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        yearTxt = new JTextField(4);
        monthTxt = new JTextField(2);
        dayTxt = new JTextField(2);
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        dateTxtPanel.add(yearTxt);
        dateTxtPanel.add(new JLabel("/"));
        dateTxtPanel.add(monthTxt);
        dateTxtPanel.add(new JLabel("/"));
        dateTxtPanel.add(dayTxt);
        priceTxtPanel.add(dollarLbl);
        priceTxtPanel.add(priceTxt);
        wrapPanel.add(nameLbl);
        wrapPanel.add(nameTxt);
        wrapPanel.add(cityLbl);
        wrapPanel.add(cityTxt);
        wrapPanel.add(maxOcupantsLbl);
        wrapPanel.add(maxOcupantsTxt);
        wrapPanel.add(smokingStatLbl);
        wrapPanel.add(smokingStatCombo);
        wrapPanel.add(priceLbl);
        wrapPanel.add(priceTxtPanel);
        wrapPanel.add(dateLbl);
        wrapPanel.add(dateTxtPanel);
        wrapPanel.add(okBtn);
        wrapPanel.add(cancelBtn);
        getContentPane().add(wrapPanel, BorderLayout.CENTER);
    }
    
    private void loadActions(){
        okBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        add(); //call the abstract method to perform the adding of newURLyBirdHotel into the database
                    }
                });                
            }
        });
        
        cancelBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        cancel();//call the abstract method to perform the cancel task
                    }
                });                
            }
        });
        
        yearTxt.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent fE){                
                if(yearTxt.getText().equals("YYYY")){
                    dateHelper.startWrite(yearTxt);
                }
            }
                    
            public void focusLost(FocusEvent fE){
                if(yearTxt.getText().length() == 4){
                    try{
                        Integer.parseInt(yearTxt.getText());//if the value provided can be successfully passed to INT
                        dateHelper.written(yearTxt);
                    }
                    catch(NumberFormatException nE){
                        dateHelper.reInit(yearTxt, "YYYY");
                    }                          
                }
                else{
                    dateHelper.reInit(yearTxt, "YYYY");
                }
            }
        });
        
        monthTxt.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent fE){                
                if(monthTxt.getText().equals("MM")){
                    dateHelper.startWrite(monthTxt);
                }
            }
                    
            public void focusLost(FocusEvent fE){
                if(monthTxt.getText().length() == 2){
                    try{
                        Integer.parseInt(monthTxt.getText());//if the value provided can be successfully passed to INT
                        dateHelper.written(monthTxt);
                    }
                    catch(NumberFormatException nE){
                        dateHelper.reInit(monthTxt, "MM");
                    }                          
                }
                else{
                    dateHelper.reInit(monthTxt, "MM");
                }
            }
        });
        
        dayTxt.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent fE){                
                if(dayTxt.getText().equals("DD")){
                    dateHelper.startWrite(dayTxt);
                }
            }
                    
            public void focusLost(FocusEvent fE){
                if(dayTxt.getText().length() == 2){
                    try{
                        Integer.parseInt(dayTxt.getText());//if the value provided can be successfully passed to INT
                        dateHelper.written(dayTxt);
                    }
                    catch(NumberFormatException nE){
                        dateHelper.reInit(dayTxt, "DD");
                    }                          
                }
                else{
                    dateHelper.reInit(dayTxt, "DD");
                }
            }
        });
    }  
    
    /**
     * Reads all filled Data from the User Interface VALIDATE IT and gets the values in form of array of String length 7 which each index
     * represents a field in the database
     * @return String[] of data filled via the UI
     * @throws NumberFormatException if price value provided cannot be converted to a Double value or any of either, day, month, or year value
     * cannot be converted to an integer value
     * @throws URLyBirdException if any of the field required is left empty 
     */
    public String[] getValues() throws NumberFormatException, URLyBirdException{
        if(nameTxt.getText().isEmpty() || cityTxt.getText().isEmpty() || maxOcupantsTxt.getText().isEmpty() || 
                smokingStatCombo.getSelectedItem().toString().isEmpty() || priceTxt.getText().isEmpty() || yearTxt.getText().isEmpty()
                || monthTxt.getText().isEmpty() || dayTxt.getText().isEmpty()){
            throw new URLyBirdException("One or Some Fields left Empty in creating New URLyBirdHotel");
        }
        Integer.parseInt(yearTxt.getText());
        Integer.parseInt(monthTxt.getText());
        Integer.parseInt(dayTxt.getText());
        if(yearTxt.getText().length() != 4 || monthTxt.getText().length() != 2 || dayTxt.getText().length() != 2){
            throw new URLyBirdException("The Date provided is not valid. Correct Date format is yyyy/mm/dd");
        }
        String vals[] = new String[7];
        vals[0] = nameTxt.getText().substring(0, 1).toUpperCase() + nameTxt.getText().substring(1).toLowerCase();
        vals[1] = cityTxt.getText().substring(0, 1).toUpperCase() + cityTxt.getText().substring(1).toLowerCase();
        vals[2] = maxOcupantsTxt.getText();
        vals[3] = smokingStatCombo.getSelectedItem().toString();
        vals[4] = URLyBirdDataTools.getAmountStringValue(Double.parseDouble(priceTxt.getText()));//while parsing the priceTxt to a double value.
        //NumberFormatException thrown should be thown to the function call
        vals[5] = yearTxt.getText() + "/" + monthTxt.getText() + 
                "/" + dayTxt.getText();//while parsing date integers.... if it throws NumberFormatException... it should be 
        //thrown to the function call
        vals[6] = ownerId;     
        return vals;
    }
}
