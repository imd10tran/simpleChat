import common.ChatIF;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
	/*
	 * Instance variables
	 */
	final public static int DEFAULT_PORT = 5555;
	private EchoServer server;
	private Scanner fromConsole;
	
	/**
	 * Constructor
	 * @param port
	 */
	public ServerConsole (int port) {
		server = new EchoServer(port);
		try 
	    {
	      server.listen(); //Start listening for connections
	      
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
		fromConsole = new Scanner(System.in);
	}

	@Override
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}
	
	/**
	 * This is similar to the ClientConsole where it will constantly be looking for the user input.
	 * This handles any kind of functionality that the user might want to perform.
	 */
	public void accept() {
		try {
			String message;
			
			while (true) {
				message = fromConsole.nextLine();
				if (message.charAt(0)=='#') {
					if (message.equals("#quit")) {
						server.quit();
						System.exit(0);
					}else if (message.charAt(1)=='s') {
						if (message.equals("#stop")) {
							 server.stopListening();
						}else if (message.equals("#start")){
							try {
								server.listen();
							}catch (IOException e) {
								
							}
						}else if (message.contains("#setport")) {
							server.setPort(Integer.valueOf(processMessage(message)));
						}
					}else if (message.equals("#close")) {
						server.sendToAllClients("WARNING - The server has stopped listening for connections\nSERVER SHUTTING DOWN! DISCONNECTING!\nAbnormal termination of connection.");
						server.close();
					}else if (message.equals("#getport")) {
						System.out.println("Server port: " + server.getPort());
					}
				} else {
					display(message);
					server.sendToAllClients("SERVER MSG> " +message);
				}
			}
		} catch (Exception e) {
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
	 * Creation of the server UI
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 0;
		
		try {
			port = Integer.valueOf(args[0]);
		}catch (ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		}
		ServerConsole server = new ServerConsole (port);
		server.accept();
	}

}
