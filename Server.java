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
		    		ArrayList<ArrayList<String>> contacts = new ArrayList<ArrayList<String>>();
				readFile(contacts);
				final int max = 20;
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
						os.println("301 invalid command");
				   
				    os.println("200 OK"); 
				}
		
				//close input and output stream and socket
				is.close();
				os.close();
				serviceSocket.close();
				if(line.equals("SHUTDOWN")){
					//write data to file
					myServerice.close();
					break;
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
			
			System.out.println("Does not exists");
		}
		while(x.hasNextLine())
		{
			ArrayList<String> c1= new ArrayList<String>();
			String [] parts = x.nextLine().split(" "); // using next line to read entire line instead of bit of data
			for (int i=0;i <parts.length;i++)
				c1.add(parts[i]);
			
			contacts.add(c1);
		}
		
		x.close();
	}
	
	static void add(String line,ArrayList<ArrayList<String>> contacts,PrintStream os, int max) {
		
	  if(contacts.size()<max) {
		contacts.add(new ArrayList<String>());
	    	int spot=contacts.size()-1;
		String[] parts = line.split(" ");
		
		//contacts.get(spot).add(Integer.toString(spot+1001)); // ID generated
		int id = Integer.parseInt(contacts.get(spot-1).get(0))+1;
		
		contacts.get(spot).add(Integer.toString(id));
		contacts.get(spot).add(parts[1]); // ADD FNAME 
		contacts.get(spot).add(parts[2]); // ADD LNAME
		
		if(parts[3].matches("(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}")) // check if phone follows format
			contacts.get(spot).add(parts[3]);  // if follows format, insert into array
		else {
			System.out.println("Throw exception"); 
			contacts.get(spot).clear();}
	  }
	  else
		  System.out.println("Can not insert data, file is full");
		// if exception thrown delete the data from that row 
		// send  message "The new Record ID is " + contacts.get(spot).get(0);
	} // DONE
	
	static void delete(ArrayList<ArrayList<String>> contacts, String line,PrintStream os) {
	
		String[] parts = line.split(" ");
		int result = Integer.parseInt(parts[1]);
				
		result -= 1001;
		
		contacts.remove(result);
		os.println("200 OK");  
		
		// return error 403 the record ID does not exist
	}// DONE
	
	static void list(ArrayList<ArrayList<String>> contacts,PrintStream os){	
		String output="";
		for(int i=0;i< contacts.size();i++) {
			for(int j=0;j<contacts.get(i).size();j++) {
				System.out.print(contacts.get(i).get(j)+"\t");
				//output+=contacts.get(i).get(j);
				//output+="\t";
			}
			//output+="=";
			System.out.println();
		}
		os.println(output);
	} //DONE

}