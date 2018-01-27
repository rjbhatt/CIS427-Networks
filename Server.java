/*
 * Server.java
 */

import java.io.*;
import java.net.*;

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
		while ((line = is.readLine()) != null) 
		{
			System.out.println(line +"1");
			//ADD
			if (line.equals("ADD"))
				System.out.println("function" + line);
			else
				System.out.println("ADD NOT MATCHED");
			// Delete
			// LIST
			// QUIT
				// closes the clients connection
			//SHUTDOWN
				// closes both client and server connections 
				// write the data of the array lists to the file 
				// break while loop 
		    System.out.println(line);
		    os.println(line); 
		}

		//close input and output stream and socket
		is.close();
		os.close();
		serviceSocket.close();
	    }   
	    catch (IOException e) 
	    {
		System.out.println(e);
	    }
	}
    }
}
