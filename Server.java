/*
 * Server.java
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Server {

    public static final int SERVER_PORT = 3246;

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
		
		// As long as we receive data, echo that data back to the client.
		ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();
		 final int max = 20;
		// fileimport(contacts);
		while ((line = is.readLine()) != null) 
		{	
			//ADD
			if (line.substring(0, 3).equals("ADD"))
				System.out.println("function" + line); // REMOVE ADD FROM STRING AND PASS TO FUNCTION
			// Delete
			else  if (line.substring(0, 4).equals("LIST"))
				System.out.println("function" + line);
			// LIST
			else if (line.substring(0, 4).equals("QUIT"))
				System.out.println("function" + line);
			// QUIT
			else if (line.substring(0, 6).equals("DELETE"))
				System.out.println("function" + line);
				// closes the clients connection
			//SHUTDOWN
			else if (line.substring(0, 8).equals("SHUTDOWN")) {
				System.out.println("function" + line);
				break;
				
			}
				// write the data of the array lists to the file 
		   
		    os.println(line); 
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
			
		}
		catch(Exception e){
			
			System.out.println("Does not exists");
		}
	}
	
	public void readFile(ArrayList<ArrayList<String>> contacts)
	{
		int rows=0;
		while(x.hasNext())
		{
			rows++;
		}
		
		
		x.close();
		
	}
	
	
	void add(String line,ArrayList<ArrayList<String>> contacts) {
		//if(spotOpen(contacts))
		// insert data
	    //else
		
		String[] parts = line.split(" ");
		//create the id --> push to first column
			// contracts.size()+1001
		contacts.add(new ArrayList<String>());
		contacts.get(contacts.size()-1).add(Integer.toString(contacts.size()+1000)); // ID generated
		contacts.get(contacts.size()-1).add(parts[0]); // ADD FNAME 
		contacts.get(contacts.size()-1).add(parts[1]); // ADD LNAME
		if(phonePattern(parts[2])) // ADD PHONE NUMBER 
			contacts.get(contacts.size()-1).add(parts[2]);
		else
			System.out.println("Throw exception");
		// if exception thrown delete the data from that row 
		
		// return code 200 ok
	}
	
	void delete(ArrayList<ArrayList<String>> contacts, String ID) {
		int result = Integer.parseInt(ID);
		System.out.println(result);
		result -= 1001;
		
		contacts.get(result).clear();
		//return code 200 ok
	}
	
	void list(ArrayList<ArrayList<String>> contacts,PrintStream os){
		for(int i=0;i< contacts.size();i++)
			for(int j=0;j<contacts.get(i).size();j++)
				os.println(j + " \t");
	}

	boolean spotOpen(ArrayList<ArrayList<String>> contacts) {
		for(int i=0;i<contacts.size();i++)
			if(contacts.get(i).get(0)!= null)
				return true;
		return false;
	}
	boolean phonePattern(String phone) {
		
		boolean match = false;
	      String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
	      match = phone.matches(phoneNumberPattern);
	      if(match)
	    	  	return true;
		
		return false;
	}
}
