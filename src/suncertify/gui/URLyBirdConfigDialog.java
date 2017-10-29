/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import suncertify.db.data.URLyBirdMode;
import suncertify.gui.events.*;
import suncertify.services.*;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This Class handles the URLyBird Configuration dialog User Interface, manages the components the dialog will contain 
 * in respect to the mode the application is running on to fit the Requirement.
 * @author Segun
 */
public class URLyBirdConfigDialog extends JDialog{
    
    private final URLyBirdConfigDialog self = this;//stores an instance of one self
    private JFrame parentFrame;
    
    private URLyBirdMode mode;
    private static URLyBirdServices services; //instantiate the services interface
    private static File theDbFile = new File("");

    private JPanel wrapPanel;
    private JPanel titlePanel;
    private JPanel formPanel;
    private JPanel btnPanel;
    private JLabel titleLabel;
    private JLabel fileDirLbl;
    private JButton dbFileChooserBtn;
    private JLabel hostNameLbl;
    private JTextField hostNameTxt;
    private JLabel portLabel;
    private JTextField portTxt;
    private JButton saveBtn;
    private JButton resetBtn;
    private JButton cancelBtn;
    
    /**
     * This is the initial constructor call of this Object. Other constructors only modifies the configuration during the application use
     * either via the Menu or Configuration button. This constructor determines the components to be rendered in the configuration dialog in
     * respect to the mode of which the application was started i.e Server or Standalone or Client
     * @param m - The URLyBirdMode which is the mode which the application is executed
     */
    public URLyBirdConfigDialog(URLyBirdMode m){
        mode = m;
        setSize(new Dimension(370, 190));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        if(mode == URLyBirdMode.SERVER){
            setTitle("URLyBird Server Configuration Dialog");
            initRemoteComponents();
        }
        else if(mode == URLyBirdMode.ALONE){
            setTitle("URLyBird Alone Configuration Dialog");
            initAloneComponents();
        }
        else if(mode == URLyBirdMode.CLIENT){
            setTitle("URLyBird Client Configuration Dialog");
            initClientComponents();            
        }
        loadActions();
    }
    
    /**
     * This Constructor is called when the Application (Thread) is running on a Server Mode. Necessary Components
     * required to configure the Remote server are been rendered in the User Interface in respect to the requirement of
     * a Server mode
     * @param remote - RemoteURLyBirdServices Object passing its instance into the Configuration dialog for accessing functions available
     * to access shared resources on the network
     * @param parent - the Parent JComponent for which the Configuration dialog will be displayed on top
     */
    public URLyBirdConfigDialog(RemoteURLyBirdServices remote, JFrame parent){
        services = remote; //downcasted to the URLyBirdServices interface so that only the function call <code>backupData()</code> can be called
        parentFrame = parent;
        mode = URLyBirdMode.SERVER; //since its running as a server
        setTitle("URLyBird Server Configuration Dialog");
        setSize(new Dimension(370, 190));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        initRemoteComponents();
        loadActions();
    }
    
    /**
     * This Constructor is called when the Application (Thread) is running on a Standalone (single) Mode. Necessary Components
     * required to configure the User Interface so as to use the application without any Sever connection. the application
     * resources (i.e database file) required are located on the local machine
     * @param local - LocalURLyBirdServices Object passing its instance into the Configuration dialog for accessing functions available
     * to work with the Application required resources on the local system. No Networking required
     * @param parent the Parent JComponent for which the Configuration dialog will be displayed on top
     */
    public URLyBirdConfigDialog(LocalURLyBirdServices local, JFrame parent){
        services = local; //downcasted to the URLyBirdServices interface so that only the function call <code>backupData()</code> can be called
        parentFrame = parent;
        mode = URLyBirdMode.ALONE; //since its running on local services
        setTitle("URLyBird Configuration Dialog");
        setSize(new Dimension(370, 190));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        initAloneComponents();
        loadActions();        
    }
    
    /**
     * This Constructor is called when the Application (Thread) is running on a Networked (CLIENT) Mode. Necessary Components
     * required to configure the User Interface so as to use the application with any Sever configurations (host address/IP). the application
     * @param service URLyBirdServices Object passing its instance into the Configuration dialog for accessing functions available
     * to work with the Application required resources on the Remote Network using RMI
     * @param parent the Parent JComponent for which the Configuration dialog will be displayed on top
     */
    public URLyBirdConfigDialog(URLyBirdServices service, JFrame parent){
        services = service;
        parentFrame = parent;
        mode = URLyBirdMode.CLIENT; //since its running as a server
        setTitle("URLyBird Configuration Dialog");
        setSize(new Dimension(370, 190));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        initClientComponents();
        loadActions();
    }
    
    @Override
    public void setVisible(boolean b){
        if(parentFrame != null){
            parentFrame.hide();
        }
        show();
    }
   
    /**
     * Gets the String value of the port number provided in the dialog
     * @return - String value of the port number in the port text field
     */
    public String getPortNumberText(){
        return portTxt.getText();
    }
    
    /**
     * Gets the JTextField instance of the Configuration Dialog port Number
     * @return - JTextField instance that user provide the port number
     */
    public JTextField getPortNumberTextField(){
        return portTxt;
    }
    
    /**
     * Gets the String value of the remote host name or IP address provided number in the dialog
     * @return String value of the remote host name or IP address in the port text field
     */
    public String getHostName(){
        return hostNameTxt.getText();
    }
    
    /**
     * Gets the JTextField instance of the Configuration Dialog host name
     * @return JTextField instance that user provide the host Name or IP
     */
    public JTextField getHostNameTextField(){
        return hostNameTxt;
    }
    
    /**
     * Gets the instance of the Configuration Dialog File chooser button
     * @return - JButton instance of the button which user clicks to select a database in the configuration dialog
     */
    public JButton getDbChooserButton(){
        return dbFileChooserBtn;
    }
    /**
     * Gets the current database file instance thats being chosen from the dialog UI or return null of no file chose yet
     * @return - File instance of the current object
     */
    public File getDbFile(){
        return theDbFile;
    }
    
    /**
     * Gets the current service instance of this object to be referenced from another object so as to use its function
     * @return URLyBirdServices instance of this object at runtime
     */
    public URLyBirdServices getCurrentService(){
        return services;
    }
    
    private void initRemoteComponents(){
        wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        formPanel.setPreferredSize(new Dimension(300, 70));
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 2));
        titleLabel = new JLabel("SELECT DATABASE FILE & ENTER THE REMOTE ACCESS PORT");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 11));
        fileDirLbl = new JLabel("****** \tDatabase File :");
        fileDirLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileDirLbl.setFont(new Font("Ariel", Font.ITALIC, 13));
        dbFileChooserBtn = new JButton("SELECT");
        dbFileChooserBtn.setToolTipText("choose a database file");
        //
        portLabel = new JLabel("****** \tPort Number :");
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setFont(new Font("Ariel", Font.ITALIC, 13));
        portTxt = new JTextField();
        portTxt.setToolTipText("provide a valid port number");
        saveBtn = new JButton("Save");
        saveBtn.setToolTipText("save configuration");
        resetBtn = new JButton("Reset");
        resetBtn.setToolTipText("reset the configuration");
        cancelBtn = new JButton("Cancel");
        cancelBtn.setToolTipText("cancel configuration");
        
        titlePanel.add(titleLabel);
        formPanel.add(fileDirLbl);
        formPanel.add(dbFileChooserBtn);
        formPanel.add(portLabel);
        formPanel.add(portTxt);
        btnPanel.add(saveBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(cancelBtn);
        wrapPanel.add(titlePanel);
        wrapPanel.add(formPanel);
        wrapPanel.add(btnPanel);
        
        getContentPane().add(wrapPanel);
        
        //cancel the Database File Chooser actionListener job below
        dbFileChooserBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){                
                JFileChooser fileChoosed = new JFileChooser(new File("").getAbsolutePath());
                fileChoosed.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("URLyBird Database File", "db"));
                if(fileChoosed.showOpenDialog(dbFileChooserBtn) == JFileChooser.APPROVE_OPTION){
                    theDbFile = fileChoosed.getSelectedFile();
                    dbFileChooserBtn.setText("'" + theDbFile.getName() + "'");//set the button text to the name of the current file selected
                    dbFileChooserBtn.setEnabled(false);//first of all disable this btn
                }
            }
        });
    }

    
    private void initAloneComponents(){
        wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        formPanel.setPreferredSize(new Dimension(300, 30));
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 2));
        titleLabel = new JLabel("SELECT DATABASE FILE TO USE");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 11));
        fileDirLbl = new JLabel("****** \tDatabase File :");
        fileDirLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileDirLbl.setFont(new Font("Ariel", Font.ITALIC, 13));
        dbFileChooserBtn = new JButton("SELECT");
        dbFileChooserBtn.setToolTipText("choose a database file");

        saveBtn = new JButton("Save");
        saveBtn.setToolTipText("save configuration");
        resetBtn = new JButton("Reset");
        resetBtn.setToolTipText("reset the configuration");
        cancelBtn = new JButton("Cancel");
        cancelBtn.setToolTipText("cancel configuration");
        
        titlePanel.add(titleLabel);
        formPanel.add(fileDirLbl);
        formPanel.add(dbFileChooserBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(cancelBtn);
        wrapPanel.add(titlePanel);
        wrapPanel.add(formPanel);
        wrapPanel.add(btnPanel);
        
        getContentPane().add(wrapPanel);
        
        //cancel the Database File Chooser actionListener job below
        dbFileChooserBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){                
                JFileChooser fileChoosed = new JFileChooser(new File("").getAbsolutePath());
                fileChoosed.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("URLyBird Database File", "db"));
                if(fileChoosed.showOpenDialog(dbFileChooserBtn) == JFileChooser.APPROVE_OPTION){
                    theDbFile = fileChoosed.getSelectedFile();
                    dbFileChooserBtn.setText("'" + theDbFile.getName() + "'");//set the button text to the name of the current file selected
                    dbFileChooserBtn.setEnabled(false);//first of all disable this btn
                }
            }
        });
    }
    
    private void initClientComponents(){
        wrapPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        formPanel.setPreferredSize(new Dimension(300, 70));
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 2));
        titleLabel = new JLabel("CONFIGURE CLIENT's REMOTE HOST ADDRESS & PORT");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 11));
        hostNameLbl = new JLabel("****** \tHOST Address :");
        hostNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        hostNameLbl.setFont(new Font("Ariel", Font.ITALIC, 13));
        hostNameTxt = new JTextField();
        hostNameTxt.setToolTipText("provide a valid remote host name / ip");
        portLabel = new JLabel("****** \tPort Number :");
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setFont(new Font("Ariel", Font.ITALIC, 13));
        portTxt = new JTextField();
        portTxt.setToolTipText("provide a valid port number");
        
        saveBtn = new JButton("Save");
        saveBtn.setToolTipText("save configuration");
        resetBtn = new JButton("Reset");
        resetBtn.setToolTipText("reset the configuration");
        cancelBtn = new JButton("Cancel");
        cancelBtn.setToolTipText("cancel configuration");
        
        titlePanel.add(titleLabel);
        formPanel.add(hostNameLbl);
        formPanel.add(hostNameTxt);
        formPanel.add(portLabel);
        formPanel.add(portTxt);
        btnPanel.add(saveBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(cancelBtn);
        wrapPanel.add(titlePanel);
        wrapPanel.add(formPanel);
        wrapPanel.add(btnPanel);
        
        getContentPane().add(wrapPanel);
    }
    
    private void loadActions(){
        //cancel button actionListener job below
        cancelBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                self.dispose();//dispose this dialog
                if(parentFrame != null){
                    parentFrame.show();//show the parent components as soon as this dialog is disposed
                }
            }
        });
        
        resetBtn.addActionListener(new ResetConfigurationAction(self, mode));

        saveBtn.addActionListener(new SaveConfigurationAction(self, mode));//add actionListener to the save Button in respect of the mode in use
    }    
}
