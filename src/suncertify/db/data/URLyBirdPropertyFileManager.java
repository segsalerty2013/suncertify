/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package suncertify.db.data;

import java.io.*;

/**
 * This Object is preserved to manage the writing and reading of the application property file/configuration file as required 
 * in respect of the mode the application is running on
 * @author Segun
 */

public class URLyBirdPropertyFileManager {
    
    private static final String PROPERTY_FILE_NAME = "suncertify.properties";
    
    /**
     * This function accepts 2 arguments that will be written respectively into the required <code>"suncertify.properties"</code> which
     * is located in the current working directory. The argument String value and long value are written into the file using DataOutputStream
     * Its designed for writing property file of the application when running on Server mode
     * @see DataOutputStream
     * @param dbPath - The String value of the database path
     * @param port - long value of the port number which the server resides
     * @throws IOException - if an error occur when writing to the designated stream
     */
    public static void writeServerProperties(String dbPath, long port) throws IOException{
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream output = new FileOutputStream(file);
        DataOutputStream data = new DataOutputStream(output);
        data.writeUTF(dbPath + "\n");
        data.writeLong(port);
        data.close();
        output.close();
    }
    
    /**
     * This function accepts 2 arguments that will be written respectively into the required <code>"suncertify.properties"</code> which
     * is located in the current working directory. The argument String value and long value are written into the file using DataOutputStream
     * Its designed for writing property file of the application when running on Server mode
     * @see DataOutputStream
     * @param hostName - The String value of the server host Address or IP
     * @param port - long value of the port number which the server resides
     * @throws IOException - if an error occur when writing to the designated stream
     */
    public static void writeClientProperties(String hostName, long port) throws IOException{
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream output = new FileOutputStream(file);
        DataOutputStream data = new DataOutputStream(output);
        data.writeUTF(hostName + "\n");
        data.writeLong(port);
        data.close();
        output.close();
    }
    
    /**
     * This function accepts 1 arguments that will be written into the required <code>"suncertify.properties"</code> which
     * is located in the current working directory. The argument String value is written into the file using DataOutputStream
     * Its designed for writing property file of the application when running on Alone mode
     * @see DataOutputStream
     * @param dbPath - The String value of the database path
     * @throws IOException - if an error occur when writing to the designated stream
     */
    public static void writeAloneProperties(String dbPath) throws IOException{
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream output = new FileOutputStream(file);
        DataOutputStream data = new DataOutputStream(output);
        data.writeUTF(dbPath + "\n");
        data.close();
        output.close();
    }
    
    /**
     * This function reads the alone configured property file from the required <code>"suncertify.properties"</code> file as
     * @return - a string value of the database location/path
     * @throws IOException - if any Input and Output Error occur at reading file
     */
    public static String readAloneProperties() throws IOException{
        String db = "";
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        InputStream input = new FileInputStream(file);
        DataInputStream data = new DataInputStream(input);
        db = data.readUTF().trim();
        data.close();
        input.close();
        return db;
    }
    
    /**
     * This function reads the client configured property file from the required <code>"suncertify.properties"</code> file as
     * @return - an array of String value of the host name or IP address at index 0 and the port number on index 1
     * @throws IOException - if any Input and Output Error occur at reading file
     */
    public static String[] readClientProperties() throws IOException{
        String []config = new String[2];
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        InputStream input = new FileInputStream(file);
        DataInputStream data = new DataInputStream(input);
        config[0] = data.readUTF().trim();//the host name
        config[1] = String.valueOf(data.readLong()); //the port number
        data.close();
        input.close();
        return config;
    }
    
    /**
     * This function reads the server configured property file from the required <code>"suncertify.properties"</code> file as
     * required by the specification
     * @return an array of String size 2 which the first element represents the database location and the second element represent the port number
     * off the server which can be connected to remotely
     * @throws IOException - if any Input and Output Error occur at reading file
     */
    public static String[] readServerProperties() throws IOException{
        String []config = new String[2];
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }
        InputStream input = new FileInputStream(file);
        DataInputStream data = new DataInputStream(input);
        config[0] = data.readUTF().trim();
        config[1] = String.valueOf(data.readLong());
        data.close();
        input.close();
        return config;
    }
    
    /**
     * This function check if this application property file exist or not
     * @return true if The application was previously executed and had a property file created in working directory or false if not
     */
    public static boolean isPropertiesFileExist(){
        File file = new File(new File("").getAbsolutePath() + "\\" + PROPERTY_FILE_NAME);
        if(file.exists()){
            return true; 
        }
        else{
            return false;
        }
    }
}
