/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Server {
    public static final int SERVER_PORT = 3245;

    public static void main(String args[]) 
    {
		ServerSocket myServerice = null;
		String line;
		BufferedReader is;
		PrintStream os;
		Socket serviceSocket = null;
	
		// Try to open a server socket 
		try {
		    myServerice = new ServerSocket(SERVER_PORT);
		    System.out.println("Connected");
		}
		catch (IOException e) {
		    System.out.println(e);
		    System.out.println("Connection failed");
		}   
	
		// Create a socket object from the ServerSocket to listen and accept connections.
		// Open input and output streams
	
		while (true)
		{
		    try 
		    {
				serviceSocket = myServerice.accept();
				is = new BufferedReader (new InputStreamReader(serviceSocket.getInputStream()));
				os = new PrintStream(serviceSocket.getOutputStream());
		
				// Open the file holding data
				// move data into the arraylist
				
				ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();
				 final int max = 20;
				// fileimport(contacts);
				while ((line = is.readLine()) != null) 
				{	
					//ADD
					if (line.substring(0, 3).equals("ADD")) 
						add(line,contacts,os,max);
					else  if (line.substring(0, 4).equals("LIST"))
						list (contacts,os);
					else if (line.substring(0, 4).equals("QUIT"))
						os.println(line);
					else if (line.substring(0, 6).equals("DELETE")) 
						delete(contacts, line, os);
					else if (line.substring(0, 8).equals("SHUTDOWN")) {
						 os.println("200 OK"); 
						break;
					}
					// write the data of the array lists to the file 
				   
				    os.println("200 OK"); 
				}
		
				//close input and output stream and socket
				is.close();
				os.close();
				serviceSocket.close();
				System.out.println("Server DOWN");
				System.exit(0);
		    }   
		    catch (IOException e) 
		    {
		    		System.out.println(e);
		    }
		}
    }
    
    private Scanner x;
	public void openFile()
	{
		try{
			x = new Scanner (new File("conatcts.txt"));
			System.out.println("File Opened");
			
		}
		catch(Exception e){
			
			System.out.println("Does not exists");
		}
	}
	
	public void readFile(ArrayList<ArrayList<String>> contacts)
	{
		int rows=0;
		while(x.hasNext())
			rows++;
		
		x.close();
	}
	
	static void add(String line,ArrayList<ArrayList<String>> contacts,PrintStream os, int max) {
		//if(spotOpen(contacts))
		// insert data
	    //else
		if (contacts.size()<max) {
		String[] parts = line.split(" ");
		contacts.add(new ArrayList<String>());
		contacts.get(contacts.size()-1).add(Integer.toString(contacts.size()+1000)); // ID generated
		contacts.get(contacts.size()-1).add(parts[1]); // ADD FNAME 
		contacts.get(contacts.size()-1).add(parts[2]); // ADD LNAME
		
		if(parts[3].matches("(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}")) // check if phone follows format
			contacts.get(contacts.size()-1).add(parts[3]);  // if follows format, insert into array
		else
			System.out.println("Throw exception");
		
		// if exception thrown delete the data from that row 
		}
	}
	
	static void delete(ArrayList<ArrayList<String>> contacts, String line,PrintStream os) {
	
		String[] parts = line.split(" ");
		int result = Integer.parseInt(parts[1]);
				
		result -= 1001;
		contacts.get(result).clear();
		os.println("200 OK"); 
	}// DONE
	
	static void list(ArrayList<ArrayList<String>> contacts,PrintStream os){		
		for(int i=0;i< contacts.size();i++) {
			for(int j=0;j<contacts.get(i).size();j++) {
				System.out.print(contacts.get(i).get(j) + " \t");
			}
			System.out.println();
		}
	} 

	boolean spotOpen(ArrayList<ArrayList<String>> contacts) {
		for(int i=0;i<contacts.size();i++)
			if(contacts.get(i).get(0)== null)
				return true;
		return false;
	}
}
