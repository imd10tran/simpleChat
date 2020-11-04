// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  if (client.getInfo("login")==null) {
		  client.setInfo("login",true);
		  System.out.println("Message received: " + msg+ " from " + client.getInfo("id"));
		  client.setInfo("id",processMessage((String)msg));
		  System.out.println(client.getInfo("id")+" has logged on.");
		  try {
			client.sendToClient(client.getInfo("id")+" has logged on.");
		} catch (IOException e) {
		}
	  }else if ((((String) msg).contains("#login")) && (client.getInfo("login").equals(true))){
		  System.out.println("An error has occured.");
	  }else {
	  System.out.println("Message received: " + msg + " from " + client.getInfo("id"));
	  this.sendToAllClients(client.getInfo("id")+": "+msg);
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
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  protected void clientConnected(ConnectionToClient client) {
//	  System.out.println("Client: "+client.toString()+" has connected.");
//	  System.out.println("Count: "+this.getNumberOfClients());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    
  }
  /**
   * This method ovverides the one in the superclass. 
   * called when a client had disconnected.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client: "+client.getInfo("id")+" has disconnected.");
	  this.sendToAllClients("Client: "+client.getInfo("id")+" has disconnected.");
	  client.setInfo("login",false);
	  
  }
  
  /**
   * This method terminates the server
   */
  public void quit() {
	  try {
		  this.close();
	  } catch (IOException e) {
		  
	  }
	  System.exit(0);
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
      
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
