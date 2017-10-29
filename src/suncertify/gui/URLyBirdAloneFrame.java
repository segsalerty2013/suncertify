/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import suncertify.db.*;
import suncertify.db.data.*;
import suncertify.gui.events.URLyBirdTableSelectionListener;
import suncertify.services.*;
import suncertify.exception.URLyBirdException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class extends URLyBirdApplicationFrame to display the Alone Frame User Interface for the user to interact with when running in
 * a non networking environment mode
 * @author Segun
 */
public class URLyBirdAloneFrame extends URLyBirdApplicationFrame{
    
    private URLyBirdAloneFrame self = this;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenuItem exitItem;
    private JMenuItem configItem;
    private JMenuItem aboutItem;
    
    private JLabel nameLbl;
    private JTextField nameTxt;
    private JLabel locationLbl;
    private JTextField locationTxt;
    private JButton searchBtn;
    
    private JButton bookBtn;
    private JButton createBtn;
    private JButton deleteBtn;
    
    private JButton []tableControls; //store buttons that its state depends on whats going on with the table
    
    private JLabel statusLbl;
    
    private JTable table;
    private JScrollPane tableScroll;
    private URLyBirdTableModel tableModel;
    
    private LocalURLyBirdServices services;
    
    private URLyBirdTableSelectionListener tableSelectionListener;
    
    private static String []searchCriteria = {"", ""};
    
    private String prevSuccessMsg = ""; //stored the success message summary that occurs at user task execution
    
    /**
     * This one constructor constructs the alone frame passing the Object instance of the LocalURLyBirdServices instance making all available
     * local functions for the alone service be available for use in this User Interface (JFrame) in a non networked environment
     * @param s Instance of the local services to be used that contains necessary functions to be executed
     */
    public URLyBirdAloneFrame(LocalURLyBirdServices s){
        super("URLyBird | ALONE MODE");
        services = s;
    }
    
    public void initMenuBar(){
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        exitItem = new JMenuItem("exit");
        configItem = new JMenuItem("Configurations");
        aboutItem = new JMenuItem("about");
        
        fileMenu.add(exitItem);       
        helpMenu.add(configItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    
    public void loadActions(){
        tableSelectionListener = new URLyBirdTableSelectionListener(table, tableControls);
        table.getSelectionModel().addListSelectionListener(tableSelectionListener);
        
        searchBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{                   
                    searchCriteria[0] = nameTxt.getText();
                    searchCriteria[1] = locationTxt.getText();
                    tableModel.updateURLyBirdTableModel(services.searchRecords(searchCriteria));
                }
                catch(RecordNotFoundException rE){
                    System.err.println(rE.getMessage());
                    URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                        rE.getMessage());
                }
                finally{
                    reDefault();
                }
            }
        });
        
        bookBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        try{
                            String input = null;
                            input = JOptionPane.showInputDialog(self, "Enter the Customer 8 digits ID", "Book Selected Room to Customer",
                                    JOptionPane.PLAIN_MESSAGE);
                            int recNo = tableModel.getRowKey(tableSelectionListener.getSelectedRowNumber());
                            services.bookRoom(recNo, Long.parseLong(input));//call service to book the room
                            JOptionPane.showMessageDialog(self, "Record Number " + recNo + " Successfully Booked", "SUCCESS", JOptionPane.INFORMATION_MESSAGE); 
                            prevSuccessMsg = "Record Number " + recNo + " Successfully Booked";                            
                        }
                        catch(NumberFormatException nFE){
                            JOptionPane.showMessageDialog(self, nFE.getMessage() + " is an invalid Customer ID provided , Room Booking Canceled");
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    nFE.getMessage() + " is an invalid Customer ID provided , Room Booking Canceled");
                        }
                        catch(NullPointerException nE){
                            JOptionPane.showMessageDialog(self, nE.getMessage() + " Customer ID provided , Room Booking Canceled");
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    nE.getMessage() + " Customer ID provided , Room Booking Canceled");
                        }
                        catch(RecordNotFoundException rE){
                            JOptionPane.showMessageDialog(self, rE.getMessage(), "Record Not Found", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    rE.getMessage());
                        }
                        catch(InvalidFieldLengthException fE){
                            JOptionPane.showMessageDialog(self, fE.getMessage(), "Invalid Field Length", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    fE.getMessage());
                        }
                        catch(SecurityException sE){
                            JOptionPane.showMessageDialog(self, sE.getMessage(), "ACCESS DENIED", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    sE.getMessage());
                        }
                        catch(RoomAlreadyBookedException aE){
                            JOptionPane.showMessageDialog(self, aE.getMessage(), "CANNOT BOOK ROOM", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    aE.getMessage());
                        }
                        finally{
                            reDefault();
                        }
                    }
                });
            }
        });
        
        deleteBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        try{
                            int recNo = tableModel.getRowKey(tableSelectionListener.getSelectedRowNumber());
                            services.deleteRoom(recNo);
                            JOptionPane.showMessageDialog(self, "Record Number " + recNo + " Successfully Deleted", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                            prevSuccessMsg = "Record Number " + recNo + " Successfully Deleted";
                        }
                        catch(RecordNotFoundException rE){
                            JOptionPane.showMessageDialog(self, rE.getMessage(), "Record Not Found", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    rE.getMessage());
                        }
                        catch(SecurityException sE){
                            JOptionPane.showMessageDialog(self, sE.getMessage(), "ACCESS DENIED", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    sE.getMessage());
                        }
                        finally{
                            reDefault();
                        }
                    }
                });
            }
        });
        
        createBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new NewURLyBirdHotelDialog(self) {
                    @Override
                    public void add() {
                        try{
                            String []newValues = this.getValues();
                            int recNo = services.createRoom(newValues);
                            this.dispose();//dispose this dialog as soon as createRoom successful
                            self.setEnabled(true); //reEnable the parent Frame after dialog is been disposed
                            JOptionPane.showMessageDialog(self, "New Record created in Record Number " + recNo, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                            prevSuccessMsg = "New Record created in Record Number " + recNo;                
                        }
                        catch(URLyBirdException uE){
                            JOptionPane.showMessageDialog(this, uE.getMessage(), "FORM PROCESS ERROR", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    uE.getMessage());
                        }
                        catch(NumberFormatException nE){
                            JOptionPane.showMessageDialog(this, nE.getMessage(), "FORM PROCESS ERROR", JOptionPane.ERROR_MESSAGE);
                            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, 
                                    nE.getMessage());
                        }
                        finally{
                            reDefault();
                        }
                    }
                    @Override
                    public void cancel() {
                        this.dispose();
                        self.setEnabled(true);
                    }
                };
            }
        });
        
        exitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        try{
                            services.backupData();
                        }
                        catch(URLyBirdException uE){
                            System.err.println(uE.getMessage());
                        }
                        finally{
                            System.exit(0);
                        }
                    }
                });               
            }
        });
        
        configItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(services, self);
                        configDialog.setVisible(true);                        
                    }
                });                
            }
        });
        
        aboutItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        JOptionPane.showMessageDialog(self, "<html><pstyle='text-align:center;'>&copy;2011 Oracle URLyBird Alone Version 1.1</p>"
                                + "</html>", "About", 
                            JOptionPane.PLAIN_MESSAGE);                     
                    }
                });                 
            }
        });
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try{
                    services.backupData();
                }
                catch(URLyBirdException uE){
                    JOptionPane.showMessageDialog(self, uE.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    public void loadTablePanel(){
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel = new URLyBirdTableModel();
        table.setModel(tableModel);
        tableScroll = new JScrollPane();
        tableScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tableScroll.setViewportView(table);
        getTablePanel().add(tableScroll);
    }
    
    public void loadStatusPanel(){
        statusLbl = new JLabel();
        getStatusPanel().add(statusLbl);
    }
    
    public void loadControlPanel(){
        bookBtn = new JButton("Book");
        bookBtn.setPreferredSize(new Dimension(150, 30));
        bookBtn.setEnabled(false);
        deleteBtn = new JButton("Delete");
        deleteBtn.setPreferredSize(new Dimension(150, 30));
        deleteBtn.setEnabled(false);
        createBtn = new JButton("Create");
        createBtn.setPreferredSize(new Dimension(150, 30));
        getControlPanel().add(bookBtn);       
        getControlPanel().add(deleteBtn);
        getControlPanel().add(createBtn);
        
        tableControls = new JButton[2];
        tableControls[0] = bookBtn;
        tableControls[1] = deleteBtn;
    }
    
    public void loadSearchPanel(){
        nameLbl = new JLabel("Name :");
        //nameLbl.setPreferredSize(new Dimension(150, 30));
        nameTxt = new JTextField();
        nameTxt.setPreferredSize(new Dimension(150, 30));
        locationLbl = new JLabel("Location :");
        //locationLbl.setPreferredSize(new Dimension(150, 30));
        locationTxt = new JTextField();
        locationTxt.setPreferredSize(new Dimension(150, 30));
        searchBtn = new JButton("Search");
        searchBtn.setPreferredSize(new Dimension(150, 30));
        getSearchPanel().add(nameLbl);
        getSearchPanel().add(nameTxt);
        getSearchPanel().add(locationLbl);
        getSearchPanel().add(locationTxt);
        getSearchPanel().add(searchBtn);
    }
    
    /**
     * This private function sets the UI to default values.. this occur when the data table structure changes
     * the book and delete button are disabled at this process
     */
    private void reDefault(){
        try{
            tableModel.updateURLyBirdTableModel(services.searchRecords(searchCriteria));//then update the table
            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.SUCCESS, 
                prevSuccessMsg + " \t Found " + tableModel.getRowCount() + " Record(s)");
        }
        catch(RecordNotFoundException rE){
            System.err.println(rE.getMessage());
            URLyBirdStatusHandler.setStatus(statusLbl, URLyBirdStatusOption.ERROR, rE.getMessage());
        }
        finally{
            bookBtn.setEnabled(false);
            deleteBtn.setEnabled(false);            
        }
    }
}
