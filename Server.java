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
		ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>(); // Holds Data from file
		readFile(contacts);
		try {// Try to open a server socket 
		    myServerice = new ServerSocket(SERVER_PORT);
		    System.out.println("Connected");
		}
		catch (IOException e) { // if connection failed
		    System.out.println(e);
		    System.out.println("Connection failed");
		}   

		// Create a socket object from the ServerSocket to listen and accept connections.
		// Open input and output streams
		while (true)
		{
		    try 
		    {	
				final int max = 20; // max contacts allowed
				serviceSocket = myServerice.accept();
				is = new BufferedReader (new InputStreamReader(serviceSocket.getInputStream()));
				os = new PrintStream(serviceSocket.getOutputStream());
		
				while ((line = is.readLine()) != null) 
				{	
					
						if (line.substring(0, 3).equals("ADD")) 
							add(line,contacts,os,max);
						else  if (line.substring(0, 4).equals("LIST")) 
							list (contacts,os); 
						else if (line.substring(0, 4).equals("QUIT")) {
							os.println(line);
							break;}
						else if (line.substring(0, 6).equals("DELETE")) 
							delete(contacts, line, os);
						else if (line.substring(0, 8).equals("SHUTDOWN")) {
							os.println(line +" 200 OK"); 
							break;
						}
						else
							os.println("300 invalid command");
					
					
				}
		
				//close input and output stream and socket if QUIT or SHUTDOWN is passed
				is.close();
				os.close();
				serviceSocket.close();
				try {
					if(line.equals("SHUTDOWN")){ // ONLY SHUTDOWN command
						writeFile(contacts); // Write 2d array to file
						myServerice.close(); // close service
						break; // breaks loop and closes server
				}}
				catch(Exception e) {
					System.out.println("client closed ");
				}
		    }   
		    catch (IOException e) 
		    {
		    		System.out.println(e);
		    }
		}
    }
    
    //PRE: NONE
    //Post: Attempt open file, if successful, for each line, create row for array list, split the line at each space
    //		add each piece of info into that row, add row to the 2d array, close file
    private static Scanner x;
	static void readFile(ArrayList<ArrayList<String>> contacts)
	{
		try{
			x = new Scanner (new File("contacts.txt"));
		}
		catch(Exception e){
			
			System.out.println("File does not exists");
		}
		while(x.hasNextLine())
		{
			ArrayList<String> c1= new ArrayList<String>();
			String [] parts = x.nextLine().split(" "); // using next line to read entire line instead each of bit of data
			for (int i=0;i <parts.length;i++)
				c1.add(parts[i]);	
			contacts.add(c1);
		}
		x.close();
	}
	
	
	//PRE:command called, matches format
	//POST: Splits string into 4 parts,if string less than 4 parts, print error 301
	//		check if number contacts is maxed out, if not generate id based on highest id present
	//		create new row and  add ID, First name, last name. Check format of the phone number, 
	//		if matches expression, then we add phone number and contact is inserted
	// 		else, error 301 printed and row is removed. 
	static void add(String line,ArrayList<ArrayList<String>> contacts,PrintStream os, int max) {
	  String[] parts = line.split(" ");
	  if(contacts.size()<max&& parts.length==4) {
		  contacts.add(new ArrayList<String>());
		  int spot=contacts.size()-1; 
		  int id = 0;
		 if(contacts.size()-1==0)
			 id=1001;
		 else
			 id=Integer.parseInt(contacts.get(spot-1).get(0))+1;
		  
		  contacts.get(spot).add(Integer.toString(id));
		  contacts.get(spot).add(parts[1]); // ADD FNAME 
		  contacts.get(spot).add(parts[2]); // ADD LNAME
			
		  if(parts[3].matches("(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}")) { // check if phone follows format
			  contacts.get(spot).add(parts[3]);  // if follows format, insert into array
			  os.println("ADD=200 OK=The new Record ID is " + Integer.toString(id)); // = is key to seperate string on client side
			}
		  else {
			  os.println("301 Message Format Error ");		
			  contacts.remove(spot);
			}
		}
	  else if(parts.length<4)
		  os.println("301 Message Format Error");
		
	  else
		  os.println("Can not insert data, file is full");
	} 
	
	//PRE: command called, Index is in database, or not found
	//POST: if found delete row, else output error, output error if format for command doesn't
	// match program requirements
	static void delete(ArrayList<ArrayList<String>> contacts, String line,PrintStream os) {
	
		String[] parts = line.split(" "); 
		Boolean found = false;
		if (parts.length<2)
			os.println("301 Message Format Error");
		else {
			
			for(int i=0;i<contacts.size();i++) {
				if(contacts.get(i).get(0).equals(parts[1])) {
					contacts.remove(i);
					found =true;
					break;
				}
			}
		if (found)
			os.println("200 OK");
		else
			os.println("403 Record ID does not exist");
		}
	}
	
	//PRE:command called
	//POST: concatenate data together with equal sign after each row
	// 	output to client data and 200 OK
	static void list(ArrayList<ArrayList<String>> contacts,PrintStream os){	
		String output="";
		for(int i=0;i< contacts.size();i++) {
			for(int j=0;j<contacts.get(i).size();j++) {			
				output+=contacts.get(i).get(j);
				output+="\t";
			}
			output+="="; // inserted to split string on client side
		}
		os.println("LIST=200 OK=The List of records in the book="+output);
	} 

	//PRE: command called, Data present in database
	//POST: Write each line to selected file
	static void writeFile(ArrayList<ArrayList<String>> contacts) throws IOException {
		PrintWriter ons = new PrintWriter("contacts.txt"); 
	    
	    for(int i=0;i<contacts.size();i++) {
	    		for(int j=0;j<contacts.get(i).size();j++){
	    			ons.write(contacts.get(i).get(j)+" ");
	    		}
	    		ons.write("\n");
	    }
	    ons.close();
	}
}