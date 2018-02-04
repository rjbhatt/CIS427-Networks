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
		ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();
		readFile(contacts);
		try {// Try to open a server socket 
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
		    		
				final int max = 20;
				serviceSocket = myServerice.accept();
				is = new BufferedReader (new InputStreamReader(serviceSocket.getInputStream()));
				os = new PrintStream(serviceSocket.getOutputStream());
		
				while ((line = is.readLine()) != null) 
				{	
					try {
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
					}
					catch (Exception e) 
				    {
				    		os.println("300 invalid command");
				    }   
				}
		
				//close input and output stream and socket
				is.close();
				os.close();
				serviceSocket.close();
				try {
				if(line.equals("SHUTDOWN")){
					writeFile(contacts);
					myServerice.close();
					break;
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
    
    private static Scanner x;
	static void readFile(ArrayList<ArrayList<String>> contacts)
	{
		try{
			x = new Scanner (new File("contacts.txt"));
			System.out.println("File Opened");
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
	
	static void add(String line,ArrayList<ArrayList<String>> contacts,PrintStream os, int max) {
	  String[] parts = line.split(" ");
	  if(contacts.size()<max&& parts.length==4) {
		  contacts.add(new ArrayList<String>());
		  int spot=contacts.size()-1; 
		  int id = Integer.parseInt(contacts.get(spot-1).get(0))+1;
			
		  contacts.get(spot).add(Integer.toString(id));
		  contacts.get(spot).add(parts[1]); // ADD FNAME 
		  contacts.get(spot).add(parts[2]); // ADD LNAME
			
		  if(parts[3].matches("(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}")) { // check if phone follows format
			  contacts.get(spot).add(parts[3]);  // if follows format, insert into array
			  os.println("ADD=200 OK=The new Record ID is " + Integer.toString(id));
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
	
	static void list(ArrayList<ArrayList<String>> contacts,PrintStream os){	
		String output="";
		for(int i=0;i< contacts.size();i++) {
			for(int j=0;j<contacts.get(i).size();j++) {			
				output+=contacts.get(i).get(j);
				output+="\t";
			}
			output+="=";
		}
		os.println("LIST=200 OK=The List of records in the book="+output);
	} 

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