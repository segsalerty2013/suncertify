/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.gui;

import javax.swing.*;
import java.awt.*;


/**
 * This is an abstract class that constructs the layout/design of the Application user interface. Meant to be extended and 
 * all abstract methods are to be override to design the portion/section of the UI. the JFrame is divided into sections 
 * @author Segun
 */
public abstract class URLyBirdApplicationFrame extends JFrame{
    
    private JPanel tablePanel = new JPanel(new BorderLayout());
    //private JPanel controlPanel = new JPanel(new GridLayout(3, 1, 0, 10));
    private JPanel centerPanel = new JPanel(new BorderLayout(20, 10));
    private JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
    private JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
    private JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    
    private GridBagConstraints constant = new GridBagConstraints();
    
    /**
     * A constructor for this class which its super must be called by child class requiring its customised frame title name
     * @param frameTitle String value of the frame title to be set in this frame
     */
    public URLyBirdApplicationFrame(String frameTitle){
        setTitle(frameTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         
    }
    
    /**
     * Must override this function which handles the JMenuBar of the UI and respective JMenuItems thats required to be rendered
     * in the JMenuBar
     */
    public abstract void initMenuBar();
    
    /**
     * Must Override this function to put all Actions/ActionListeners of components thats in the User Interface that has action
     * to perform at runtime
     */
    public abstract void loadActions();
    
    /**
     * Must Override this function to create new components to be loaded/rendered into the search Panel of the GUI
     */
    public abstract void loadSearchPanel();
    
    /**
     * Must Override this function to create new components (JButtons) to be loaded/rendered into the button Panel of the GUI
     */
    public abstract void loadControlPanel();
    
    /**
     * Must Override this function to create new components to be loaded/rendered into the status Panel of the GUI
     */
    public abstract void loadStatusPanel();
    
    /**
     * Must Override this function to create new components (JTable) to be loaded/rendered into the table Panel of the GUI
     */
    public abstract void loadTablePanel();
    
    /**
     * This gets instance of the JPAnel thats renders the search criteria part of the NORTH(TOP) side of the UI
     * @return JPanel instance of the JPanel thats rendering the search components
     */
    public JPanel getSearchPanel(){
        return searchPanel;
    }
    
    /**
     * This gets instance of the JPAnel thats renders the status part of the SOUTH side of the UI
     * @return JPanel instance of the JPanel thats rendering the status JLabel
     */
    public JPanel getStatusPanel(){
        return statusPanel;
    }
    
    /**
     * This gets instance of the JPanel thats the User Interface controls are been rendered at the East side of the UI
     * @return JPanel instance of the JPanel thats rendering the controls JButtons
     */
    public JPanel getControlPanel(){
        return controlPanel;
    }
    
    /**
     * This gets the instance of the JPanel that the JTable is rendered.
     * @return JTable instance of the table this GUI is displaying
     * this table is showing records in the database
     */
    public JPanel getTablePanel(){
        return tablePanel;
    }
    
    //function initialized the components. its called by the packPanel method
    private void initComponents(){
        controlPanel.setPreferredSize(new Dimension(180, 400));
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.white));
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.EAST);
        setLayout(new GridBagLayout());
        constant.fill = GridBagConstraints.HORIZONTAL;
        constant.anchor = GridBagConstraints.PAGE_START;
        constant.gridwidth = 3;
        constant.gridx = 0;
        constant.gridy = 0;
        constant.weightx = 0.5;
        constant.weighty = 1.0;
        constant.insets = new Insets(20,0,5,0);
        add(searchPanel, constant);
        constant.fill = GridBagConstraints.BOTH;
        constant.anchor = GridBagConstraints.CENTER;
        constant.gridwidth = 3;
        constant.weightx = 0.5;
        constant.gridx = 0;
        constant.gridy = 1;
        constant.insets = new Insets(20,0,5,0);
        add(centerPanel, constant);
        /*
        constant.fill = GridBagConstraints.VERTICAL;
        constant.anchor = GridBagConstraints.EAST;
        constant.gridwidth = 1;
        constant.gridx = 1;
        constant.gridy = 1;
        constant.ipady = 0;
        constant.insets = new Insets(20,0,20,0);
        add(controlPanel, constant);
         * 
         */
        constant.fill = GridBagConstraints.HORIZONTAL;
        constant.anchor = GridBagConstraints.PAGE_END;
        constant.gridwidth = 3;
        constant.gridx = 0;
        constant.gridy = 2;
        constant.ipady = 0;
        constant.insets = new Insets(20,0,5,0);
        add(statusPanel, constant);
        pack();
    }
    
    /**
     * This function call on this class packs all the panels with there respective components added to them
     * This should be called after the abstract methods of this class has been override by child class
     */
    public void packPanel(){
        initMenuBar();
        loadSearchPanel();
        loadControlPanel();
        loadStatusPanel();
        loadTablePanel();
        initComponents();
        loadActions();
    }

}
