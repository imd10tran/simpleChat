// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  private int port;
  private String id;
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String id) 
  {
	  
    try 
    {
      client= new ChatClient(host, port, this,id);
      this.id = id;
      
    } 
    catch(IOException exception) 
    {}
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
    
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   * This also accepts or reads any function that the user wants to perform.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if (message.charAt(0)=='#') {
        	if(message.equals("#quit")) {
        		if (client.isConnected()) {
        			client.handleMessageFromClientUI(id+" has disconnected.");
        			client.quit();
        		}
        		System.exit(0);
        		break;
        	}else if (message.charAt(1)=='l') {
        		if (message.equals("#logoff")) {
        			client.handleMessageFromClientUI(id+" has disconnected.");
        			client.closeConnection();
        		}else if(message.contains("#login")) {
        			if (!client.isConnected()) {
        				client.openConnection();
        				client.sendToServer(message);
        			}else {
        				System.out.println("ERROR: Already logged in.");
        			}
        		}
        	}else if (message.charAt(1)=='s') {
        		if (message.contains("#sethost")) {
        			if (!client.isConnected()) {
        				client.setHost(processMessage(message));
        				this.display("Host set to: "+processMessage(message));
        			}else {
        				System.out.println("ERROR: Still logged in. Please logout.");
        			}
        			
        		}else if (message.contains("#setport")) {
        			if (!client.isConnected()) {
        				client.setPort(Integer.valueOf(processMessage(message)));
        				this.display("Port set to: "+processMessage(message));
        			}else {
        				System.out.println("ERROR: Still logged in. Please logout.");
        			}
        		}
        	}else if (message.charAt(1)=='g') {
        		if (message.equals("#gethost")) {
        			System.out.println("Host: "+client.getHost());
        		}else if (message.equals("#getport")) {
        			System.out.println("Port: "+client.getPort());
        		}
        	}
        } else {
        	client.handleMessageFromClientUI(message);
        }
        
      }
    } 
    catch (Exception ex) 
    {
    	
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
  
  /**
   * This method will process inputs from the user that goes with the format of #setport <port> for example.
   * @param message
   * @return finished
   */
  private String processMessage(String message) {
	  String finished = "";
	  String[] firstProcess = message.split(" ");
	  finished = firstProcess[1];
	  String[] secondProcess = finished.split("<");
	  finished = secondProcess[1];
	  String [] finalProcess = finished.split(">");
	  finished = finalProcess[0];
	  return finished;
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods *************************************************** 
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	Scanner fromConsole = new Scanner(System.in);
    String host = "";
    String id = "";
    int port = 0;
    try {
    	id = args[0];
    }catch (ArrayIndexOutOfBoundsException e) {
    	System.out.println("ERROR - No login ID specified. Connection aborted.");
    	System.exit(0);
    }

    try
    {
      host = args[1];
      port = Integer.valueOf(args[2]);
      
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }
    
    ClientConsole chat =  new ClientConsole(host,port,id);
 
    
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
