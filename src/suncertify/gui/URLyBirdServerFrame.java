/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import suncertify.services.URLyBirdServerImpl;
import suncertify.services.URLyBirdServerException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * This Object extends the JFrame class to display the Server Graphical User Interface necessary for the user to start and 
 * communicate with the Server and its necessary components and resources
 * @author Segun
 */
public class URLyBirdServerFrame extends JFrame{
    
    private URLyBirdServerFrame self = this;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenuItem startServerItem;
    private JMenuItem exitItem;
    private JMenuItem configItem;
    private JMenuItem aboutItem;
    
    private JPanel wrapPanel;
    private JButton startServerBtn;
    private JLabel serverStatusLbl;
    
    private Action connectAction;
    
    private URLyBirdServerImpl serverImpl;
    
    private static boolean connectionStatus =  false;
    
    /**
     * This single constructor is used to initialise the server frame parsing the Server Implementation class into it making required services
     * to be accessed by client machines which is connected to the remote system
     * @param service 
     */
    public URLyBirdServerFrame(URLyBirdServerImpl service){
        super("URLyBird Server | V 1.1");
        serverImpl = service;
        setSize(new Dimension(380, 130));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        initComponents();
        loadActions();
    }
    
    private void initComponents(){
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");
        startServerItem = new JMenuItem("start server");
        exitItem = new JMenuItem("exit");
        configItem = new JMenuItem("Configurations");
        aboutItem = new JMenuItem("about");
        
        wrapPanel = new JPanel(new BorderLayout(10, 10));
        startServerBtn = new JButton("start server");
        serverStatusLbl = new JLabel();
        serverStatusLbl.setText("<html>Ready on Port Number : <span style='color:red'>" + serverImpl.getPortNumber() + "</span> using Database File : "
                + "<span style='color:red'>" + serverImpl.getDatabaseFile().getName() + "</span></html>");
        startServerBtn.setPreferredSize(new Dimension(getWidth(), 30));
        startServerBtn.setToolTipText("click to start the server");
        serverStatusLbl.setPreferredSize(new Dimension(getWidth(), 30));
        serverStatusLbl.setForeground(Color.MAGENTA);
        serverStatusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        
        fileMenu.add(startServerItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);       
        helpMenu.add(configItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        wrapPanel.add(startServerBtn, BorderLayout.CENTER);
        wrapPanel.add(serverStatusLbl, BorderLayout.SOUTH);
        getContentPane().add(wrapPanel);
        pack();
    }
    
    private void loadActions(){
        connectAction = new AbstractAction("start server"){
            public void actionPerformed(ActionEvent e){
                connectAction.setEnabled(false); //disable the connection Action as long as connection is gonna be init now
                new SwingWorker<Void, Void>(){
                    public Void doInBackground(){
                        try{
                            serverImpl.startServer();
                            connectionStatus = true; //connection status will be true since server was successfully started
                        }
                        catch(URLyBirdServerException uE){
                            JOptionPane.showMessageDialog(self, "Sever cannot be started \nREASON: " + uE.getMessage(), "SERVER ERROR", 
                                    JOptionPane.ERROR_MESSAGE);
                            connectAction.setEnabled(true);//re enable it for retry
                        }
                        return null;
                    }
                    public void done(){
                        if(connectionStatus){
                            connectAction.putValue(NAME, "SERVER STARTED AND RUNNING");
                            connectAction.putValue(Action.SHORT_DESCRIPTION, "Exit Application anytime to stop the server");
                            serverStatusLbl.setForeground(Color.BLUE);
                            serverStatusLbl.setText("<html>Running on Port Number : <span style='color:red'>" + serverImpl.getPortNumber() + 
                                    "</span> using Database File : <span style='color:red'>" + serverImpl.getDatabaseFile().getName() + 
                                    "</span></html>");
                        }
                    }
                }.execute();
            }
        };
        connectAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        connectAction.putValue(Action.SHORT_DESCRIPTION, "Start The Server");
        startServerItem.setAction(connectAction);
        startServerBtn.setAction(connectAction);
        
        exitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //autocommit the new data the data cache is holding before exiting the application
                try{
                    serverImpl.backupData();
                }
                catch(Exception err){
                    JOptionPane.showMessageDialog(self, "APPLICATION Encountered Database Error\nREASON: " + err.getMessage(), "ERROR", 
                        JOptionPane.ERROR_MESSAGE);                     
                }
                finally{
                    System.exit(0);
                }
            }
        });
        
        configItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        URLyBirdConfigDialog configDialog = new URLyBirdConfigDialog(serverImpl, self);
                        configDialog.setVisible(true);                        
                    }
                });
            }
        });
        
        aboutItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        JOptionPane.showMessageDialog(self, "<html><pstyle='text-align:center;'>&copy;2011 Oracle URLyBird Server Version 1.1</p>"
                                + "</html>", "About", 
                            JOptionPane.PLAIN_MESSAGE);                     
                    }
                });                
            }
        });
        
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                //autocommit the new data the data cache is holding before exiting the application
                try{
                    serverImpl.backupData();
                }
                catch(Exception err){
                    JOptionPane.showMessageDialog(self, "APPLICATION Encountered Database Error\nREASON: " + err.getMessage(), "ERROR", 
                        JOptionPane.ERROR_MESSAGE);                     
                }
            }
        });
    }
}
